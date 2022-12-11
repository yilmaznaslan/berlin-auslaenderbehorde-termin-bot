FROM ubuntu:16.04


RUN apt-get update \
 && apt-get install wget unzip zip xvfb -y

RUN apt-get -f install
ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get install -y tzdata


# Install chrome broswer
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list
RUN apt-get -y update
RUN apt-get -y install google-chrome-stable


