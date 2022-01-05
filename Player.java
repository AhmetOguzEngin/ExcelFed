
public class Player {
	private int id;
	private int sklevel;
	private int numofmessages;
	private boolean available;
	private double messagetimewait;
	private double mosttimephyswait;
	public Player(int id, int sklevel) {
		this.id = id;
		this.sklevel = sklevel;
		this.numofmessages = 0;
		this.available = true;
		this.messagetimewait = 0;
		this.mosttimephyswait = 0;
	}
	public int getId() {
		return id;
	}
	public int getSklevel() {
		return sklevel;
	}
	public int getNumofmessages() {
		return numofmessages;
	}
	public void setNumofmessages(int numofmessages) {
		this.numofmessages = numofmessages;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public double getMessagetimewait() {
		return messagetimewait;
	}
	public void setMessagetimewait(double messagetimewait) {
		this.messagetimewait = messagetimewait;
	}
	public double getMosttimephyswait() {
		return mosttimephyswait;
	}
	public void setMosttimephyswait(double mosttimephyswait) {
		this.mosttimephyswait = mosttimephyswait;
	}
	
}
