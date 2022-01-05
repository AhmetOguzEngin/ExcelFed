import java.util.Comparator;

public class ComparebyTime implements Comparator<Event> {
	//This class is to compare events according to firstly by time then ID.
	@Override
	public int compare(Event o1, Event o2) {
		if(Math.abs(o1.time - o2.time) > 0.0000000001) {
			if(o1.time > o2.time) return 1;
			else if(o1.time < o2.time) return -1;
		}
		else {
			if(o1.player.getId() < o2.player.getId()) return -1;
			else return 1;
		}
		return 0;
	}


}
