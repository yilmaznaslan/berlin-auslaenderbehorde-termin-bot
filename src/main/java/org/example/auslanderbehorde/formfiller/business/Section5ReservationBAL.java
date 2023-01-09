package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.Section4FormInputs;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.example.auslanderbehorde.formfiller.enums.Section4FormParameterEnum.*;

/**
 * Business Access Layer for filling the Section 5: Reservation
 */
public class Section5ReservationBAL {

    private final Logger logger = LogManager.getLogger(Section5ReservationBAL.class);

    private RemoteWebDriver driver;

    public Section5ReservationBAL(Section4FormInputs formInputs, RemoteWebDriver remoteWebDriver) {
        this.driver = remoteWebDriver;
    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
