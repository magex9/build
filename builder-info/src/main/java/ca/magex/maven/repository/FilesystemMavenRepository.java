package ca.magex.maven.repository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.magex.maven.exceptions.MavenException;
import ca.magex.maven.model.Gav;

public class FilesystemMavenRepository implements MavenRepository {

	private final File basedir;
	
	public FilesystemMavenRepository(String basedir) {
		this(new File(basedir));
	}
	
	public FilesystemMavenRepository(File basedir) {
		if (!basedir.exists() || !basedir.isDirectory())
			throw new MavenException("Base directory does not exist: " + basedir);
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
		stream(is, os);
		return new String(os.toByteArray(), Charset.defaultCharset());
	}

	public void download(Gav gav, File file) {
		if (file == null)
			throw new MavenException("Cannot download null file");
		try {
			FileOutputStream os = new FileOutputStream(file);
			InputStream is = read(gav);
			stream(is, os);
		} catch (Exception e) {
			throw new MavenException("Unable to download file: " + gav.toString() + " to " + file.getAbsolutePath(), e);
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
			throw new MavenException("Unable to read file: " + file(gav).getAbsolutePath(), e);
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
			throw new MavenException("Unable to get the root uri: " + basedir.getAbsolutePath(), e);
		}
	}

	public String getRepoId() {
		return basedir.getName();
	}

}