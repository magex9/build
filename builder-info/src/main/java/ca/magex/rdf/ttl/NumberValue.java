package ca.magex.rdf.ttl;

public class NumberValue implements Value {

	private Number number;
	
	public NumberValue(Number number) {
		this.number = number;
	}
	
	public String toString() {
		return number == null ? "null" : number.toString();
	}
	
}
