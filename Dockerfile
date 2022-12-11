FROM ubuntu

RUN apt update -y

WORKDIR app
COPY . /app/

RUN apt install -y wget unzip

ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get install -y tzdata


RUN wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN dpkg -i google-chrome-stable_current_amd64.deb
RUN apt -f install -y



