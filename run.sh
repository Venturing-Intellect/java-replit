#!/bin/bash

# Make gradlew executable
chmod +x ./gradlew

# Build the application
./gradlew clean bootJar --no-daemon

# Run with explicit port binding
exec java -Dserver.port=8000 -Dserver.address=0.0.0.0 -jar build/libs/feedback-1.0.0.jar
