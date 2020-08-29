# mb-rngpy

Python bindings for the MusicBrainz RNG schema. Required by [sir](https://github.com/metabrainz/sir)

## Installation

Development files of both [`libxml2`](http://www.xmlsoft.org/) and
[`libxslt`](http://www.xmlsoft.org/XSLT/) are
required to install [`lxml`](https://lxml.de/) Python package
which is imported by `mb-rngpy`.

If you are on Ubuntu/Debian you can install these via:
```bash
sudo apt-get install libxml2-dev libxslt1-dev
```

Then you can install `mb-rngpy` from [PyPI](https://pypi.org/project/mb-rngpy/) via:
```bash
pip install mb-rngpy
```

It is supported on Python 3 only.

## Community

Join the development community of MusicBrainz at https://community.metabrainz.org/c/musicbrainz/devel

Report issues at https://tickets.metabrainz.org/secure/CreateIssue!default.jspa?pid=10022

## Updating the models

### Requirements

Please install the following programs:

* [Trang](https://github.com/relaxng/jing-trang/releases)
* [Twine](https://twine.readthedocs.io/) to upload to PyPI
* [Virtualenv](https://virtualenv.pypa.io/) to create a virtual environment

If you are on Ubuntu/Debian you can install these via:
```bash
sudo apt-get install trang twine python-virtualenv
```

Make sure you have:
* Git credentials for remote `origin`
* GPG private signing key `CE33CF04`
* PyPI credentials in `~/.pypirc`

### Updating and pushing to Git and PyPI

Finall run
```bash
./update.sh
```

It will create a virtual environment with the packages lxml and
[generateDS](http://www.davekuhlman.org/generateDS.html), update the
schema, regenerate the files, test, commit and tag changes with Git,
push commits and tags with Git, build Python package and push it to
PyPI.
