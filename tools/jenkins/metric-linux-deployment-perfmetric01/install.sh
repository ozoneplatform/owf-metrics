#!/bin/sh
# PURPOSE: INSTALL OWF ONTO TOMCAT INSTANCE

### VARIABLES ###
INSTALL_TOMCAT_HOME=`pwd`
HUDSON_JOB_NAME=metric
CAS_SERVER_OWF=cas-server-owf-
TOMCAT_VERSION=7.0.21

BUNDLE=metric-bundle
TOMCAT_DEPLOY=$INSTALL_TOMCAT_HOME/$BUNDLE/apache-tomcat-$TOMCAT_VERSION

CATALINA_LOG_FILE=$TOMCAT_DEPLOY/logs/catalina.out

# TODO: I don't think we use this cert stuff - charlie
#CERT_KEYSTORE_ABS_LOC=$TOMCAT_DEPLOY/certs/owftest01.goss.owfgoss.org.jks
#CERT_KEYSTORE_PASS=changeit
#CERT_TRUSTSTORE_ABS_LOC=$TOMCAT_DEPLOY/certs/owftest01.goss.owfgoss.org.jks
#CERT_TRUSTSTORE_PASS=changeit

CATALINA_PID_FILE_NAME=.tomcat-$TOMCAT_VERSION.pid
CATALINA_PID=$INSTALL_TOMCAT_HOME/$CATALINA_PID_FILE_NAME

STAGING_BUNDLE_HTTP_LOC=http://owfsvcs01.goss.owfgoss.org:8080/job/metric/lastSuccessfulBuild/artifact/staging/$BUNDLE.zip
TOMCAT_SAVE_CONFIG=$INSTALL_TOMCAT_HOME/config/apache-tomcat-$TOMCAT_VERSION

### HELPER METHODS ###
check_errs()
{
  # Function. Parameter 1 is the return code
  # Para. 2 is text to display on failure.
  if [ "${1}" -ne "0" ]; then
    echo "ERROR # ${1} : ${2}"
    # as a bonus, make our script exit with the right error code.
  # cwn!  exit ${1}
  fi
}

display_errs_and_exit(){
	echo "ERROR # 1 : ${1}"
    exit 1
}

create_log_backup(){
	new_date=$(date -u '+%F_%T')
	backup_file=catalina-${new_date}.out
cp -rf $TOMCAT_DEPLOY/logs/* $TOMCAT_SAVE_CONFIG/logs/
	echo !!! Backing up $CATALINA_LOG_FILE TO $TOMCAT_SAVE_CONFIG/logs/$backup_file...
	mv $CATALINA_LOG_FILE $TOMCAT_SAVE_CONFIG/logs/$backup_file
#	check_errs $? "Sorry, cannot backup $CATALINA_LOG_FILE $INSTALL_TOMCAT_HOME/hudson_config/log_backup/$backup_file"
	echo !!! Creating empty $CATALINA_LOG_FILE...
	touch $CATALINA_LOG_FILE
	check_errs $? "Sorry, cannot Create empty $CATALINA_LOG_FILE"
}

### MAIN SCRIPT STARTS HERE ###


# delete old bundle file
echo !! Delete old bundle
rm -rf $INSTALL_TOMCAT_HOME/$BUNDLE.zip
check_errs $? "Sorry, cannot Delete old bundle in $INSTALL_TOMCAT_HOME/$BUNDLE.zip"

# download new bundle zip
echo !! Download new bundle
wget ${STAGING_BUNDLE_HTTP_LOC}
check_errs $? "Sorry, cannot Download new bundle from ${STAGING_BUNDLE_HTTP_LOC}"

#Set Environment Vars
echo !!! Setting environment variables
#JAVA_HOME=/usr/lib/jvm/java-1.6.0-openjdk.x86_64
JAVA_OPTS="-Xms2048m -Xmx2048m -XX:PermSize=128m -XX:MaxPermSize=512m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -verbose:gc -Xloggc:/tmp/"

CATALINA_HOME=$TOMCAT_DEPLOY
BASEDIR=$TOMCAT_DEPLOY
PATH=$JAVA_HOME/bin:$PATH
LD_LIBRARY_PATH=/usr/local/apr/lib

#echo !!! Exporting JAVA_HOME=$JAVA_HOME...
#export JAVA_HOME
#check_errs $? "Sorry, cannot export JAVA_HOME=$JAVA_HOME"

echo !!! Exporting JAVA_OPTS=$JAVA_OPTS...
export JAVA_OPTS
check_errs $? "Sorry, cannot export JAVA_OPTS=$JAVA_OPTS"

echo !!! Exporting CATALINA_HOME=$CATALINA_HOME...
export CATALINA_HOME
check_errs $? "Sorry, cannot export CATALINA_HOME=$CATALINA_HOME"

echo !!! Exporting BASEDIR=$BASEDIR...
export BASEDIR
check_errs $? "Sorry, cannot export BASEDIR=$BASEDIR"

echo !!! Exporting PATH=$PATH...
export PATH
check_errs $? "Sorry, cannot export PATH=$PATH"

echo !!! Exporting LD_LIBRARY_PATH=$LD_LIBRARY_PATH...
export LD_LIBRARY_PATH
check_errs $? "Sorry, cannot export LD_LIBRARY_PATH=$LD_LIBRARY_PATH"

#TOMCAT will write its PID to the specified file
echo !!! Exporting CATALINA_PID=$CATALINA_PID
export CATALINA_PID
check_errs $? "Sorry, cannot export CATALINA_PID=$CATALINA_PID"

echo !! Stopping Tomcat...
$TOMCAT_DEPLOY/bin/shutdown.sh -force &> $TOMCAT_DEPLOY/logs/shutdown.log &
check_errs $? "Sorry, cannot Stop Tomcat, please see: $TOMCAT_DEPLOY/logs/shutdown.log"
while sleep 5s; do
        break
done

echo !!! Saving catalina.out from previous install
create_log_backup

echo !!! Copying $TOMCAT_DEPLOY/logs/* $TOMCAT_SAVE_CONFIG/logs/...
#cp -rf $TOMCAT_DEPLOY/conf/* $TOMCAT_SAVE_CONFIG/conf/
#cp -rf $TOMCAT_DEPLOY/lib/* $TOMCAT_SAVE_CONFIG/lib/
cp -rf $TOMCAT_DEPLOY/logs/* $TOMCAT_SAVE_CONFIG/logs/

echo !! Delete bundle folder from install folder
echo !! Deleting $INSTALL_TOMCAT_HOME/$BUNDLE...
rm -rf $INSTALL_TOMCAT_HOME/$BUNDLE
check_errs $? "Sorry, cannot delete $INSTALL_TOMCAT_HOME/$BUNDLE"

echo !! Extract bundle to install folder
echo !! Extracting $INSTALL_TOMCAT_HOME/$BUNDLE.zip to $BUNDLE...
unzip -d $BUNDLE $INSTALL_TOMCAT_HOME/$BUNDLE.zip
check_errs $? "Sorry, cannot extract $INSTALL_TOMCAT_HOME/$BUNDLE/* to $BUNDLE"

# copy saved config into Apache Tomcat dir
echo !! Copy saved configs to Apache Tomcat deploy folder
echo !! Copying $TOMCAT_SAVE_CONFIG/certs TO $TOMCAT_DEPLOY/
cp -rf $TOMCAT_SAVE_CONFIG/certs $TOMCAT_DEPLOY/
check_errs $? "Sorry, cannot copy $TOMCAT_SAVE_CONFIG/ TO $TOMCAT_DEPLOY/"
echo !! Copying $TOMCAT_SAVE_CONFIG/conf TO $TOMCAT_DEPLOY/
cp -rf $TOMCAT_SAVE_CONFIG/conf $TOMCAT_DEPLOY/
check_errs $? "Sorry, cannot copy $TOMCAT_SAVE_CONFIG/ TO $TOMCAT_DEPLOY/"
echo !! Copying $TOMCAT_SAVE_CONFIG/lib TO $TOMCAT_DEPLOY/
cp -rf $TOMCAT_SAVE_CONFIG/lib $TOMCAT_DEPLOY/
check_errs $? "Sorry, cannot copy $TOMCAT_SAVE_CONFIG/ TO $TOMCAT_DEPLOY/"

echo !!! Creating empty $CATALINA_LOG_FILE...
touch $CATALINA_LOG_FILE
check_errs $? "Sorry, cannot Create empty $CATALINA_LOG_FILE"

# update permissions
echo !! Updating permissions
echo !! Changing File Access Permissions RECURSIVELY on $TOMCAT_DEPLOY/bin TO 755...
chmod -R 755 $TOMCAT_DEPLOY/bin
check_errs $? "Sorry, cannot Change File Access Permissions RECURSIVELY on $TOMCAT_DEPLOY/bin TO 755"

echo !! Changing File Access Permissions RECURSIVELY on $TOMCAT_DEPLOY/lib TO 755...
chmod -R 755 $TOMCAT_DEPLOY/lib
check_errs $? "Sorry, cannot Change File Access Permissions RECURSIVELY on $TOMCAT_DEPLOY/lib TO 755"

echo !! Changing File Access Permissions RECURSIVELY on $TOMCAT_DEPLOY/conf TO 777...
chmod -R 777 $TOMCAT_DEPLOY/conf
check_errs $? "Sorry, cannot Change File Access Permissions RECURSIVELY on $TOMCAT_DEPLOY/conf TO 777"

# start Tomcat
echo !! Starting Tomcat
echo !! Changing Directory to $TOMCAT_DEPLOY/bin/...
cd $TOMCAT_DEPLOY/bin/
check_errs $? "Sorry, cannot Change Directory to $TOMCAT_DEPLOY/bin/"

CATALINA_LOG_FILE_SIZE=$(stat --printf="%s" "$CATALINA_LOG_FILE")
echo !! $CATALINA_LOG_FILE Log size is = $CATALINA_LOG_FILE_SIZE
echo !! Starting Tomcat...
touch $TOMCAT_DEPLOY/logs/startup.log
$TOMCAT_DEPLOY/bin/startup.sh &> $TOMCAT_DEPLOY/logs/startup.log &
check_errs $? "Sorry, cannot Start Tomcat, please see: $TOMCAT_DEPLOY/logs/startup.log"

echo !! Returned from starting Tomcat...

while sleep 1s; do
  if
    LOG_ERROR_MSG_1=$(fgrep -c 'SEVERE:' "$CATALINA_LOG_FILE")
    [[ LOG_ERROR_MSG_1 -ge 1 ]]
  then
    display_errs_and_exit "Sorry, error exists in Tomcat Startup relating to 'SEVERE:', Please check '$CATALINA_LOG_FILE'"
  elif
    LOG_ERROR_MSG_2=$(fgrep -c 'java.net.BindException: Address already in use' "$CATALINA_LOG_FILE")
    [[ LOG_ERROR_MSG_2 -ge 1 ]]
  then
    display_errs_and_exit "Sorry, error exists in Tomcat Startup relating to 'java.net.BindException: Address already in use', Please check '$CATALINA_LOG_FILE'"
  elif
    LOG_ERROR_MSG_3=$(fgrep -c 'Exception:' "$CATALINA_LOG_FILE")
    [[ LOG_ERROR_MSG_3 -ge 1 ]]
  then
    display_errs_and_exit "Sorry, error exists in Tomcat Startup relating to some 'Exception:' thrown, Please check '$CATALINA_LOG_FILE'"
  elif
     LOG_SUCCESS_START_MSG_1=$(fgrep -c 'Server startup in' "$CATALINA_LOG_FILE")
    [[ LOG_SUCCESS_START_MSG_1 -ge 1 ]]
  then
    echo
    echo !! Tomcat Started Successful...
    break
  else
    echo -ne .
  fi
done

echo !! Finished : Install of Metric on Tomcat ver: $TOMCAT_VERSION Successful!

