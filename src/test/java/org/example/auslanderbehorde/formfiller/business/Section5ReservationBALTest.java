package org.example.auslanderbehorde.formfiller.business;

import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

class Section5ReservationBALTest {

    String path_DE = Section5ReservationBALTest.class.getClassLoader().getResource("page_after_step4_sendform_21_2023-01-16_12:45:02.html").getPath();
    String url_DE = "file:".concat(path_DE);
    static ChromeDriver driver;


    @BeforeAll
    static void initDriver() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
    }

    @AfterAll
    static void quitDriver() {
        driver.quit();
    }


    @Test
    void ASSERT_THAT_form_is_sent_WHEN_sendForm_is_called() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException {
        // GIVEN
        driver.get(url_DE);

        Section5ReservationBAL formFiller = new Section5ReservationBAL(driver);

        // WHEN
        formFiller.sendForm();

        // THEN

    }

}