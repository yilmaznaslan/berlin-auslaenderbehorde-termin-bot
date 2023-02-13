package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.intercaces.Visa;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.Section2FormInputs;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.example.auslanderbehorde.formfiller.business.Section3DateSelectionBAL.foundAppointmentCount;
import static org.example.auslanderbehorde.formfiller.business.Section3DateSelectionBAL.handledAppointmentCount;
import static org.example.auslanderbehorde.formfiller.enums.FormParameterEnum.*;

/**
 * Business Access Layer for filling the form
 */
public class Section2ServiceSelectionBAL {

    private final Logger logger = LogManager.getLogger(Section2ServiceSelectionBAL.class);
    private final String citizenshipValue;
    private final String applicantNumber;
    private final String familyStatus;
    private final Visa visa;

    private static int searchCount = 0;
    private static int succesfullyFormSentCount = 0;

    private RemoteWebDriver driver;

    public Section2ServiceSelectionBAL(Section2FormInputs section2FormInputs, RemoteWebDriver remoteWebDriver) {
        this.driver = remoteWebDriver;
        this.citizenshipValue = section2FormInputs.getCitizenshipValue();
        this.applicantNumber = section2FormInputs.getApplicationsNumber();
        this.familyStatus = section2FormInputs.getFamilyStatus();
        this.visa = section2FormInputs.getVisaEnum();
    }

    public void fillAndSendForm() throws ElementNotFoundTimeoutException, InteractionFailedException, InterruptedException {
        fillForm();
        sendForm();
    }

    private void selectCitizenshipValue() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementName = COUNTRY.getId();
        String elementDescription = COUNTRY.name();
        WebElement element = FormFillerUtils.getElementById(elementName, elementDescription, driver);
        FormFillerUtils.selectOptionByValue(element, elementDescription, citizenshipValue);
    }

    private void selectApplicantsCount() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = APPLICANT_COUNT.getId();
        String elementDescription = APPLICANT_COUNT.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.selectOptionByValue(element, elementDescription, applicantNumber);
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
        FormFillerUtils.selectOptionByValue(element, elementDescription, familyStatus);
        //Select select = new Select(element);
        //select.selectByValue(familyStatus);
        //WebElement option = select.getFirstSelectedOption();
        //String selectValue = option.getText();
        // logInfo(elementDescription, SeleniumProcessEnum.SELECTING_OPTION, "Successful", "value" + selectValue);
    }

    private void clickServiceType() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementXPath = "//*[@id=\"xi-div-30\"]/div[2]/label/p";
//        String elementXPath = "//*[@id=\"xi-div-30\"]/div[1]/label/p";
        String elementDescription = "serviceType".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXPath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        //element.click();
        //logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SeleniumProcessResultEnum.SUCCESSFUL.firstName());
    }

    private void clickVisaGroup() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementXpath = "//*[@id=\"inner-163-0-1\"]/div/div[3]/label";
        elementXpath = "//*[@id=\"inner-163-0-2\"]/div/div[1]/label";
        String elementId = "//*[@id=\"SERVICEWAHL_DE_163-0-2-3\"]";
        String elementDescription = "visaGroup".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        //element.click();
        //logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SeleniumProcessResultEnum.SUCCESSFUL.firstName());
    }

    private void clickToVisa() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = visa.getId();
        String elementDescription = visa.getDataTag0();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        //element.click();
        //logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SeleniumProcessResultEnum.SUCCESSFUL.firstName());
    }

    private void sendForm() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        String elementDescription = "clickButton".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        //element.click();
        //logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SeleniumProcessResultEnum.SUCCESSFUL.firstName());
        searchCount++;
        String msg = String.format("Completed search count: %s. SuccessfullyFormSenCount:%s, HandledAppoi.Count:%s, Found count: %s", searchCount, succesfullyFormSentCount, handledAppointmentCount, foundAppointmentCount);
        logger.info(msg);
    }

    private void fillForm() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException {
        logger.info("Starting to fill the form");
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
