#!/bin/sh

# Download the clean dependencies
mvn clean

# Install application
mvn install

# Release it
mvn release:prepare

# Deploy the release
mvn release:perform

