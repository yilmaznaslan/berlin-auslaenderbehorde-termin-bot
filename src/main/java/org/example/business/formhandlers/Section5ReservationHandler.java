package org.example.business.formhandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.Section5FormParameterEnum;
import org.example.exceptions.ElementNotFoundTimeoutException;
import org.example.exceptions.InteractionFailedException;
import org.example.utils.FormFillerUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

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
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        logger.info("BOOKED");
        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), this.getClass().getSimpleName(), "aftersend");
        FormFillerUtils.saveScreenshot(driver, this.getClass().getSimpleName(), "aftersend");
    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
