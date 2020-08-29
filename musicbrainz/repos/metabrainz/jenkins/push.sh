#!/bin/bash
#
# Build images for metabrainz jenkins and push it to Docker Hub
#
set -e

echo "Building Jenkins..."
docker build --pull -t metabrainz/jenkins .
echo "Done!"
echo "Pushing image to docker hub metabrainz/jenkins:latest..."
docker push metabrainz/jenkins:latest
echo "Done!"
