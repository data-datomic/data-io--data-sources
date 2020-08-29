FROM metabrainz/python:3.6-1

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
                       build-essential \
                       git \
                       libpq-dev \
                       libffi-dev \
                       libssl-dev \
                       sudo \
    && rm -rf /var/lib/apt/lists/*

RUN useradd --create-home --shell /bin/bash caa

WORKDIR /home/caa/redirect
RUN chown caa:caa /home/caa/redirect

# Python dependencies
RUN sudo -E -H -u caa pip install --user -U cffi
COPY requirements.txt ./
RUN sudo -E -H -u caa pip install --user -r requirements.txt

# Node dependencies
RUN curl -sL https://deb.nodesource.com/setup_6.x | bash -
RUN apt-get install -y nodejs
COPY package.json package-lock.json ./
RUN sudo -E -H -u caa npm install

COPY . ./
RUN sudo -E -H -u caa ./node_modules/.bin/lessc \
        static/css/main.less > static/css/main.css
RUN chown -R caa:caa ./

############
# Services #
############

COPY ./docker/prod/redirect.service /etc/service/redirect/run
COPY ./docker/prod/uwsgi.ini /etc/uwsgi/

RUN chmod 755 /etc/service/redirect/run

# Configuration
COPY ./docker/prod/consul-template-redirect.conf /etc/

EXPOSE 8080
