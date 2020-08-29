#!/bin/bash

set -e -u

MB_RNGPY_ROOT=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
cd "$MB_RNGPY_ROOT"

usage="Usage: $0 [<python command>]"

if [ $# -gt 1 ]
then
  echo Error: Too many arguments
  echo $usage
  exit 64
fi

if [ $# -eq 1 ]
then
  python_command=$1
  if ! type $python_command &>/dev/null
  then
    echo Error: $python_command command not found
    echo $usage
    exit 69
  fi
else
  if type python3 &>/dev/null
  then
    python_command=python3
  else
    echo Error: python3 command not found , please install python3 or create a virtual env with python3
    echo $usage
    exit 69
  fi
fi

if [[ ! `$python_command --version` =~ "Python 3." ]]
then
  echo Error: $python_command is not Python 3.x
  echo $usage
  exit 69
fi

if ! type trang &>/dev/null
then
  echo Error: trang command not found
  echo Please install trang package
  exit 69
fi

if ! type twine &>/dev/null
then
  echo Error: twine command not found
  echo Please install twine package
  exit 69
fi

################################################################################
echo Setting virtual environment up

virtualenv -p $python_command venv
unset python_command
set +u
source venv/bin/activate
set -u
pip install -r requirements.txt -U
python_version=`python --version`

################################################################################
echo Updating the schema

cd mmd-schema
git pull origin production
mmd_schema_version=`git describe --tags --dirty --always`
cd ..
if git diff --name-only | grep -qx mmd-schema
then
  git commit mmd-schema/ \
    -m "Update mmd-schema submodule to $mmd_schema_version"
fi

################################################################################
echo Regenerating the files

make clean
make
git commit mbrng/ musicbrainz_mmd.xsd \
  -m "Regenerate everything with mmd-schema $mmd_schema_version"

################################################################################
echo Testing

pip install -r requirements-test.txt -U
python -m pytest

################################################################################
echo Creating git tag

minor=`echo "$mmd_schema_version" \
  | sed 's/^v-\([0-9]\+\)-\([0-9]\+\)-\([0-9]\+\)/\1\2\3/'`
version="2.${minor}."`git tag --list "v-2.${minor}.*" | wc -l`
tag="v-$version"
git tag -u CE33CF04 "$tag" \
  -m "Regenerated upon MMD schema 2.0 $mmd_schema_version with $python_version."
python setup.py version

################################################################################
echo Pushing git commits and tag

git push origin master
git push origin "$tag"

################################################################################
echo Building and pushing Python package distribution

set -x

python setup.py sdist
gpg -u CE33CF04 --detach-sign -a dist/mb-rngpy-${version}.tar.gz
twine upload \
  --config-file ~/.pypirc \
  -c "Python bindings for MMD schema 2.0 $mmd_schema_version" \
  dist/mb-rngpy-${version}.tar.gz{,.asc}

################################################################################
echo Done

deactivate

# vim: autoindent expandtab filetype=sh softtabstop=2 shiftwidth=2 tabstop=2
