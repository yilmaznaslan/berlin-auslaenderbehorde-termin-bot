package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.Section4FormInputs;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Optional;

import static org.example.auslanderbehorde.formfiller.enums.Section4FormParameterEnum.*;

/**
 * Business Access Layer for filling the Section 4: Angaben
 */
public class Section4DetailsBAL {

    private final Logger logger = LogManager.getLogger(Section4DetailsBAL.class);
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final String birthdate;
    private final boolean isResidencePermitPresent;
    private final Optional<String> residencePermitId;

    private RemoteWebDriver driver;

    public Section4DetailsBAL(Section4FormInputs formInputs, RemoteWebDriver remoteWebDriver) {
        this.residencePermitId = formInputs.getResidencePermitId();
        this.driver = remoteWebDriver;
        this.firstName = formInputs.getName();
        this.lastName = formInputs.getLastname();
        this.emailAddress = formInputs.getEmailAddress();
        this.birthdate = formInputs.getBirthdate();
        this.isResidencePermitPresent = formInputs.isResidencePermitPresent();
    }

    public void fillAndSendForm() throws ElementNotFoundTimeoutException, InteractionFailedException, InterruptedException {
        fillForm();
        sendForm();
        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "section4_aftersend");
        FormFillerUtils.saveScreenshot(driver, "section4_aftersend");
    }

    protected void enterFirstName() throws InterruptedException, ElementNotFoundTimeoutException {
        String elementId = FIRSTNAME.getId();
        String elementDescription = FIRSTNAME.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        element.sendKeys(firstName);
    }

    protected void enterLastName() throws InterruptedException, ElementNotFoundTimeoutException {
        String elementId = LASTNAME.getId();
        String elementDescription = LASTNAME.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        element.sendKeys(lastName);
    }

    protected void enterEmail() throws InterruptedException, ElementNotFoundTimeoutException {
        String elementId = EMAIL.getId();
        String elementDescription = EMAIL.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        element.sendKeys(emailAddress);
    }

    protected void enterBirthdate() throws InterruptedException, ElementNotFoundTimeoutException {
        String elementId = BIRTHDATE.getId();
        String elementDescription = BIRTHDATE.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        element.sendKeys(birthdate);
    }

    protected void enterResidencePermitId() throws ElementNotFoundTimeoutException, InterruptedException {
        String elementId = RESIDENCE_PERMIT_NUMBER.getId();
        String elementDescription = RESIDENCE_PERMIT_NUMBER.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        element.sendKeys(residencePermitId.orElse("ABCSD123"));
    }

    protected void selectResidencePermit() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = RESIDENCE_PERMIT.getId();
        String elementDescription = RESIDENCE_PERMIT.getName();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        if (isResidencePermitPresent) {
            FormFillerUtils.selectOptionByValue(element, elementDescription, "1");
            enterResidencePermitId();
        } else {
            FormFillerUtils.selectOptionByValue(element, elementDescription, "0");
        }
    }

    protected void sendForm() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = "applicationForm:managedForm:proceed";
        String elementDescription = "weiter button".toUpperCase();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }

    protected void fillForm() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException {
        logger.info("Starting to fill the section 4 form: Angaben");
        enterFirstName();
        enterLastName();
        enterBirthdate();
        enterEmail();
        selectResidencePermit();
        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "step4_fillform_");
        FormFillerUtils.saveScreenshot(driver, "step4_fillform_");
    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
