package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.PersonalInfoFormTO;
import org.example.auslanderbehorde.formfiller.model.VisaFormTO;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.example.auslanderbehorde.formfiller.enums.Section4FormParameterEnum.*;

/**
 * Business Access Layer for filling the Section 4: Angaben
 */
public class Section4VisaFormHandler {

    private final Logger logger = LogManager.getLogger(Section4VisaFormHandler.class);
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final String birthdate;
    private final Boolean isResidencePermitPresent;
    private final String residencePermitId;
    private final String serviceType;
    private final RemoteWebDriver driver;

    public Section4VisaFormHandler(PersonalInfoFormTO personalInfoFormTO, VisaFormTO visaFormTO, RemoteWebDriver remoteWebDriver) {
        this.serviceType = visaFormTO.getServiceType();
        this.residencePermitId = visaFormTO.getResidencePermitId();
        this.driver = remoteWebDriver;
        this.firstName = personalInfoFormTO.getFirstName();
        this.lastName = personalInfoFormTO.getLastName();
        this.emailAddress = personalInfoFormTO.getEmailAddress();
        this.birthdate = personalInfoFormTO.getBirthdate();
        this.isResidencePermitPresent = visaFormTO.getResidencePermitPresent();
    }

    public void fillAndSendForm() throws ElementNotFoundTimeoutException, InteractionFailedException, InterruptedException {
        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "Section4VisaFormHandler", "");
        FormFillerUtils.saveScreenshot(driver, this.getClass().getSimpleName(), "");

        fillForm();
        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), this.getClass().getSimpleName(),  "after_filling");
        FormFillerUtils.saveScreenshot(driver, this.getClass().getSimpleName() , "after_filling");

        sendForm();

        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), this.getClass().getSimpleName() , "after_send");
        FormFillerUtils.saveScreenshot(driver, this.getClass().getSimpleName() , "after_send");
    }

    protected void fillForm() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException {
        logger.info("Starting to fill the section 4 form: Angaben, for servicetype: {}.", serviceType);
        enterFirstName();
        enterLastName();
        enterBirthdate();
        enterEmail();

        if (serviceType.equals("Aufenthaltstitel - verlängern")) {
            enterResidencePermitId(RESIDENCE_PERMIT_NUMBER_EXTENSION.getId());
        }

        if (serviceType.equals("Aufenthaltstitel - beantragen")) {
            selectResidencePermit();
            if (isResidencePermitPresent) {
                enterResidencePermitId(RESIDENCE_PERMIT_NUMBER.getId());
            }
        }
        logger.error("This shouldn't happen");
        throw new RuntimeException("Failed to enter residence permit id");

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

    protected void enterResidencePermitId(String elementId) throws ElementNotFoundTimeoutException, InterruptedException {
        logger.info("Entering the residence permit id");
        String elementDescription = RESIDENCE_PERMIT_NUMBER.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        element.sendKeys(residencePermitId);
    }

    protected void selectResidencePermit() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = RESIDENCE_PERMIT.getId();
        String elementDescription = RESIDENCE_PERMIT.getName();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        if (isResidencePermitPresent) {
            FormFillerUtils.selectOptionByValue(element, elementDescription, "1");
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

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
