package ca.magex.rdf.ttl;

import org.apache.commons.codec.digest.DigestUtils;

public class EntityRef implements Subject, Value {

	private final String key;
	
	private final Type type;
	
	public EntityRef(Type type, String ref) {
		this.key = buildKey(type, ref);
		this.type = type;
	}
	
	public EntityRef(String domain, String type, String ref) {
		this.type = new Type(domain, type);
		this.key = buildKey(this.type, ref);
	}

	public static String buildKey(Type type, String ref) {
		return DigestUtils.md5Hex(type.toString() + "/" + ref).substring(0, 10);
	}
	
	public Type getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return PREFIX + key;
	}
	
}
