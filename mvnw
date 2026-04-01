#!/bin/sh

MAVEN_PROJECTBASEDIR="$(cd "$(dirname "$0")" && pwd)"
WRAPPER_DIR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper"
WRAPPER_JAR="$WRAPPER_DIR/maven-wrapper.jar"
WRAPPER_PROPERTIES="$WRAPPER_DIR/maven-wrapper.properties"

if [ ! -f "$WRAPPER_JAR" ]; then
  if [ ! -f "$WRAPPER_PROPERTIES" ]; then
    echo "Missing $WRAPPER_PROPERTIES"
    exit 1
  fi
  WRAPPER_URL="$(grep '^wrapperUrl=' "$WRAPPER_PROPERTIES" | sed 's/wrapperUrl=//')"
  if [ -z "$WRAPPER_URL" ]; then
    WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
  fi
  mkdir -p "$WRAPPER_DIR"
  echo "Downloading Maven Wrapper jar..."
  curl -fsSL "$WRAPPER_URL" -o "$WRAPPER_JAR"
fi

exec java ${MAVEN_OPTS} -classpath "$WRAPPER_JAR" "-Dmaven.multiModuleProjectDirectory=$MAVEN_PROJECTBASEDIR" org.apache.maven.wrapper.MavenWrapperMain "$@"

