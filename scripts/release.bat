@echo off

set JAVA_HOME=C:\ATGDEV\jdk1.7.0_72
set JRE_HOME=C:\ATGDEV\jdk1.7.0_72
set MAVEN_OPTS=-Xms256m -Xmx512m -XX:PermSize=64m -XX:MaxPermSize=256m

:: Builds a release version of the ratingreview.war
echo Building ratingreview.war for environment %1
if [%1]==[CI] goto rel_ci
if [%1]==[TEST] goto rel_test
if [%1]==[ACCEPTANCE] goto rel_acc
if [%1]==[PRODUCTION] goto rel_prod

goto usage

:rel_ci
set liverw.dbserver.username=liverw
set liverw.dbserver.password=liverw
set liverw.dbserver.url=jdbc:oracle:thin:@NLATGDDBS09:1521:ATGDEVDB
goto build

:rel_test
set liverw.dbserver.username=liverw
set liverw.dbserver.password=liverw
set liverw.dbserver.url=jdbc:oracle:thin:@NLATGDDBS09:1521:ATGTSTDB
goto build

:rel_acc
set liverw.dbserver.username=liverw
set liverw.dbserver.password=no8ScamjnT1x
set liverw.dbserver.url=jdbc:oracle:thin:@NLATGADBS02:1521:ATGACCDB
goto build

:rel_prod
set liverw.dbserver.username=liverw
set liverw.dbserver.password=r_Wpfz8TKM2#
set liverw.dbserver.url=jdbc:oracle:thin:@//NLATGPDBS02.OTRA-INT.COM:1521/ATGJBOSS-DEDICATED.OTRA-INT.COM
goto build

:build
call mvn -e -s settings.xml clean install

echo.
echo When completed successfully, ratingreview.war is located in \RatingsAndReviews\target\ratingreview.war
echo In this war file the ebean.properties file is populated with the database settings for the given environment

goto done

:usage
echo Missing parameter for environment.
echo.
echo Usage: release.bat CI or TEST or ACCEPTANCE or PRODUCTION
goto done


:error
echo Environment %1 not supported yet

:done