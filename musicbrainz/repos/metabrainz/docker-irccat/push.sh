#!/bin/bash
#
# Build image from for the specified version of IRCCat
# and push it to the Docker Hub, tagged with version.
#
# Usage:
#   $ ./push.sh [version]

set -e

version=${1:-v0.4.1}

cd `dirname "${BASH_SOURCE[0]}"`
docker build \
  --build-arg IRCCAT_VERSION=${version} \
  --build-arg BUILD_DATE=`date -u +"%Y-%m-%dT%H:%M:%SZ"` \
  --build-arg VCS_REF=`git describe --always --broken --dirty` \
  --tag metabrainz/irccat:${version} .
docker push metabrainz/irccat:${version}
