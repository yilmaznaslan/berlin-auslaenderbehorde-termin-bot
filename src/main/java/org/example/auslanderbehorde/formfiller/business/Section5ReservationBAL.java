package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.enums.Section5FormParameterEnum;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Business Access Layer for filling the Section 5: Reservation
 */
public class Section5ReservationBAL {

    private final Logger logger = LogManager.getLogger(Section5ReservationBAL.class);

    private RemoteWebDriver driver;

    public Section5ReservationBAL(RemoteWebDriver remoteWebDriver) {
        this.driver = remoteWebDriver;
    }

    public void sendForm() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = Section5FormParameterEnum.SEND_FORM_BUTTON.getId();
        String elementDescription = Section5FormParameterEnum.SEND_FORM_BUTTON.getName();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        logger.info("BOOKED");
        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "section5_aftersend");
        FormFillerUtils.saveScreenshot(driver, "section5_aftersend");
    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
