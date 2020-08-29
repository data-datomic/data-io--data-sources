#!/bin/bash
#
# Build all Python images image from the currently checked out version and
# push them it to the Docker Hub.

for version in 10
do
	pushd "$(dirname "${BASH_SOURCE[0]}")/${version}/"
	echo "Building ${version}..."
	docker build -t metabrainz/node:${version} .
	echo "Pushing ${version}..."
	docker push metabrainz/node:${version}
	popd
done

echo "Done!"
