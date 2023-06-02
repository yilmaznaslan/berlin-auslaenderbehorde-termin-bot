package org.example;

import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;
import org.example.utils.IoUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.UUID;

import static org.example.utils.DriverUtils.getChromeOptions;

public class BaseTestSetup {

    public static UUID id = UUID.randomUUID();
    public static ChromeDriver driver;
    public static String citizenshipValue = "Türkei";
    public static String firstName = "firstName";
    public static String lastName = "lastName";
    public static String email = "yilmazn.aslan@gmail.com";
    public static String birthdate = "12.03.1993";
    public static String familyStatus = "2";
    public static String applicationsNumber = "1";
    public static String residencePermitId = "123JSE421";
    public static VisaFormTO visaForm_apply_for_bluecard_without_residencePermit = new VisaFormTO(
            false,
            null,
            "Apply for a residence title",
            "Economic activity",
            "EU Blue Card / Blaue Karte EU (sect. 18b para. 2)");
    public PersonalInfoFormTO personalInfoFormTO = new PersonalInfoFormTO(citizenshipValue, null, applicationsNumber, familyStatus, firstName, lastName, email, birthdate);

    @BeforeAll
    static void initDriver() {
        Config.getPropValues();
        ChromeOptions chromeOptions = getChromeOptions();
        chromeOptions.addArguments("--headless");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
        IoUtils.isS3Enabled = false;
        IoUtils.isLocalSaveEnabled = false;
        IoUtils.isCloudwatchEnabled = false;
    }

    @AfterAll
    static void quitDriver() {
        driver.quit();
    }


}