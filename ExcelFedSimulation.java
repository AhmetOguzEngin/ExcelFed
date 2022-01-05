import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.PriorityQueue;

public class ExcelFedSimulation {
	public static void runExcelFedSimulation(Player[] players, PriorityQueue<Event> events, PriorityQueue<Physiotherapist> physiotherapists,Physiotherapist[] physiotherapistlist,
			int numofcoaches, int numofmasseurs, File outfile) {
		//Data collecting for output
		int maxlengthtr = 0;
		int maxlengthpyhs = 0;
		int maxlengthmsg = 0;
		double sumofwaitingtimetr = 0;
		int numofpeopletr = 0;
		double sumofwaitingtimephys = 0;
		int numofpeoplephys = 0;
		double sumofwaitingtimemsg = 0;
		int numofpeoplemsg = 0;
		double sumoftrainingtime = 0;
		double sumofphystime = 0;
		double sumofmsgtime = 0;
		int idofmosttimephys = 0;
		double timeofmosttimephys = 0;
		int idofmintimemsg = -1;
		double timeofmintimemsg = -1;
		double currenttime = 0;
		//End of variables

		int nmofInvalidAttempt = 0;
		int nmofCancelledAttempt = 0;
		PriorityQueue<Event> physiotherapyQueue = new PriorityQueue<Event>(new ComparebyTrainingtime());
		PriorityQueue<Event> trainingQueue = new PriorityQueue<Event>(new ComparebyTime());
		PriorityQueue<Event> messageQueue = new PriorityQueue<Event>(new ComparebySkill());
		while(!events.isEmpty()) {
			Event currentEvent = events.poll();
			currenttime = currentEvent.time;
			
			//Takes training event, if there is any coach available decreases the number of coaches else puts player to the queue. 
			
			if(currentEvent.title.equals("t") ) {
				if(currentEvent.player.isAvailable() == false) {
					nmofCancelledAttempt++;
					continue;
				}
				numofpeopletr++;
				sumoftrainingtime += currentEvent.processtime;
				if(numofcoaches > 0) {
					numofcoaches--;
					currentEvent.player.setAvailable(false);
					events.add(new Event("et", currentEvent.player, currentEvent.time + currentEvent.processtime, currentEvent.processtime));
				}
				else {
					currentEvent.player.setAvailable(false);
					trainingQueue.add(currentEvent);
					//This part is to find the max length of trainingqueue.
					if(trainingQueue.size() > maxlengthtr) {
						maxlengthtr = trainingQueue.size();
					}
				}
			}
			//This part is to find player exiting the training and if there is any physiotherapist available decreases the number else puts player to the queue. 			
			else if(currentEvent.title.equals("et")) {
				if(!trainingQueue.isEmpty()) {
					Event eventtobeexecuted = trainingQueue.poll();
					events.add(new Event("et", eventtobeexecuted.player, currentEvent.time + eventtobeexecuted.processtime, eventtobeexecuted.processtime));
					sumofwaitingtimetr += currentEvent.time - eventtobeexecuted.time;

				}
				else {
					numofcoaches++;
				}
				numofpeoplephys++;
				if(!physiotherapists.isEmpty()) {
					Physiotherapist phys =  physiotherapists.poll();
					events.add(new Event("ep", currentEvent.player, currentEvent.time + phys.servicetime, phys.id));
					sumofphystime += phys.servicetime;
				}
				else {
					physiotherapyQueue.add(new Event("p",currentEvent.player, currentEvent.time, currentEvent.processtime));
					if(physiotherapyQueue.size() > maxlengthpyhs) {
						maxlengthpyhs = physiotherapyQueue.size();
					}
				}
			}
			
			
			//This part is to exit from physiotheraphy.			
			else if(currentEvent.title.equals("ep")) {
				currentEvent.player.setAvailable(true);
				Physiotherapist phys =  physiotherapistlist[currentEvent.physid];
				if(!physiotherapyQueue.isEmpty()) {
					Event eventtobeexecutedp = physiotherapyQueue.poll();
					events.add(new Event("ep", eventtobeexecutedp.player, currentEvent.time + phys.servicetime, phys.id));
					eventtobeexecutedp.player.setAvailable(false);
					double waitingtime = currentEvent.time - eventtobeexecutedp.time;
					sumofwaitingtimephys += waitingtime;
					eventtobeexecutedp.player.setMosttimephyswait(eventtobeexecutedp.player.getMosttimephyswait() + waitingtime);
					sumofphystime += phys.servicetime;
					boolean condition = (eventtobeexecutedp.player.getId() < idofmosttimephys && 
							Math.abs(eventtobeexecutedp.player.getMosttimephyswait() - timeofmosttimephys) < 0.0000000001);
					if(	eventtobeexecutedp.player.getMosttimephyswait() > timeofmosttimephys || condition) {
						timeofmosttimephys = eventtobeexecutedp.player.getMosttimephyswait();
						idofmosttimephys = eventtobeexecutedp.player.getId();
					}
				}
				else {
					physiotherapists.add(phys);
					
					
				}
					
			}
			
			//This time is to hangle message events.			
			else if(currentEvent.title.equals("m")) {
				if(currentEvent.player.getNumofmessages() < 3) {
					if(currentEvent.player.isAvailable() == false) {
						nmofCancelledAttempt++;
						continue;
					}
					sumofmsgtime += currentEvent.processtime;
					numofpeoplemsg++;
					currentEvent.player.setNumofmessages(currentEvent.player.getNumofmessages()+1);
					if(numofmasseurs > 0) {
						numofmasseurs--;
						currentEvent.player.setAvailable(false);
						events.add(new Event("em", currentEvent.player, currentEvent.time + currentEvent.processtime, 0));
					}
					else {
						currentEvent.player.setAvailable(false);
						messageQueue.add(currentEvent);
						if(messageQueue.size() > maxlengthmsg) {
							maxlengthmsg = messageQueue.size();
						}

					}
				}
				else {
					nmofInvalidAttempt++;
				}
			}
			
			//This time is to handle exit events.
			else if(currentEvent.title.equals("em")) {
				currentEvent.player.setAvailable(true);
				if(!messageQueue.isEmpty()) {
					Event eventtobeexecutedm = messageQueue.poll();
					events.add(new Event("em", eventtobeexecutedm.player , currentEvent.time + eventtobeexecutedm.processtime, 0));
					eventtobeexecutedm.player.setAvailable(false);
					double waitingtimem = currentEvent.time - eventtobeexecutedm.time;
					eventtobeexecutedm.player.setMessagetimewait(eventtobeexecutedm.player.getMessagetimewait() + waitingtimem);
					sumofwaitingtimemsg += waitingtimem;
					
				}
				else {
					numofmasseurs++;
				}
				boolean condition = (timeofmintimemsg == -1 || (idofmintimemsg > currentEvent.player.getId() && 
						(Math.abs(currentEvent.player.getMessagetimewait() - timeofmintimemsg) < 0.0000000001)) 
						|| currentEvent.player.getMessagetimewait() < timeofmintimemsg );
				if(currentEvent.player.getNumofmessages() == 3 && condition) {
					timeofmintimemsg = currentEvent.player.getMessagetimewait();
					idofmintimemsg = currentEvent.player.getId();
				}
			}
			
			
		
		}
		//This part is to print out outputs.
		PrintStream printout; 
		try {
			printout = new PrintStream(outfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		boolean flag = false;
		double averagewaitingtimetr;
		if(numofpeopletr == 0) {
			averagewaitingtimetr = 0;
		}else {
			averagewaitingtimetr = sumofwaitingtimetr / numofpeopletr;
		}
		
		double averagewaitingtimephys;
		if(numofpeoplephys == 0) {
			averagewaitingtimephys = 0;
		}else {
			averagewaitingtimephys = sumofwaitingtimephys / numofpeoplephys;
		}
		double averagewaitingtimemsg;
		if(numofpeoplemsg == 0) {
			averagewaitingtimemsg = 0;
		}else {
			averagewaitingtimemsg = sumofwaitingtimemsg / numofpeoplemsg;
		}
		double averagetrainingtime; 
		if(numofpeopletr == 0) {
			averagetrainingtime = 0;
		}else {
			averagetrainingtime = sumoftrainingtime / numofpeopletr;
		}
		double averagephystime;
		if(numofpeoplephys == 0) {
			averagephystime = 0;
		}else {
			averagephystime = sumofphystime / numofpeoplephys;
		}
		double averagemsgtime;
		if(numofpeoplemsg == 0) {
			averagemsgtime = 0;
		}else {
			averagemsgtime = sumofmsgtime/numofpeoplemsg;
		}
		double averageturnaroundtime;
		if(numofpeopletr == 0) {
			averageturnaroundtime = 0;
		}else {
			averageturnaroundtime = (sumoftrainingtime + sumofwaitingtimetr + sumofwaitingtimephys + sumofphystime) / numofpeopletr;
		}
		if(timeofmintimemsg == -1) {
			flag = true;
		}
		printout.println(maxlengthtr + "\n" + maxlengthpyhs + "\n" + maxlengthmsg);
		printout.println(String.format("%.3f", averagewaitingtimetr) + "\n" + String.format("%.3f", averagewaitingtimephys) +  "\n" + String.format("%.3f", averagewaitingtimemsg));
		printout.println(String.format("%.3f", averagetrainingtime) + "\n" + String.format("%.3f", averagephystime) +  "\n" + String.format("%.3f", averagemsgtime));
		printout.println(String.format("%.3f",averageturnaroundtime) + "\n" + idofmosttimephys + " " + String.format("%.3f", timeofmosttimephys)); 
		if(flag == false) {
			printout.println(idofmintimemsg + " " + String.format("%.3f", timeofmintimemsg) + "\n" + nmofInvalidAttempt + "\n" + nmofCancelledAttempt + "\n" + String.format("%.3f", currenttime));
		}else {
			printout.println(idofmintimemsg + " " + -1 + "\n" + nmofInvalidAttempt + "\n" + nmofCancelledAttempt + "\n" + String.format("%.3f", currenttime));
		}
		
		
	}
}
