#!/bin/sh
export JAVA=/usr/bin/java

export NTCLASSPATH=
export NTCLASSPATH=$NTCLASSPATH:/usr/share/nettail/commons-io-2.0.1.jar
export NTCLASSPATH=$NTCLASSPATH:/usr/share/nettail/commons-codec-1.6.jar
export NTCLASSPATH=$NTCLASSPATH:/usr/share/nettail/commons-logging-1.1.3.jar
export NTCLASSPATH=$NTCLASSPATH:/usr/share/nettail/httpclient-4.3.3.jar
export NTCLASSPATH=$NTCLASSPATH:/usr/share/nettail/httpclient-cache-4.3.3.jar
export NTCLASSPATH=$NTCLASSPATH:/usr/share/nettail/httpcore-4.3.2.jar
export NTCLASSPATH=$NTCLASSPATH:/usr/share/nettail/httpmime-4.3.3.jar
export NTCLASSPATH=$NTCLASSPATH:/usr/share/nettail/log4j-1.2.15.jar
export NTCLASSPATH=$NTCLASSPATH:/usr/share/nettail/nettail.jar

$JAVA -cp $NTCLASSPATH test.NetTail "$@"
