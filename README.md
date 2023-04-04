# Berlin Auslaenderbehorde Termin Bot

This application uses Selenium library to automate the process of getting an appointment in Berlin Ausländerbehörde.
Instead of notifying the person like other solutions, this application automatically **books** for you the requested *Termin*. For more info please visit [github pages](https://yilmaznaslan.github.io/berlin-auslaenderbehorde-termin-bot/ )

<img src="/doc/form.gif"  width="60%" height="30%">

## Prerequisites
1. In order to run selenium server you will need to install docker first. See [Get Docker](https://docs.docker.com/get-docker/) for more info. After installing the docker run the selenium server as below

```shell 
docker run -p 4444:4444 -d -t selenium/standalone-chrome:latest
```

2. Make sure that JDK version in your machine is above > 11
   - Check the java version `java --version`. 
   - If it is below 11,  [install](https://docs.oracle.com/en/java/javase/11/install/installation-jdk-macos.html#GUID-2FE451B0-9572-4E38-A1A5-568B77B146DE) a newer version of java. After installation check again the version by `java --version` 

## How to run
- Fill the [personInfoDTO.Json](src/main/resources/DEFAULT_PERSONAL_INFO_FORM.json) file with **your** personal information
  - Write the Country value in **English** as displayed on the browser
- Fill the [visaFormTO.Json](src/main/resources/DEFAULT_VISA_APPLICATION_FORM.json) with your visa request.
  - You can also copy-paste from a [template](src/main/resources/) that matches your request.  
 
- Run the application in terminal by `./gradlew run`.
  - You will get the email from *LEA* once the bot booked the termin. 
  - REMEMBER: Due to very limited number of available spots, you might need to run the script for a week !
