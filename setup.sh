#!/bin/sh

export WORKSPACE=/Users/magex/workspace/maven
export SVNBASE="https://github.com/magex9/build.git"
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

# Zip up required content
tar -c $WORKSPACE/local-repo | gzip > $WORKSPACE/maven-central.tar.gz

