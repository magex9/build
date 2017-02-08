package ca.magex.rdf.ttl;

public class Type {

	private Domain domain;
	
	private String name;
	
	public Type(Domain domain, String name) {
		this.domain = domain;
		this.name = name;
	}
	
	public Type(String domain, String type) {
		this.domain = new Domain(domain);
		this.name = type;
	}
	
	public Domain getDomain() {
		return domain;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString(String seperator) {
		return domain.toString() + seperator + name;
	}
	
	@Override
	public String toString() {
		return toString(".");
	}
	
}
