@echo on

set HUDSON_HOME=c:\hudson
set HUDSON_JOB=metric-windows-deployment
set INSTALL_HOME=c:\ozone_metric
set WEBSERVER=apache-tomcat-7.0.21
set WEBSERVER_SERVICE_NAME=Tomcat7Metric

cd %HUDSON_HOME%\workspace\%HUDSON_JOB%

echo !!Stop %WEBSERVER_SERVICE_NAME%
net stop %WEBSERVER_SERVICE_NAME%

echo !!Downloading last successful bundle
wget --output-document=metric-bundle.zip http://owfsvcs01:8080/job/metric/lastSuccessfulBuild/artifact/staging/metric-bundle.zip

mkdir staging
cd staging

echo !!Unzipping last successful bundle
jar xvf ..\metric-bundle.zip

echo !!copy out last config and logs
xcopy %INSTALL_HOME%\%WEBSERVER%\conf\* %HUDSON_HOME%\configMetric\%WEBSERVER%\conf /s /i /F /y /E 
xcopy %INSTALL_HOME%\%WEBSERVER%\lib\* %HUDSON_HOME%\configMetric\%WEBSERVER%\lib /s /i /F /y /E 
xcopy %INSTALL_HOME%\%WEBSERVER%\logs\* %HUDSON_HOME%\configMetric\%WEBSERVER%\logs /s /i /F /y /E 

echo !!clean install folder
rmdir /s /q %INSTALL_HOME%
mkdir %INSTALL_HOME%

echo !!copy extracted bundle to install location
xcopy %HUDSON_HOME%\workspace\metric-windows-deployment\staging\* %INSTALL_HOME% /s /i /F /y /E

echo !!Deleting unzipped files
cd ..
rmdir /s /q staging

echo !!copy saved config files to install location
xcopy %HUDSON_HOME%\configMetric\* %INSTALL_HOME% /s /i /F /y /E

echo !!Start %WEBSERVER_SERVICE_NAME%
net start %WEBSERVER_SERVICE_NAME%

cd %HUDSON_HOME%