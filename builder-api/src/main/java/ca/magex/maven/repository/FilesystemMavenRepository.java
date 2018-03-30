package ca.magex.maven.repository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import ca.magex.maven.exceptions.GavNotFoundException;
import ca.magex.maven.model.Gav;

public class FilesystemMavenRepository implements MavenRepository {

	private final File basedir;
	
	public FilesystemMavenRepository(String basedir) {
		this(new File(basedir));
	}
	
	public FilesystemMavenRepository(File basedir) {
		if (!basedir.exists() || !basedir.isDirectory())
			throw new IllegalArgumentException("Base directory does not exist: " + basedir);
		this.basedir = basedir;
	}

	public List<String> findAllGroups() {
		return appendGroups(basedir, new ArrayList<String>());
	}

	public List<String> findRootGroups() {
		return findChildGroups("");
	}

	public List<String> findChildGroups(String group) {
		List<String> groups = new ArrayList<String>();
		File dir = new File(basedir + File.separator + 
				group.replaceAll("\\.", File.separator));
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				groups.add(file.getName());
			}
		}
		return groups;
	}

	public boolean isGroup(String group) {
		return new File(basedir + File.separator + 
				group.replaceAll("\\.", File.separator)).exists();
	}
	
	private List<String> appendGroups(File dir, List<String> groups) {
		int basedirLen = basedir.getAbsolutePath().length() + 1;
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				appendGroups(file, groups);
			} else if (file.isFile() && file.getName().endsWith(".pom")) {
				String group = file.getParentFile().getParentFile().getParent().substring(basedirLen).replaceAll("/", ".");
				if (!groups.contains(group))
					groups.add(group);
			}
		}
		return groups;
	}

	public List<String> findArtifacts(String group) {
		List<String> artifacts = new ArrayList<String>();
		File dir = new File(basedir + File.separator + 
				group.replaceAll("\\.", File.separator));
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				String artifact = file.getName();
				artifacts.add(artifact);
			}
		}
		return artifacts;
	}

	public List<String> findVersions(String group, String artifact) {
		List<String> versions = new ArrayList<String>();
		File dir = new File(basedir + File.separator + 
				group.replaceAll("\\.", File.separator) + File.separator + 
				artifact);
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				String version = file.getName();
				versions.add(version);
			}
		}
		return versions;
	}

	public Gav findPom(String group, String artifact, String version) {
		return findArtifacts(group, artifact, version)
				.stream()
				.filter(gav -> gav.getPackaging().equals("pom"))
				.findFirst().get();
	}

	public Gav findArtifact(String group, String artifact, String version) {
		return findArtifacts(group, artifact, version)
			.stream()
			.filter(gav -> !gav.getPackaging().equals("pom") && 
					gav.getClassifier() == null)
			.findFirst().get();
	}

	public List<Gav> findArtifacts(String group, String artifact, String version) {
		List<Gav> gavs = new ArrayList<Gav>();
		File dir = new File(basedir + File.separator + 
				group.replaceAll("\\.", File.separator) + File.separator + 
				artifact + File.separator + version);
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				Gav gav = parse(group, artifact, version, file.getName());
				if (gav != null && !gavs.contains(gav))
					gavs.add(gav);
			}
		}
		return gavs;
	}
	
	private Gav parse(String group, String artifact, String version, String filename) {
		if (filename.endsWith(".sha1"))
			return null;
		Pattern p = Pattern.compile(artifact + "-" + version + "(-[^\\.]+)?\\.([^\\.]+)");
		Matcher m = p.matcher(filename);
		if (m.matches()) {
			String classifier = m.group(1);
			String packaging = m.group(2);
			return new Gav(group, artifact, version, packaging, classifier);
		}
		return null;
	}

	public boolean contains(Gav gav) {
		return file(gav).exists();
	}
	
	public String content(Gav gav) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		InputStream is = read(gav);
		try {
			IOUtils.copy(is, os);
		} catch (IOException e) {
			throw new GavNotFoundException(gav, e);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
		return new String(os.toByteArray(), Charset.defaultCharset());
	}

	public void download(Gav gav, File file) {
		if (file == null)
			throw new IllegalArgumentException("Cannot download null file");
		InputStream is = null;
		OutputStream os = null;
		try {
			is = read(gav);
			os = new FileOutputStream(file);
			IOUtils.copy(is, os);
		} catch (IOException e) {
			throw new GavNotFoundException(gav, e);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}
	
	public void upload(Gav gav, InputStream is) {
		FileOutputStream os = null;
		File file = file(gav);
		if (file.exists())
			return;
		file.getParentFile().mkdirs();
		try {
			os = new FileOutputStream(file);
			IOUtils.copy(is, os);
		} catch (IOException e) {
			throw new GavNotFoundException(gav, e);
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}
	
	public File file(Gav gav) {
		StringBuilder filename = new StringBuilder();
		filename.append(basedir);
		filename.append(File.separator);
		filename.append(gav.getGroup().replaceAll("\\.", File.separator)); 
		filename.append(File.separator);
		filename.append(gav.getArtifact()); 
		filename.append(File.separator);
		filename.append(gav.getVersion());
		filename.append(File.separator);
		filename.append(gav.getFilename());
		return new File(filename.toString());
	}

	public InputStream read(Gav gav) {
		try {
			return new FileInputStream(file(gav));
		} catch (FileNotFoundException e) {
			throw new GavNotFoundException(gav, e);
		}
	}

	public String getBaseUrl() {
		return basedir.getAbsolutePath();
	}
	
	public File getFile(Gav gav) {
		return null;
	}

	public URI getURI(Gav gav) {
		try {
			return new URI(getFile(gav).getAbsolutePath());
		} catch (URISyntaxException e) {
			throw new GavNotFoundException(new Gav(gav));
		}
	}

	public String getRepo() {
		return basedir.getName();
	}

}
