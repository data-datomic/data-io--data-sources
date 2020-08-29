ARG CT_VERSION=0.19.4
FROM metabrainz/consul-template-base:v${CT_VERSION}

ARG IRCCAT_VERSION

ARG CT_VERSION

ARG BUILD_DATE
ARG VCS_REF

LABEL org.label-schema.build-date=${BUILD_DATE} \
      org.label-schema.vcs-url="https://github.com/metabrainz/docker-irccat.git" \
      org.label-schema.vcs-ref=${VCS_REF} \
      org.label-schema.schema-version="1.0.0-rc1" \
      org.label-schema.vendor="MetaBrainz Foundation" \
      org.metabrainz.based-on-image="metabrainz/consul-template-base:${CT_VERSION}" \
      org.metabrainz.irccat.version=${IRCCAT_VERSION}

RUN curl -L -o /usr/local/bin/irccat -S https://github.com/irccloud/irccat/releases/download/${IRCCAT_VERSION}/linux_amd64_irccat && \
    chmod 755 /usr/local/bin/irccat

RUN apt-get update && \
    apt-get install -y --no-install-recommends ca-certificates && \
    rm -rf /var/lib/apt/lists/*

COPY docker/consul-template-irccat.conf /etc/
COPY docker/irccat.json.ctmpl /etc/
COPY docker/irccat.service /etc/service/irccat/run
