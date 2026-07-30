#!/bin/sh

# Gradle wrapper script
# Learn more: https://docs.gradle.org/current/userguide/gradle_wrapper.html

# Determine the project base dir
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Add default JVM options here
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD=maximum

warn () {
    echo "$*"
} >&2

die () {
    echo
    echo "$*"
    echo
    exit 1
} >&2

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
fi

# Resolve links
while [ -h "$0" ] ; do
    ls=$(ls -ld -- "$0")
    link=$(expr "$ls" : '.*-> \(.*\)$')
    if expr "$link" : '/.*' > /dev/null; then
        SCRIPT_DIR="$link"
    else
        SCRIPT_DIR=$(dirname "$SCRIPT_DIR")/"$link"
    fi
done

exec "$JAVACMD" \
    $DEFAULT_JVM_OPTS \
    $JAVA_OPTS \
    -jar "$SCRIPT_DIR/gradle/wrapper/gradle-wrapper.jar" \
    "$@"
