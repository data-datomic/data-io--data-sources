# Wikidata bot to add MBIDs

## Installation

This needs both `psycopg2` and
[pywikipediabot](https://www.mediawiki.org/wiki/PWB) (core repository).

Both can be installed with

> pip install -r requirements.txt

After installation configure pywikibot so the login works non-interactively. This repository is made to work with the `MineoBot` user on Wikidata. Its password needs to be stored in a file called `passwd` with the following content:

> ('MineoBot', '&lt;bot-password-here&gt;')

Copy `bot/settings.py.dist` to `bot/settings.py` and edit the connection string
settings. Their format is
documented
[here](http://www.postgresql.org/docs/current/static/libpq-connect.html#LIBPQ-CONNSTRING).

The `readonly_connection_string` is used to connect to a MusicBrainz database to
extract all the entities that have links to Wikipedia articles. The `readwrite`
connection string is used to connect to a database with read and write access to
keep a log of all already processed MBIDs.

If you want the bot to automatically edit URLs to redirect pages in Wikipedia to
their target pages, do

> git submodule init
>
> git submodule update

and set the `mb_user` and `mb_password` values in `bot/settings.py` to your bots
login data in MusicBrainz.

Please make sure that your bot does not violate the
[Code of Conduct](https://musicbrainz.org/doc/Code_of_Conduct/Bots) for bots in MusicBrainz

The configuration of a running bot can be reloaded by sending it a HUP signal.
