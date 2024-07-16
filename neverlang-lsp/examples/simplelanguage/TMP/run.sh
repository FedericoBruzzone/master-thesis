#!/usr/bin/env sh

# Directory of this script
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Run gradlew in this dir
"$DIR/gradlew" runLSP -q --console=plain

# Sleep for 1 second to allow the server to start
sleep 1


