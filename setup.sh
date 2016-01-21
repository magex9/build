#!/bin/sh

export SVNBASE="https://github.com/magex9/build.git"
export BUILD_WORK=target/work
if [ -d $BUILD_WORK ]; then
	rm -rf $BUILD_WORK
fi

svn checkout $SVNBASE/trunk $BUILD_WORK

#export MAVEN_OPTS="-Dmaven.repo.local=target/local-repo -DaltDeploymentRepository=release-repo::default::file:target/deploy-repo"
export MAVEN_OPTS="-Dmaven.repo.local=/Users/magex/maven/local-repo -DaltDeploymentRepository=release-repo::default::file:/Users/magex/maven/deploy-repo"

cd $BUILD_WORK
pwd

# Deploy
mvn clean deploy 

# Release it
mvn --batch-mode release:prepare release:perform

# Site
mvn site
