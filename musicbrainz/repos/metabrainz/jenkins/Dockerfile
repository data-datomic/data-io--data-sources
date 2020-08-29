FROM jenkins/jenkins:lts-alpine

# Parent container switches to "jenkins" user, so we need to revert that.
# Don't switch back because Docker will stop working.
USER root

RUN apk add --no-cache \
            ca-certificates \
            curl \
            gcc \
            libffi-dev \
            m4 \
            make \
            musl-dev \
            openssl \
            openssl-dev \
            py2-pip \
            py3-pip \
            python2 \
            python2-dev \
            python3 \
            python3-dev \
            rsync \
            yarn \
    && rm -rf /var/cache/apk/*

# Installing Docker and Compose...
# See https://hub.docker.com/_/docker/ for updates.
ENV DOCKER_BUCKET https://download.docker.com/linux/static/stable/x86_64
ENV DOCKER_VERSION 19.03.5
ENV DOCKER_SHA256 50cdf38749642ec43d6ac50f4a3f1f7f6ac688e8d8b4e1c5b7be06e1a82f06e9
RUN set -x \
    && curl -fSL "${DOCKER_BUCKET}/docker-${DOCKER_VERSION}.tgz" -o docker.tgz \
    && echo "${DOCKER_SHA256} *docker.tgz" | sha256sum -c - \
    && tar -xzvf docker.tgz \
    && mv docker/* /usr/local/bin/ \
    && rmdir docker \
    && rm docker.tgz \
    && docker -v
RUN pip3 --no-cache-dir install docker-compose

# Installing zopfli (for StaticBrainz jobs)
RUN cd /tmp \
    && git clone https://github.com/google/zopfli \
    && cd zopfli \
    && git checkout zopfli-1.0.3 \
    && make zopfli \
    && install -m 755 zopfli /usr/local/bin/ \
    && cd - \
    && rm -rf /tmp/zopfli
