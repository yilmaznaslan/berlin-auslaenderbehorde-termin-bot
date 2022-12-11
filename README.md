# Berlin Termin Bot


## How to setup
### 1. Install Chromedriver

#### MacOs

`wget https://chromedriver.storage.googleapis.com/108.0.5359.71/chromedriver_mac64.zip`

curl https://chromedriver.storage.googleapis.com/108.0.5359.71/chromedriver_mac64.zip --output chromedriver_mac64.zip

unzip chromedriver_mac64.zip

rm chromedriver_mac64.zip

#### Linux

## How to dockerize
`docker build --tag 'berlinterminfinder:latest' .` 

``

# Setup
#COPY build/libs/berlinTerminFinder-1.0-SNAPSHOT-all.jar /opt/hello/

#EXPOSE 80
#WORKDIR /opt/hello
#CMD ["java", "-jar", "berlinTerminFinder-1.0-SNAPSHOT-all.jar"]

