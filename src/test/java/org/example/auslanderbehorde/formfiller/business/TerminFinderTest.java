package org.example.auslanderbehorde.formfiller.business;

import org.example.auslanderbehorde.formfiller.enums.applyforavisa.EconomicActivityVisaDe;
import org.example.auslanderbehorde.formfiller.model.Section2FormInputs;
import org.example.auslanderbehorde.formfiller.model.Section4FormInputs;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Optional;

import static org.mockito.Mockito.*;

class TerminFinderTest {

    static ChromeDriver driver;
    Section2FormInputs section2FormInputs = new Section2FormInputs("163", "1", "2", serviceType, EconomicActivityVisaDe.BLUECARD);
    String firstName = "firstName";
    String lastName = "lastName";
    String email = "yilmazn.aslan@gmail.com";
    String birthdate = "12.03.1993";
    String residencePermitId = "ABCSD12333";
    Section4FormInputs section4FormInputs = new Section4FormInputs(firstName, lastName, email, birthdate, false, Optional.of(residencePermitId), serviceType);


    @BeforeAll
    static void initDriver() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
    }

    //@AfterAll
    static void quitDriver() {
        driver.quit();
    }

    @Test
    void ASSERT_THAT_everythings_is_fine_WHEN_run_is_called() {
        // GIVEN
        //driver.get(url_DE);


        TerminFinder terminFinder = spy(new TerminFinder(section4FormInputs, section2FormInputs, driver));

        doThrow(new NoSuchSessionException("")).when(terminFinder).run();

        // WHEN
        terminFinder.run();

        // THEN
       // verify(terminFinder).

    }

    @Test
    void run() {
    }
}