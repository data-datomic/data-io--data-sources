# Jenkins for MetaBrainz

![](https://cloud.githubusercontent.com/assets/460525/20229468/df585590-a856-11e6-8cb4-c7856e67d168.png)

## Running the container

```bash
docker volume create --driver local --name jenkins-data
docker pull metabrainz/jenkins
docker run \
    --publish 80:8080 \
    --volume jenkins-data:/var/jenkins_home \
    --volume /var/run/docker.sock:/var/run/docker.sock \
    metabrainz/jenkins
```
