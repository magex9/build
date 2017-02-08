package ca.magex.rdf.ttl;

public class Property implements Predicate {

	public static final String PREFIX = "http://magex.ca/data/property/";
	
	private Type type;
	
	private String name;
	
	public Property(Type type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public Property(String domain, String type, String property) {
		this.type = new Type(domain, type);
		this.name = property;
	}
	
	public Type getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString(String seperator) {
		return type.toString(seperator) + seperator + name;
	}
	
	@Override
	public String toString() {
		return "prop:" + toString(".");
	}
	
}
