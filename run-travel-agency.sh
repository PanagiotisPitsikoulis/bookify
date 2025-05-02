#!/bin/bash

export JAVA_HOME=$(/usr/libexec/java_home -v 24)

cd "$(dirname "$0")"

mvn clean javafx:run 