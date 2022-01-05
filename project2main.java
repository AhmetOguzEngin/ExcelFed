import java.io.File;
import java.util.PriorityQueue;
import java.util.Scanner;

public class project2main {
	public static void main(String[] args) {
		File infile = new File(args[0]);
		File outfile = new File(args[1]);
		Scanner sc; 
		try {
			sc = new Scanner(infile);
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
		//Input handling part
		//Getting information about players
		int nmofplayers = sc.nextInt();
		Player[] players = new Player[nmofplayers];
		for(int ii = 0; ii < nmofplayers; ii++) {
			Player pl = new Player(sc.nextInt(), sc.nextInt());
			players[ii] = pl;
		}
		PriorityQueue<Event> eventq = new PriorityQueue<Event>(new ComparebyTime());
		//Events and their information
		int nmofevents = sc.nextInt();
		for(int kk = 0; kk < nmofevents; kk++) {
			Event event = new Event(sc.next(), players[sc.nextInt()], sc.nextDouble(), sc.nextDouble());
			eventq.add(event);
		}
		//Getting physiotherapists and their information.  		
		int numofphysiotherapist = sc.nextInt();
		PriorityQueue<Physiotherapist> physiotherapists = new PriorityQueue<Physiotherapist>(); 
		Physiotherapist[] physiotherapistlist = new Physiotherapist[numofphysiotherapist];
		for(int ll = 0; ll < numofphysiotherapist; ll++) {
			float servicetime = sc.nextFloat();
			Physiotherapist physioterapist = new Physiotherapist(ll, servicetime);
			physiotherapistlist[ll] = physioterapist;
			physiotherapists.add(physioterapist);
		}
		int numofcoaches = sc.nextInt();
		int numofmasseurs = sc.nextInt();
		ExcelFedSimulation.runExcelFedSimulation(players, eventq, physiotherapists,physiotherapistlist, numofcoaches, numofmasseurs, outfile);
	}
}
