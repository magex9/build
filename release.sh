#!/bin/sh
echo Using svn base: $SVNBASE

echo "Checking for $SVNBASE/trunk"
svn ls $SVNBASE 2>/dev/null | grep -icq "trunk"
if [[ $? -eq 0 ]]; then
	echo "[P] Trunk exists"
else
	echo "[F] Trunk doesnt exist"
fi

echo "Checking for $SVNBASE/trunk"
svn ls $SVNBASE 2>/dev/null | grep -icq "tags"
if [[ $? -eq 0 ]]; then
        echo "[F] Tags exists"
else
        echo "[P] Tags doesnt exist"
fi

export BUILD_WORK=target/work
if [ -d $BUILD_WORK ]; then
	rm -rf $BUILD_WORK
fi

svn checkout $SVNBASE/trunk $WORKSPACE

export MAVEN_OPTS="-Dmaven.repo.local=target/local-repo -DaltDeploymentRepository=snapshot-repo::default::file:target/snapshot-repo -DaltDeploymentRepository=release-repo::default::file:target/release-repo"

# Download the clean dependencies
#mvn clean

# Install application
#mvn install

# Deploy
#mvn deploy $MAVEN_OPTS 

# Release it
#mvn release:prepare

# Deploy the release
#mvn release:perform

