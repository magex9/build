package ca.magex.rdf.ttl;

public class Triple {

	private Subject subject;
	
	private Predicate predicate;
	
	private Value value;
	
	public Triple(Subject subject, Predicate predicate, Value value) {
		super();
		this.subject = subject;
		this.predicate = predicate;
		this.value = value;
	}

	public Subject getSubject() {
		return subject;
	}
	
	public Predicate getPredicate() {
		return predicate;
	}
	
	public Value getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return subject.toString() + "\t" + predicate.toString() + "\t" + value.toString() + "\t.";
	}
	
}
