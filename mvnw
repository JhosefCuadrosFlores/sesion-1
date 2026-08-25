#!/bin/sh
# Apache Maven Wrapper
if [ -z "$JAVA_HOME" ]; then
  JAVACMD="java"
else
  JAVACMD="$JAVA_HOME/bin/java"
fi

find_maven_basedir() {
  basedir="$1"
  wdir="$1"
  while [ "$wdir" != '/' ]; do
    if [ -d "$wdir"/.mvn ]; then
      basedir=$wdir
      break
    fi
    wdir=$(cd "$wdir/.." && pwd)
  done
  printf '%s' "$(cd "$basedir" && pwd)"
}

BASE_DIR=$(find_maven_basedir "$(dirname "$0")")
MAVEN_PROJECTBASEDIR=${MAVEN_BASEDIR:-"$BASE_DIR"}
wrapperJarPath="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
wrapperUrl="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"

if [ ! -r "$wrapperJarPath" ]; then
  if command -v curl >/dev/null; then
    curl -s -o "$wrapperJarPath" "$wrapperUrl" -f -L
  elif command -v wget >/dev/null; then
    wget -q "$wrapperUrl" -O "$wrapperJarPath"
  fi
fi

exec "$JAVACMD" \
  $MAVEN_OPTS \
  -classpath "$wrapperJarPath" \
  "-Dmaven.multiModuleProjectDirectory=${MAVEN_PROJECTBASEDIR}" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"
