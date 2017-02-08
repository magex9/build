package ca.magex.rdf.ttl;

public class Topic implements Subject {

	private final String mid;
	
	public Topic(String mid) {
		if (mid.startsWith(Subject.PREFIX))
			this.mid = mid.substring(Subject.PREFIX.length());
		else
			this.mid = mid;
	}
	
	@Override
	public String toString() {
		return PREFIX + mid;
	}
	
}