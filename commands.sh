#!/bin/sh

echo Using workspace: $WORKSPACE
echo Using svn base: $SVNBASE

export CHECKOUT=$WORKSPACE/checkout
echo Deleting working directory $CHECKOUT
if [ -d $CHECKOUT ]; then
	rm -rf $CHECKOUT
fi

echo Checkout $SVNBASE/trunk to $CHECKOUT
svn checkout $SVNBASE/trunk $CHECKOUT

export MAVEN_OPTS="-Dmaven.repo.local=$WORKSPACE/local-repo -DaltDeploymentRepository=release-repo::default::file:$WORKSPACE/deploy-repo"
echo MAVEN_OPTS=$MAVEN_OTPS

cd $CHECKOUT
echo Present working directory:
pwd

# Deploy
echo Maven clean and deploy
mvn clean deploy 

# Release it
echo Maven release using default options
mvn --batch-mode release:prepare release:perform

# Site
echo Build html report about the project
mvn site

# Useful commands
echo Display a dependency tree
mvn dependency:tree 
echo Download the sources plugin
mvn dependency:sources
echo Download the javadocs
mvn dependency:resolve -Dclassifier=javadoc
echo Download the eclipse plugin
mvn eclipse:eclipse 
echo Download the findbugs plugin
mvn findbugs:findbugs 
echo Download the cobertura plugin
mvn cobertura:cobertura
echo Download the jacoco plugin
mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true

# Create an archetype
cd target
echo Creating a webapp archetype
mvn --batch-mode archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-webapp -DgroupId=ca.magex.archetype -DartifactId=mywebapp -Dversion=1.0.0-SNAPSHOT -Dpackage=ca.magex.archetype

mvn ca.magex.build:builder-maven-plugin:complete-maven-repository -Dsrc=http://central.maven.org/maven2/ -Ddest=file://$WORKSPACE/local-repo

# Zip up required content
echo Deleting builder artifacts from final repo
rm -rf $WORKSPACE/local-repo/ca

echo Zipping repository into $WORKSPACE/maven-central.tar.gz
tar -c $WORKSPACE/local-repo/* | gzip > $WORKSPACE/maven-central.tar.gz

find $WORKSPACE | grep -v '.svn' | grep -v '.DS_Store' > $CHECKOUT/find.out 
