# docker-irccat

Dockerize [IRCCat](https://github.com/irccloud/irccat) with consul-template for \*Brainz git notifications

* [Docker image](https://hub.docker.com/r/metabrainz/irccat)
* [Issue tracker](https://tickets.metabrainz.org/issues/?jql=project=OTHER+AND+component=BrainzGit)

## Consul irccat JSON config

The only difference is about `channels` key which is expected to be a
semi-colon delimited list in a single string rather than an array of
strings. This is required because consul stores all KV data as strings.

Example:

```
    "channels": "#metabrainz;#musicbrainz",
```

will be rewritten into:

```
    "channels": ["#metabrainz", "#musicbrainz"],
```


## Building and deploying image

Make sure the Docker daemon is running and you are logged in Docker Hub.
Use `push.sh` script.
