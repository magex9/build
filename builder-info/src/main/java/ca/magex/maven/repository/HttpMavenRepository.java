package ca.magex.maven.repository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.NotImplementedException;

import ca.magex.maven.exceptions.MavenException;
import ca.magex.maven.model.Gav;

public class HttpMavenRepository implements MavenRepository {

	private final String repoId;
	
	private final String baseurl;
	
	public HttpMavenRepository(String repoId, String baseurl) {
		if (!content(baseurl).contains("Index of"))
			throw new MavenException("Base url is not in the expected format: " + baseurl);
		this.repoId = repoId;
		this.baseurl = baseurl;
	}
	
	public List<String> findAllGroupIds() {
		throw new NotImplementedException("Not implemented for http repostory, could take up too much bandwidth");
	}

	public List<String> findRootGroups() {
		String url = baseurl;
		return directories(url);
	}

	public List<String> findChildGroups(String group) {
		String url = baseurl + "/" + group.replaceAll("\\.", "/");
		return directories(url);
	}

	public boolean isGroupId(String groupId) {
		String url = baseurl + "/" + groupId.replaceAll("\\.", "/");
		return !directories(url).isEmpty();
	}

	public List<String> findArtifactIds(String groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> findVersions(String groupId, String artifactId) {
		String url = baseurl + "/" + groupId.replaceAll("\\.", "/") + "/" + artifactId;
		List<String> versions = new ArrayList<String>();
		for (String link : directories(url)) {
			versions.add(link);
		}
		return versions;
	}

	public Gav findPom(String groupId, String artifactId, String version) {
		for (Gav gav : findArtifacts(groupId, artifactId, version)) {
			if (gav.getPackaging().equals("pom"))
				return gav;
		}
		return null;
	}

	public Gav findArtifact(String groupId, String artifactId, String version) {
		for (Gav gav : findArtifacts(groupId, artifactId, version)) {
			if (!gav.getPackaging().equals("pom") && gav.getClassifier() == null)
				return gav;
		}
		return null;
	}

	public List<Gav> findArtifacts(String groupId, String artifactId, String version) {
		String url = baseurl + "/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/" + version;
		List<Gav> artifacts = new ArrayList<Gav>();
		for (String link : files(url)) {
			Gav gav = parse(groupId, artifactId, version, link);
			if (gav != null && !artifacts.contains(gav))
				artifacts.add(gav);
		}
		return artifacts;
	}
	
	private Gav parse(String groupId, String artifactId, String version, String filename) {
		Pattern p = Pattern.compile(artifactId + "-" + version + "(-[^\\.]+)?\\.([^\\.]+)");
		Matcher m = p.matcher(filename);
		if (m.matches()) {
			String classifier = m.group(1) == null ? null : m.group(1).substring(1);
			String packaging = m.group(2);
			return new Gav(groupId, artifactId, version, packaging, classifier);
		}
		return null;
	}

	public boolean contains(Gav gav) {
		String url = url(gav);
		try {
			new URL(url).openConnection().getInputStream();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void download(Gav gav, File file) {
		try {
			stream(read(gav), new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			throw new MavenException("Unable to download gav to file: " + gav + " to " + file.getAbsolutePath(), e);
		}
	}

	public InputStream read(Gav gav) {
		String url = url(gav);
		try {
			return new URL(url).openConnection().getInputStream();
		} catch (Exception e) {
			throw new MavenException("Unable to open connection to: " + url, e);
		}
	}

	public String getBaseUrl() {
		return baseurl;
	}

	public String getRepoId() {
		return repoId;
	}
	
	public String url(Gav gav) {
		return baseurl + "/" + 
				gav.getGroupId().replaceAll("\\.", "/") + "/" + 
				gav.getArtifactId() + "/" + 
				gav.getVersion() + "/" + 
				gav.getFilename();
	}
	
	public List<String> directories(String url) {
		List<String> dirs = new ArrayList<String>();
		for (String link : links(url)) {
			if (link.endsWith("/") && !link.equals("./") && !link.equals("../")) {
				dirs.add(link.substring(0, link.length() - 1));
			}
		}
		return dirs;
	}
	
	public List<String> files(String url) {
		List<String> files = new ArrayList<String>();
		for (String link : links(url)) {
			if (!link.endsWith("/")) {
				files.add(link);
			}
		}
		return files;
	}
	
	public List<String> links(String url) {
		String content = content(url);
		Pattern p = Pattern.compile("href=\"([^\"]+)\"", Pattern.MULTILINE);
		Matcher m = p.matcher(content);
		List<String> links = new ArrayList<String>();
		while (m.find()) {
			links.add(m.group(1));
		}
		return links;
	}

	public String content(Gav gav) {
		return content(url(gav));
	}
	
	public String content(String url) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			InputStream is = new URL(url).openConnection().getInputStream();
			stream(is, os);
			return os.toString();
		} catch (IOException e) {
			throw new MavenException("Unable to get content: " + url, e);
		}
	}
	
	private void stream(InputStream is, OutputStream os) {
		try {
			byte[] buffer = new byte[1024];
			int len = is.read(buffer);
			while (len != -1) {
			    os.write(buffer, 0, len);
			    len = is.read(buffer);
			}
		} catch (Exception e) {
			throw new MavenException("Unable to stream input to output", e);
		}
	}

}
