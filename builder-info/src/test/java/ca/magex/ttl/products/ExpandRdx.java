package ca.magex.ttl.products;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.xerces.impl.dv.util.Base64;

public class ExpandRdx {

	public static void main(String[] args) throws Exception {
		for (File file : new File("src/main/resources").listFiles()) {
			if (file.getName().endsWith(".rdx")) {
				new ExpandRdx().run(file);
			}
		}
	}
	
	private static final Pattern PREFIX = Pattern.compile("@prefix\\s+([^\\s]+):\\s+<(.*)>\\s+\\.\\s*");
	
	private static final Pattern TRIPLE = Pattern.compile("([a-z][^\\s]+)\\s+([^\\s]+):([^\\s]+)\\s+(.*[^\\s])\\s+\\.\\s*");
	
	private void run(File file) throws Exception {
		System.out.println("Parsing: " + file.getAbsolutePath());
		Map<String, String> prefixes = new HashMap<String, String>();
		String[] lines = FileUtils.readFileToString(file).split("\n");
		StringBuilder sb = new StringBuilder();
		for (String line : lines) {
			Matcher prefix = PREFIX.matcher(line);
			if (prefix.matches()) {
				String name = prefix.group(1);
				String url = prefix.group(2);
				prefixes.put(name, url);
			}
			Matcher triple = TRIPLE.matcher(line);
			if (triple.matches()) {
				String subject = mid(triple.group(1));
				String domain = triple.group(2);
				String type = triple.group(3);
				String property = "<" + prefixes.get(domain) + type + ">";
				String value = triple.group(4);
				if (value.matches("^[a-z].*"))
					value = mid(value);
				String n3 = subject + " " + property + " " + value + " .";
				System.out.println(n3);
				sb.append(n3);
				sb.append("\n");
			}
		}
		File output = new File(file.getParentFile(), file.getName().replaceAll("\\.[a-z]+$", ".ttl"));
		FileUtils.writeStringToFile(output, sb.toString());
		System.out.println("File written: " + output.getAbsolutePath());
	}
	
	private String mid(String input) {
		return "<http://magex.ca/data/mid/" + 
				Base64.encode(DigestUtils.md5(input)).replaceAll("[^a-zA-Z0-9]", "").substring(0, 10) + 
				">";
	}
	
}
