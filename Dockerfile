# on ubuntu
FROM azul/zulu-openjdk:11

RUN apt-get update && apt-get install -y \
gnupg2 \
wget \
less \
&& rm -rf /var/lib/apt/lists/*

#######################
# Google Chrome

# Adding trusting keys to apt for repositories
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub |
apt-key add -
# Adding Google Chrome to the repositories
RUN sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/
stable main" >> /etc/apt/sources.list.d/google-chrome.list'
# Updating apt to see and install Google Chrome
RUN apt-get -y update
# Magic happens
RUN apt-get install -y google-chrome-stable
# Installing Unzip
RUN apt-get install -yqq unzip


#######################
# Google Chrome Driver - now i have driver in my java app (i need both
java app version on docker and non-docker)

# Download the Chrome Driver
#RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub |
apt-key add -
#RUN sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/
stable main" >> /etc/apt/sources.list.d/google-chrome.list'
#RUN apt-get -y update
#RUN apt-get install -y google-chrome-stable
# install chromedriver
#RUN apt-get install -yqq unzip
#RUN wget -O /tmp/chromedriver.zip
http://chromedriver.storage.googleapis.com/`curl -sS
 chromedriver.storage.googleapis.com/LATEST_RELEASE`/chromedriver_linux64.zip
#RUN unzip /tmp/chromedriver.zip chromedriver -d /usr/local/bin/

#######################

RUN apt-get update


# Set display port as an environment variable
ENV DISPLAY=:99


ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} bot-app.jar
ENTRYPOINT ["java","-jar","/bot-app.jar"]