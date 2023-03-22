package org.example.business.formhandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.Section5FormParameterEnum;
import org.example.exceptions.ElementNotFoundTimeoutException;
import org.example.exceptions.InteractionFailedException;
import org.example.utils.DriverUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.example.utils.IoUtils.savePage;

/**
 * Business Access Layer for filling the Section 5: Reservation
 */
public class Section5ReservationHandler {

    private final Logger logger = LogManager.getLogger(Section5ReservationHandler.class);

    private RemoteWebDriver driver;

    public Section5ReservationHandler(RemoteWebDriver remoteWebDriver) {
        this.driver = remoteWebDriver;
    }

    public void sendForm() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = Section5FormParameterEnum.SEND_FORM_BUTTON.getId();
        String elementDescription = Section5FormParameterEnum.SEND_FORM_BUTTON.getName();
        WebElement element = DriverUtils.getElementById(elementId, elementDescription, driver);
        DriverUtils.clickToElement(element, elementDescription);
        logger.info("BOOKED");
        savePage(driver,this.getClass().getSimpleName(), "aftersend" );

    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
