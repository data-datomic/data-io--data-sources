#!/bin/bash

docker build -t rsyncd .
docker rm -f rsyncd; docker run -d -p 873:873 -v /tmp:/data -e "BWLIMIT=100" --name=rsyncd rsyncd
