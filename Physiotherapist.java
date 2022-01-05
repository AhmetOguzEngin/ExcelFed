
public class Physiotherapist implements Comparable<Physiotherapist> {
	int id;
	double servicetime;
	public Physiotherapist(int id, double servicetime) {
		this.id = id;
		this.servicetime = servicetime;
	}
	@Override
	public int compareTo(Physiotherapist o) {
		if(this.id < o.id) return -1;
		else return 1;
	}	
}
