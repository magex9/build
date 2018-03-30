package ca.magex.maven.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.http.client.fluent.Request;

import ca.magex.maven.exceptions.GavNotFoundException;
import ca.magex.maven.model.Gav;

public class HttpMavenRepository implements MavenRepository {

	private final String repo;
	
	private final String baseurl;
	
	public HttpMavenRepository(String repo, String baseurl) {
//		if (!content(baseurl).contains("Index of"))
//			throw new MavenException("Base url is not in the expected format: " + baseurl);
		this.repo = repo;
		this.baseurl = baseurl.replaceAll("/$", "");
	}
	
	public List<String> findAllGroups() {
		throw new NotImplementedException("Not implemented for http repostory, could take up too much bandwidth");
	}

	public List<String> findRootGroups() {
		try {
			String url = baseurl;
			return directories(url);
		} catch (IOException e) {
			throw new GavNotFoundException(null);
		}
	}

	public List<String> findChildGroups(String group) {
		try {
			String url = baseurl + "/" + group.replaceAll("\\.", "/");
			return directories(url);
		} catch (IOException e) {
			throw new GavNotFoundException(null);
		}
	}

	public boolean isGroup(String group) {
		try {
			String url = baseurl + "/" + group.replaceAll("\\.", "/");
			return !directories(url).isEmpty();
		} catch (IOException e) {
			throw new GavNotFoundException(null);
		}
	}

	public List<String> findArtifacts(String group) {
		try {
			String url = baseurl + "/" + group.replaceAll("\\.", "/");
			List<String> versions = new ArrayList<String>();
			for (String link : directories(url)) {
				if (directories(url).contains("metadata.xml"))
					versions.add(link);
			}
			return versions;
		} catch (IOException e) {
			throw new GavNotFoundException(new Gav(group, null, null));
		}
	}

	public List<String> findVersions(String group, String artifact) {
		try {
			String url = baseurl + "/" + group.replaceAll("\\.", "/") + "/" + artifact;
			return directories(url);
		} catch (IOException e) {
			throw new GavNotFoundException(new Gav(group, artifact, null));
		}
	}

	public Gav findPom(String group, String artifact, String version) {
		for (Gav gav : findArtifacts(group, artifact, version)) {
			if (gav.getPackaging().equals("pom"))
				return gav;
		}
		return null;
	}

	public Gav findArtifact(String group, String artifact, String version) {
		for (Gav gav : findArtifacts(group, artifact, version)) {
			if (!gav.getPackaging().equals("pom") && gav.getClassifier() == null)
				return gav;
		}
		return null;
	}

	public List<Gav> findArtifacts(String group, String artifact, String version) {
		try {
			String url = baseurl + "/" + group.replaceAll("\\.", "/") + "/" + artifact + "/" + version;
			List<Gav> artifacts = new ArrayList<Gav>();
			for (String link : files(url)) {
				Gav gav = parse(group, artifact, version, link);
				if (gav != null && !artifacts.contains(gav))
					artifacts.add(gav);
			}
			return artifacts;
		} catch (IOException e) {
			throw new GavNotFoundException(new Gav(group, artifact, version));
		}
	}
	
	private Gav parse(String group, String artifact, String version, String filename) {
		Pattern p = Pattern.compile(artifact + "-" + version + "(-[^\\.]+)?\\.([^\\.]+)");
		Matcher m = p.matcher(filename);
		if (m.matches()) {
			String classifier = m.group(1) == null ? null : m.group(1).substring(1);
			String packaging = m.group(2);
			return new Gav(group, artifact, version, packaging, classifier);
		}
		return null;
	}

	public boolean contains(Gav gav) {
		try {
			return Request.Get(url(gav)).execute().returnResponse().getStatusLine().getStatusCode() == 200;
		} catch (Exception e) {
			return false;
		}
	}

	public void download(Gav gav, File file) {
		try {
			IOUtils.copy(read(gav), new FileOutputStream(file));
		} catch (IOException e) {
			throw new GavNotFoundException(gav, e);
		}
	}

	public InputStream read(Gav gav) {
		try {
			return Request.Get(url(gav)).execute().returnResponse().getEntity().getContent();
		} catch (Exception e) {
			throw new GavNotFoundException(gav, e);
		}
	}

	public String getBaseUrl() {
		return baseurl;
	}

	public String getRepo() {
		return repo;
	}
	
	public String url(Gav gav) {
		return baseurl + "/" + 
				gav.getGroup().replaceAll("\\.", "/") + "/" + 
				gav.getArtifact() + "/" + 
				gav.getVersion() + "/" + 
				gav.getFilename();
	}
	
	public List<String> directories(String url) throws IOException {
		List<String> dirs = new ArrayList<String>();
		for (String link : links(url)) {
			if (link.endsWith("/") && !link.equals("./") && !link.equals("../")) {
				dirs.add(link.substring(0, link.length() - 1));
			}
		}
		return dirs;
	}
	
	public List<String> files(String url) throws IOException {
		List<String> files = new ArrayList<String>();
		for (String link : links(url)) {
			if (!link.endsWith("/")) {
				files.add(link);
			}
		}
		return files;
	}
	
	public List<String> links(String url) throws IOException {
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
		try {
			return content(url(gav));
		} catch (IOException e) {
			throw new GavNotFoundException(gav, e);
		}
	}
	
	private String content(String url) throws IOException {
		return Request.Get(url).execute().returnContent().asString();
	}

}
