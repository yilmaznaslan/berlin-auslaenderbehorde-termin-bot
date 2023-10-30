# Berlin Auslaenderbehorde Termin Bot

This application uses Selenium library to automatically detect when an appointment is available at
Ausländerbehörde Berlin for the selected visa service. Whenever an available day is found, it beeps.

<img src="/doc/form.gif"  width="60%" height="30%">

## Notes
- If you are not developer or having difficulties to setup the infrastructure, submit a query through [termin101.de](http://termin101.de.s3-website.eu-central-1.amazonaws.com/)
- You will be placed in the waiting list and get notified as soon as your booking is done.

## Prerequisites
1. In order to run the application you need a running Selenium server.
   - Visit [https://www.selenium.dev](https://www.selenium.dev/documentation/webdriver/getting_started/) to install.
2. Make sure that JDK version in your machine is **17** or higher.
   - If it is below 17,  [install](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) a newer version of java.

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

Copyright © since 2022 Yilmaz Naci Aslan

This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

You should have received a copy of the GNU Affero General Public License along with this program.
If not, see <http://www.gnu.org/licenses/>.
