# Berlin Auslaenderbehorde Termin Bot

This application uses Selenium library to automatically detect when an appointment is available at
Ausländerbehörde Berlin for the selected visa service. Whenever an available day is found, it beeps.

<img src="/doc/form.gif"  width="60%" height="30%">

## Prerequisites
1. In order to run selenium server you will need to install docker first. See [Get Docker](https://docs.docker.com/get-docker/) for more info. After installing the docker run the selenium server as below

```shell 
docker run \
  -d \
  --name selenium \
  -p 4444:4444 -p 7900:7900\
  --shm-size="2g" \
  -e SE_NODE_MAX_SESSIONS=5 \
  -e SE_NODE_OVERRIDE_MAX_SESSIONS=true \
  -e SE_NODE_SESSION_TIMEOUT=120 \
  -t selenium/standalone-chrome:latest
```

**For MacOS with M1 chip**
```
docker run \
  -d \
  --name selenium \
  -p 4444:4444 -p 7900:7900\
  --shm-size="2g" \
  -e SE_NODE_MAX_SESSIONS=5 \
  -e SE_NODE_OVERRIDE_MAX_SESSIONS=true \
  -e SE_NODE_SESSION_TIMEOUT=120 \
  -t seleniarm/standalone-chromium:latest
```

2. Make sure that JDK version in your machine is above > 11
   - Check the java version `java --version`. 
   - If it is below 11,  [install](https://docs.oracle.com/en/java/javase/11/install/installation-jdk-macos.html#GUID-2FE451B0-9572-4E38-A1A5-568B77B146DE) a newer version of java. After installation check again the version by `java --version` 

## How to run
- Fill the [personInfoDTO.json](src/main/resources/DEFAULT_PERSONAL_INFO_FORM.json) file with **your** personal information.
  - Remember to write the Country value in **English**, as displayed on the browser.
  - There is also an [example file](src/main/resources/example_DEFAULT_PERSONAL_INFO_FORM_with_family.json) if you have family.
- Fill the [visaFormTO.json](src/main/resources/DEFAULT_VISA_APPLICATION_FORM.json) with your visa request.
  - You can also copy-paste from a [template](src/main/resources/) that matches your request.  
 
- Run the application in terminal by `./gradlew run`.
  - You will get the email from *LEA* once the bot booked the termin. 
  - REMEMBER: Due to very limited number of available spots, you might need to run the script for a week !

- To see what is happening inside the container, head to http://localhost:7900/?autoconnect=1&resize=scale&password=secret.
