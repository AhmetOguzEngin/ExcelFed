import java.util.Comparator;

public class ComparebyTrainingtime implements Comparator<Event> {
	@Override
	// This class is to compare events by their processtime then time and lastly ID.	
	public int compare(Event o1, Event o2) {
		if(o1.processtime != o2.processtime) {
			if(o1.processtime > o2.processtime) return -1;
			else if(o1.processtime < o2.processtime) return 1;
		}
		else {
			if(o1.time < o2.time) return -1;
			else if(o2.time < o1.time) return 1;
			else if(o1.player.getId() < o2.player.getId()) return -1;
			else return 1;
		}
		return 0;
	}
}
