This is a repository with configuration files for [Locust](http://locust.io/) load testing tool.

## Installation

See http://docs.locust.io/en/latest/installation.html.

## Usage

Go to the project directory and start the server:

    $ locust --host=<location>
    
For example, to start load testing production CritiqueBrainz server:

    $ cd critiquebrainz
    $ locust --host=https://critiquebrainz.org
