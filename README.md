# Berlin Auslaenderbehorde Termin Bot

This application uses Selenium library to automate the process of getting an appointment in Berlin Ausländerbehörde.
Instead of notifying the person like other solutions, this application automatically **books** for you the requested *Termin*

![recording](/doc/form.gif)

## How to setup
1. In order to run selenium server you will need to install docket first. See [Get Docker](https://docs.docker.com/get-docker/) for more info.
 
2. Make sure that JDK version in your machine is above > 11
   - Check the java version `java --version`. 
   - If it is below 11,  [install](https://docs.oracle.com/en/java/javase/11/install/installation-jdk-macos.html#GUID-2FE451B0-9572-4E38-A1A5-568B77B146DE) a newer version of java. After installation check again the version by `java --version` 

3. Start SeleniumHub server 
```shell 
docker run \
  -d \
  -p 4444:4444 -p 7900:7900 -p 5900:5900 \
  -e SE_NODE_MAX_SESSIONS=5 \
  -e SE_NODE_OVERRIDE_MAX_SESSIONS=true \
  -e SE_NODE_SESSION_TIMEOUT=20 \
  -t selenium/standalone-chrome:latest
```

## How to run
- Fill the [personInfoDTO.Json](src/main/resources/PERSONAL_INFO_FORM_default.json) file with your personal information
  - Write the Country value in German as displayed on the browser
- Fill the [visaFormTO.Json](src/main/resources/APPLY_FOR_A_RESIDENCE_TITLE_default.json) with your visa request.
  - You can also copy-paste from a template that matches your request. Or copy paste values as displayed in german language
 
- Run the application in terminal by `./gradlew run`.
  - You will get the email from *LEA* once the bot booked the termin. 
  - REMEMBER: Due to very limited number of available spots, you might need to run the script for a week !