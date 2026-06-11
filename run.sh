#!/bin/bash

export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH

echo "Starting CRM Application..."
echo "Using Java from: $JAVA_HOME"
echo ""

java -jar target/crm-coursework-1.0.0-SNAPSHOT.jar

echo ""
echo "Application stopped. Press any key to exit."
read -n 1
