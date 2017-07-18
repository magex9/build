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

import org.apache.maven.shared.utils.io.IOUtil;

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

	public List<String> findAllGroupIds() {
		return appendGroupIds(basedir, new ArrayList<String>());
	}

	public List<String> findRootGroups() {
		return findChildGroups("");
	}

	public List<String> findChildGroups(String group) {
		List<String> groupIds = new ArrayList<String>();
		File dir = new File(basedir + File.separator + 
				group.replaceAll("\\.", File.separator));
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				groupIds.add(file.getName());
			}
		}
		return groupIds;
	}

	public boolean isGroupId(String groupId) {
		return new File(basedir + File.separator + 
				groupId.replaceAll("\\.", File.separator)).exists();
	}
	
	private List<String> appendGroupIds(File dir, List<String> groupIds) {
		int basedirLen = basedir.getAbsolutePath().length() + 1;
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				appendGroupIds(file, groupIds);
			} else if (file.isFile() && file.getName().endsWith(".pom")) {
				String groupId = file.getParentFile().getParentFile().getParent().substring(basedirLen);
				if (!groupIds.contains(groupId))
					groupIds.add(groupId);
			}
		}
		return groupIds;
	}

	public List<String> findArtifactIds(String groupId) {
		List<String> artifactIds = new ArrayList<String>();
		File dir = new File(basedir + File.separator + 
				groupId.replaceAll("\\.", File.separator));
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				String artifactId = file.getName();
				artifactIds.add(artifactId);
			}
		}
		return artifactIds;
	}

	public List<String> findVersions(String groupId, String artifactId) {
		List<String> versions = new ArrayList<String>();
		File dir = new File(basedir + File.separator + 
				groupId.replaceAll("\\.", File.separator) + File.separator + 
				artifactId);
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				String version = file.getName();
				versions.add(version);
			}
		}
		return versions;
	}

	public Gav findPom(String groupId, String artifactId, String version) {
		return findArtifacts(groupId, artifactId, version)
				.stream()
				.filter(gav -> gav.getPackaging().equals("pom"))
				.findFirst().get();
	}

	public Gav findArtifact(String groupId, String artifactId, String version) {
		return findArtifacts(groupId, artifactId, version)
			.stream()
			.filter(gav -> !gav.getPackaging().equals("pom") && 
					gav.getClassifier() == null)
			.findFirst().get();
	}

	public List<Gav> findArtifacts(String groupId, String artifactId, String version) {
		List<Gav> gavs = new ArrayList<Gav>();
		File dir = new File(basedir + File.separator + 
				groupId.replaceAll("\\.", File.separator) + File.separator + 
				artifactId + File.separator + version);
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				Gav gav = parse(groupId, artifactId, version, file.getName());
				if (gav != null && !gavs.contains(gav))
					gavs.add(gav);
			}
		}
		return gavs;
	}
	
	private Gav parse(String groupId, String artifactId, String version, String filename) {
		if (filename.endsWith(".sha1"))
			return null;
		Pattern p = Pattern.compile(artifactId + "-" + version + "(-[^\\.]+)?\\.([^\\.]+)");
		Matcher m = p.matcher(filename);
		if (m.matches()) {
			String classifier = m.group(1);
			String packaging = m.group(2);
			return new Gav(groupId, artifactId, version, packaging, classifier);
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
			IOUtil.copy(is, os);
		} catch (IOException e) {
			throw new GavNotFoundException(gav, e);
		} finally {
			IOUtil.close(is);
			IOUtil.close(os);
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
			IOUtil.copy(is, os);
		} catch (IOException e) {
			throw new GavNotFoundException(gav, e);
		} finally {
			IOUtil.close(is);
			IOUtil.close(os);
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
			IOUtil.copy(is, os);
		} catch (IOException e) {
			throw new GavNotFoundException(gav, e);
		} finally {
			IOUtil.close(is);
			IOUtil.close(os);
		}
	}
	
	public File file(Gav gav) {
		StringBuilder filename = new StringBuilder();
		filename.append(basedir);
		filename.append(File.separator);
		filename.append(gav.getGroupId().replaceAll("\\.", File.separator)); 
		filename.append(File.separator);
		filename.append(gav.getArtifactId()); 
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

	public String getRepoId() {
		return basedir.getName();
	}

}
