package org.example.formhandlers;


import org.example.enums.Section5FormParameterEnum;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.example.utils.DriverUtils.TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS;
import static org.example.utils.IoUtils.savePage;

/**
 * Business Access Layer for filling the Section 5: Reservation
 */
public class Section5ReservationHandler implements IFormHandler {

    private final Logger logger = LoggerFactory.getLogger(Section5ReservationHandler.class);

    private final RemoteWebDriver driver;

    public Section5ReservationHandler(RemoteWebDriver remoteWebDriver) {
        this.driver = remoteWebDriver;
    }

    public boolean fillAndSendForm() {
        String elementId = Section5FormParameterEnum.SEND_FORM_BUTTON.getId();
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS))
                .until(__ -> driver.findElement(By.id(elementId)));
        element.click();
        logger.info("BOOKED");
        savePage(driver, this.getClass().getSimpleName(), "aftersend");
        return true;
    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
