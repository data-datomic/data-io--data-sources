#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Argument must be file path to apache download. (e.g. hadoop/common/hadoop-3.1.1/hadoop-3.1.1.tar.gz )"
    exit 1
fi

FILE_PATH=$1
REDIRECT_BASE_URL="http://apache.mirror.digionline.de/"
ARCHIVE_BASE_URL="https://archive.apache.org/dist/"

echo "downloading $FILE_PATH from mirror"
CODE=`curl -s -o /dev/null -I -w "%{http_code}" "$REDIRECT_BASE_URL$FILE_PATH"`
if [ "$CODE" = "200" ];
then
	wget "$ARCHIVE_BASE_URL$FILE_PATH"
    exit 0;
fi

echo "$FILE_PATH not found on mirror, trying archive: $ARCHIVE_BASE_URL$FILE_PATH"
CODE=`curl -s -o /dev/null -I -w "%{http_code}" "$ARCHIVE_BASE_URL$FILE_PATH"`
if [ "$CODE" = "200" ];
then
	wget "$ARCHIVE_BASE_URL$FILE_PATH"
    exit 0;
fi

echo "failed to download $FILE_PATH"
exit 1
