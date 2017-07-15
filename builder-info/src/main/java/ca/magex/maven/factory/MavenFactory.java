package ca.magex.maven.factory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import ca.magex.maven.model.Gav;
import ca.magex.maven.repository.MavenRepository;

public class MavenFactory {

	public static MavenProject build(File pomfile) throws IOException, XmlPullParserException {
		return build(new FileReader(pomfile));
	}
	
	public static MavenProject build(Reader reader) throws IOException, XmlPullParserException {
		return new MavenProject(new MavenXpp3Reader().read(reader));
	}

	public static MavenProject build(MavenRepository repository, Gav gav) throws IOException, XmlPullParserException {
		return build(new StringReader(repository.content(gav)));
	}

	public static void main(String[] args) throws Exception {
		MavenProject project = build(new File("/Users/magex/workspace/build/builder-info/pom.xml"));
		System.out.println(project.getName());
		System.out.println(project.getDescription());
		for (Dependency mavenDep : project.getDependencies()) {
			System.out.println(mavenDep.getGroupId());
		}
	}


}
