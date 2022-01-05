import java.util.Comparator;

public class ComparebySkill implements Comparator<Event> {
	//This class is to compare events according to players skill-level, then time and lastly ID.
	@Override
	public int compare(Event o1, Event o2) {
		double pl1sklevel = o1.player.getSklevel();
		double pl2sklevel = o2.player.getSklevel();
		if(pl1sklevel != pl2sklevel) {
			if(pl1sklevel > pl2sklevel) return -1;
			else return 1;
		}
		else if(o1.time < o2.time) return -1;
		else if(o2.time < o1.time) return 1;
		else {
			if(o1.player.getId()<o2.player.getId()) return -1;
			else return 1;
		}
	}
	
}
