package ca.magex.rdf.ttl;

public class TextValue implements Value {

	private String text;
	
	public TextValue(String text) {
		this.text = text;
	}
	
	public String toString() {
		return "'" + text.replaceAll("'", "\\\\'") + "'";
	}
	
}
