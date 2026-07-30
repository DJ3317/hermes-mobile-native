#!/bin/sh

# Gradle wrapper
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
JAVACMD="${JAVA_HOME:+$JAVA_HOME/bin/java}"
JAVACMD="${JAVACMD:-java}"
exec "$JAVACMD" -jar "$SCRIPT_DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
