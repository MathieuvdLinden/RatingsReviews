#!/bin/sh

#release-ratingreview-server.sh <env name>
# $1 the env name
# $2 if provided equals dev we aim for local java home
# Get the environent from command line and make lowercase
ENV=$1
ENV=`echo "${ENV}" | tr '[A-Z]' '[a-z]'`

if [ "x$ENV" != "xci" ] && [ "x$ENV" != "xtest" ] && [ "x$ENV" != "xacceptance" ] && [ "x$ENV" != "xproduction" ] ; then
  echo ENV must be one of CI, TEST, ACCEPTANCE or PRODUCTION
  exit 1
fi

echo create ratingreview.ebean.properties file for environment: ${ENV}

if [ "$ENV" == "ci" ] ; then
 xusername=liverw
 xpassword=liverw
 xurl=jdbc:oracle:thin:@NLATGDDBS09:1521:ATGDEVDB
fi

if [ "$ENV" == "test" ] ; then
 xusername=liverw
 xpassword=liverw
 xurl=jdbc:oracle:thin:@NLATGDDBS09:1521:ATGTSTDB
fi

if [ "$ENV" == "acceptance" ] ; then
 xusername=liverw
 xpassword=no8ScamjnT1x
 xurl=jdbc:oracle:thin:@NLATGADBS02:1521:ATGACCDB
fi

if [ "$ENV" == "production" ] ; then
 xusername=liverw
 xpassword=r_Wpfz8TKM2#
 xurl=jdbc:oracle:thin:@//NLATGPDBS02.OTRA-INT.COM:1521/ATGJBOSS-DEDICATED.OTRA-INT.COM
fi

echo un: ${xusername}
echo pw: ${xpassword}
echo url: ${xurl}
cd ..
echo datasource.ora.username=${xusername} > target/ratingreview.ebean.properties
echo datasource.ora.password=${xpassword} >> target/ratingreview.ebean.properties
echo datasource.ora.databaseUrl=${xurl} >> target/ratingreview.ebean.properties

echo "When completed successfully, ratingreview.ebean.properties is located in /target/ratingreview.ebean.properties"
echo "Place this file within the conf directory of the tomcat server. This file is populated with the database settings for the given environment"

echo "Build process is finished"