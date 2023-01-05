package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessEnum;
import org.example.auslanderbehorde.formfiller.enums.VisaEnum;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.FormInputs;
import org.example.auslanderbehorde.sessionfinder.model.SessionInfo;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.logInfo;
import static org.example.auslanderbehorde.formfiller.enums.FormParameterEnum.*;

/**
 * Business Access Layer for filling the Section 4: Angaben
 */
public class Section4InformationFormFillerBAL{

    private final Logger logger = LogManager.getLogger(Section4InformationFormFillerBAL.class);

    private final String nameValue;
    private final String lastnameValue;
    private final String emailAddress;
    private final VisaEnum visaEnum;

    private SessionInfo sessionInfo;
    private static int searchCount = 0;
    private static int succesfullyFormSentCount = 0;

    private RemoteWebDriver driver;

    public Section4InformationFormFillerBAL(FormInputs formInputs, SessionInfo sessionInfo, RemoteWebDriver remoteWebDriver) {
        this.sessionInfo = sessionInfo;
        this.driver = remoteWebDriver;
        this.nameValue = formInputs.getName();
        this.lastnameValue = formInputs.getLastname();
        this.emailAddress = formInputs.getEmailAddress();
        this.visaEnum = formInputs.getVisaEnum();
    }

    private void selectCitizenshipValue() throws InterruptedException, ElementNotFoundTimeoutException {
        String elementName = COUNTRY.getId();
        String elementDescription = COUNTRY.name();
        WebElement element = FormFillerUtils.getElementById(elementName, elementDescription, driver);
        Select select = new Select(element);
        select.selectByValue(nameValue);
        WebElement option = select.getFirstSelectedOption();
        String selectValue = option.getText();
        logInfo(elementDescription, SeleniumProcessEnum.SELECTING_OPTION, "Successful", "value" + selectValue);
    }

    private void selectApplicantsCount() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = APPLICANT_COUNT.getId();
        String elementDescription = APPLICANT_COUNT.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.selectOptionByValue(element, elementDescription, lastnameValue);
        //Select select = new Select(element);
        //select.selectByValue(applicantNumber);
        //WebElement option = select.getFirstSelectedOption();
        //String selectValue = option.getText();
        //logInfo(elementDescription, SeleniumProcessEnum.SELECTING_OPTION, "Successful", "value" + selectValue);
    }

    private void selectFamilyStatus() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = FAMILY_STATUS.getId();
        String elementDescription = FAMILY_STATUS.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.selectOptionByValue(element, elementDescription, emailAddress);
        //Select select = new Select(element);
        //select.selectByValue(familyStatus);
        //WebElement option = select.getFirstSelectedOption();
        //String selectValue = option.getText();
        // logInfo(elementDescription, SeleniumProcessEnum.SELECTING_OPTION, "Successful", "value" + selectValue);
    }

    private void clickServiceType() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementXPath = "//*[@id=\"xi-div-30\"]/div[1]/label/p";
        String elementDescription = "serviceType".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXPath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        //element.click();
        //logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SeleniumProcessResultEnum.SUCCESSFUL.name());
    }

    private void clickVisaGroup() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementXpath = "//*[@id=\"inner-163-0-1\"]/div/div[3]/label";
        String elementDescription = "visaGroup".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        //element.click();
        //logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SeleniumProcessResultEnum.SUCCESSFUL.name());
    }

    private void clickToVisa() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = visaEnum.getId();
        String elementDescription = visaEnum.getDataTag0();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        //element.click();
        //logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SeleniumProcessResultEnum.SUCCESSFUL.name());
    }

    private void sendForm() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        String elementDescription = "clickButton".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        //element.click();
        //logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SeleniumProcessResultEnum.SUCCESSFUL.name());
    }

    private void fillForm() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException {
        logger.info("Starting to fill the section 4 form: Angaben");
        selectCitizenshipValue();
        selectApplicantsCount();
        selectFamilyStatus();
        clickServiceType();
        clickVisaGroup();
        clickToVisa();
    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
