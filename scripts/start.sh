#!/bin/bash

set -eu

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd "$SCRIPT_DIR/.."

WIREMOCK_VERSION="2.23.2"
WIREMOCK_JAR="vendor/wiremock-standalone-${WIREMOCK_VERSION}.jar"

if [ ! -f "$WIREMOCK_JAR" ]; then
    mkdir -p "vendor" && cd "vendor"
    curl -O -J "http://repo1.maven.org/maven2/com/github/tomakehurst/wiremock-standalone/${WIREMOCK_VERSION}/wiremock-standalone-${WIREMOCK_VERSION}.jar"
    cd ..
fi

# Use provided port, or default to 8282
PORT="${1:-8282}"

# Start WireMock server. See http://wiremock.org/docs/running-standalone/
java -jar "${WIREMOCK_JAR}" --port "$PORT" \
                            --global-response-templating \
                            --root-dir src/main/assets/mocks
