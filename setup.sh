#!/bin/sh

# The base directory where the project can be worked on
export WORKSPACE=/Users/magex/workspace/maven

# The base svn directory of the build project
export SVNBASE="https://github.com/magex9/build.git"

#######################################
# Dont edit the rest
#######################################
export CHECKOUT=$WORKSPACE/checkout
if [ -d $CHECKOUT ]; then
	rm -rf $CHECKOUT
fi

svn checkout $SVNBASE/trunk $CHECKOUT

export MAVEN_OPTS="-Dmaven.repo.local=$WORKSPACE/local-repo -DaltDeploymentRepository=release-repo::default::file:$WORKSPACE/deploy-repo"

cd $CHECKOUT
echo Working directory:
pwd

# Deploy
mvn clean deploy 

# Release it
mvn --batch-mode release:prepare release:perform

# Site
mvn site

# Useful commands
#mvn dependency:tree eclipse:eclipse findbugs:findbugs cobertura:cobertura

# Create an archetype
cd target
mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-webapp -DgroupId=ca.magex.archetype -DartifactId=mywebapp -Dversion=1.0.0-SNAPSHOT

# Zip up required content
rm -rf $WORKSPACE/local-repo/ca
tar -c $WORKSPACE/local-repo | gzip > $WORKSPACE/maven-central.tar.gz

