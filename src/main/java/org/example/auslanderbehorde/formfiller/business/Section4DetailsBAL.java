package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.Section4FormInputs;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.logInfo;
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

    private RemoteWebDriver driver;

    public Section4DetailsBAL(Section4FormInputs formInputs, RemoteWebDriver remoteWebDriver) {
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
    }

    protected void enterFirstName() throws InterruptedException, ElementNotFoundTimeoutException{
        String elementId = FIRSTNAME.getId();
        String elementDescription = FIRSTNAME.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        element.sendKeys(firstName);
    }

    protected void enterLastName() throws InterruptedException, ElementNotFoundTimeoutException{
        String elementId = LASTNAME.getId();
        String elementDescription = LASTNAME.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        element.sendKeys(lastName);
    }

    protected void enterEmail() throws InterruptedException, ElementNotFoundTimeoutException{
        String elementId = EMAIL.getId();
        String elementDescription = EMAIL.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        element.sendKeys(emailAddress);
    }

    protected void enterBirthdate() throws InterruptedException, ElementNotFoundTimeoutException{
        String elementId = BIRTHDATE.getId();
        String elementDescription = BIRTHDATE.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        element.sendKeys(birthdate);
    }

    protected void selectResidencePermit() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = RESIDENCE_PERMIT.getId();
        String elementDescription = RESIDENCE_PERMIT.getName();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        if(isResidencePermitPresent){
            FormFillerUtils.selectOptionByValue(element, elementDescription, "1");
        }else{
            FormFillerUtils.selectOptionByValue(element, elementDescription, "0");
        }
    }

    protected void sendForm() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = "applicationForm:managedForm:proceed";
        String elementDescription = "weiter button".toUpperCase();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        //element.click();
        //logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SeleniumProcessResultEnum.SUCCESSFUL.name());
    }

    protected void fillForm() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException {
        logger.info("Starting to fill the section 4 form: Angaben");
        enterFirstName();
        enterLastName();
        enterBirthdate();
        enterEmail();
        selectResidencePermit();
    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
