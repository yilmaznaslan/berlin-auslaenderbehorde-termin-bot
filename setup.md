## Prerequisites
1. In order to run the application you need a running Selenium server.
    - Visit [https://www.selenium.dev](https://www.selenium.dev/documentation/webdriver/getting_started/) to install.
2. Make sure that JDK version in your machine is **17** or higher.
    - If it is below 17,  [install](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) a newer version of java.

## How to run
- Fill the [visaFormTO.json](src/main/resources/DEFAULT_VISA_APPLICATION_FORM.json) with your visa request.
    - You can also copy-paste from a [template](src/main/resources/) that matches your request.
    - If you are single, set the value of `"isThereFamilyMember"`: to `"2"`.
    - If you are married,  set the value of `"isThereFamilyMember"`: to `"1"`
  Set the value of the `"Country"` matching the value of your country in the [countries](src/main/resources/countries.json)
- Run the application in terminal by `./gradlew run`.
    - You will get a sound notification once the bot found available dates.

- To see what is happening inside the container, head to http://localhost:7900/?autoconnect=1&resize=scale&password=secret.
