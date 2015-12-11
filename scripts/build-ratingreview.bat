@echo off

set JAVA_HOME=C:\ATGDEV\jdk1.7.0_72
set JRE_HOME=C:\ATGDEV\jdk1.7.0_72
set MAVEN_OPTS=-Xms256m -Xmx512m -XX:PermSize=64m -XX:MaxPermSize=256m
set BUILD_OPTIONS=-Denvironment=development -Dmaven.test.skip=false

:: Builds a release version of the ratingreview.war
echo Building ratingreview.war for user %1
if [%1]==[] goto usage

::call mvn -Pdev -e -s settings.xml %BUILD_OPTIONS% clean tomcat:redeploy
call mvn -Pdev -e -s settings.xml %BUILD_OPTIONS% clean install

::Create a overriding property file with the correct local database settings
::Call deploy.bat file for deployment of this file to the correct location
@echo datasource.ora.username=%1_liverw >> target\ratingreview.ebean.properties
@echo datasource.ora.password=atg123 >> target\ratingreview.ebean.properties
@echo datasource.ora.databaseUrl=jdbc:oracle:thin:@nlatgddbs09.otra-int.com:1521:atgdevdb >> target\ratingreview.ebean.properties

if ERRORLEVEL 1 goto failure

goto done

:usage
echo Missing parameter for user.
echo.
echo Usage: build-ratingreview.bat MXXX where you have to replace XXX with your personal M number
goto done

:failure
echo Errors during compilation, aborted!

:done