
public class Event {
	String title;
	double time;
	double processtime;
	Player player;
	int physid;
	public Event(String title,Player player, double time, double processtime) {
		this.title = title;
		this.time = time;
		this.player = player;
		this.processtime = processtime;
	}
	public Event(String title,Player player, double time, int physid) {
		this.title = title;
		this.time = time;
		this.player = player;
		this.physid = physid;
	}

}
