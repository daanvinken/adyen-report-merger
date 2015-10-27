#!/bin/bash

#java -jar ReportMerger*.jar



if type -p java; then
    echo found java executable in PATH
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo found java executable in JAVA_HOME     
    _java="$JAVA_HOME/bin/java"
else
    echo "no java"
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo version "$version"
    if [[ "$version" > "1.7" ]]; then
        java -jar AdyenReportMerger*.jar
    else         
        echo version is less than 1.7, please update your Java in order to run this programm.
	echo The program wil terminate in 5 seconds
	sleep 5s
    fi
fi
