package ca.magex.maven.plugin;

import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import ca.magex.maven.exceptions.MavenException;
import ca.magex.maven.model.Gav;
import ca.magex.maven.model.MavenRepository;
import ca.magex.maven.repository.MavenRepositoryFactory;

@Mojo(name = "upload-maven-repository", requiresProject = false)
public class UploadMavenRepositoryMojo extends AbstractMavenMojo {

	@Parameter(property = "src", required = true)
	private String src;

	@Parameter(property = "dest", required = true)
	private String dest;

	public void execute() throws MojoExecutionException {
		MavenRepository sourceRepo = MavenRepositoryFactory.createMavenRepository(src);
		MavenRepository destRepo = MavenRepositoryFactory.createMavenRepository(dest);

		getLog().info("Uploading " + src + " into repository " + dest);
		for (String groupId : sourceRepo.findAllGroupIds()) {
			for (String artifactId : sourceRepo.findArtifactIds(groupId)) {
				for (String version : sourceRepo.findVersions(groupId, artifactId)) {
					Gav pom = sourceRepo.findPom(groupId, artifactId, version);
					Gav file = sourceRepo.findArtifact(groupId, artifactId, version);
					List<Gav> classifiers = sourceRepo.findArtifacts(groupId, artifactId, version);
					upload(pom, file, classifiers, sourceRepo, destRepo);
				}
			}
		}
	}

	private void upload(Gav pom, Gav file, List<Gav> classifiers, MavenRepository sourceRepo,
			MavenRepository destRepo) {
		if (!destRepo.contains(pom)) {
			getLog().info("Skipping " + pom + " because it already exists in " + dest);
			return;
		}
		
		File tmp = new File(workingDirectory, "copy-maven-repo");
		if (!tmp.exists())
			tmp.mkdirs();
		
		sourceRepo.download(pom, new File(tmp, pom.getFilename()));
		sourceRepo.download(file, new File(tmp, file.getFilename()));
		
		String url = destRepo.getBaseUrl();
		String repositoryId = destRepo.getRepoId();
		String mainFile = new File(tmp, file.getFilename()).getAbsolutePath();
		String pomFile = new File(tmp, pom.getFilename()).getAbsolutePath();
		String packaging = file == null ? "pom" : file.getPackaging();
		String classifiersNames = getClassifierNames(classifiers);
		String types = getClassifierTypes(classifiers);
		String files = getClassifierFiles(tmp, classifiers);
		
		if (getLog().isDebugEnabled()) {
			getLog().debug("Uploading artifact: " + pom);
			getLog().debug("  url: " + url);
			getLog().debug("  repositoryId: " + repositoryId);
			getLog().debug("  file: " + mainFile);
			getLog().debug("  pomFile: " + pomFile);
			getLog().debug("  packaging: " + packaging);
			getLog().debug("  classifiers: " + classifiersNames);
			getLog().debug("  types: " + types);
			getLog().debug("  files: " + files);
		}
		
		try {
			executeMojo(
				plugin(groupId("org.apache.maven.plugins"), artifactId("maven-deploy-plugin"), version("2.7")),
				goal("deploy-file"),
				configuration(element(name("url"), url),
						element(name("repositoryId"), repositoryId),
						element(name("file"), mainFile),
						element(name("pomFile"), pomFile),
						element(name("packaging"), packaging),
						element(name("classifiers"), classifiersNames),
						element(name("types"), types),
						element(name("files"), files)),
				executionEnvironment(mavenProject, session, pluginManager));
			mavenProject.getAttachedArtifacts().clear();
		} catch (MojoExecutionException e) {
			throw new MavenException("Unable to deploy file: " + pom + " to " + destRepo.getBaseUrl(), e);
		}
		
	}

	private String getClassifierFiles(File dir, List<Gav> classifiers) {
		if (classifiers.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		for (Gav classifier : classifiers) {
			sb.append(",");
			sb.append(new File(dir, classifier.getFilename()).getAbsolutePath());
		}
		return sb.substring(1);
	}

	private String getClassifierTypes(List<Gav> classifiers) {
		if (classifiers.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		for (Gav classifier : classifiers) {
			sb.append(",");
			sb.append(classifier.getPackaging());
		}
		return sb.substring(1);
	}

	private String getClassifierNames(List<Gav> classifiers) {
		if (classifiers.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		for (Gav classifier : classifiers) {
			sb.append(",");
			sb.append(classifier.getClassifier());
		}
		return sb.substring(1);
	}

}
