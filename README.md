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

2. Make sure that JDK version in your machine is **17** or higher.
   - Check the java version: `java --version`.
   - If it is below 17,  [install](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) a newer version of java.
   - After installation, check again the version with `java --version`

## How to run
- Fill the [personInfoDTO.json](src/main/resources/DEFAULT_PERSONAL_INFO_FORM.json) file with **your** personal information.
    - Set the value of the `"Country"` matching the value of your country in the [](src/main/resources/countries.json)
    - If you are single, set the value of `"isThereFamilyMember"`: to `"2"`.
    - If you are married,  set the value of `"isThereFamilyMember"`: to `"1"`
  - There is also an [example file](src/main/resources/example_DEFAULT_PERSONAL_INFO_FORM_with_family.json) if you have family.
- Fill the [visaFormTO.json](src/main/resources/DEFAULT_VISA_APPLICATION_FORM.json) with your visa request.
  - You can also copy-paste from a [template](src/main/resources/) that matches your request.  

- Run the application in terminal by `./gradlew run`.
  - You will get a sound notification once the bot found available dates.

- To see what is happening inside the container, head to http://localhost:7900/?autoconnect=1&resize=scale&password=secret.

## License

Copyright © since 2022 Yilmaz Naci Aslan and other contributors.
_(For the full contributors' list, run `git shortlog --summary --numbered --email` from the root directory of this project.)_

This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

You should have received a copy of the GNU Affero General Public License along with this program.
If not, see <http://www.gnu.org/licenses/>.
