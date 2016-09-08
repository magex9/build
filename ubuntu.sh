!#/bin/sh Install VM
#
# Install VirtualBox or any VM Player
# Install a ubuntu server iso on it
#   http://releases.ubuntu.com/16.04.1/ubuntu-16.04.1-server-amd64.iso
# Setup with the default settings and user/pass of dev/dev.

# Update the apt-get database
apt-get update

#Zip
apt-get install zip
$APP_ZIP=`zip --version`
if [ $(zip --version) =~ "Info-ZIP" ]; then
	echo "Zip not installed correctly"
	exit;
fi


#Unzip
#apt-get install unzip

#Vi
#apt-get install vi

#Curl
#apt-get install curl

#Wget
#apt-get install wget

#Tar
#apt-get install tar

#Git
#apt-get install git

#Svn
#Perl
#Python
#NodeJS
#Docker
#ImageMagick
#GraphViz

#DNS
#Mail
#WebMail
#LDAP
#OracleXE

#Php
#MySql
#PhpMyAdmin
#Drupal

#Java
#Maven
#Ant
#Builder

#Jboss
#ActiveMQ
#Tomcat
#TomEE
#Elastic
#Hadoop

#Nexus
#Jenkins
#Zabbix
