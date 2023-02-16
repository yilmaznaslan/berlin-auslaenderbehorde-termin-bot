package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.PersonalInfoFormTO;
import org.example.auslanderbehorde.formfiller.model.VisaFormTO;
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
    private final String serviceType;
    private final String visaLabelValue;
    private final String visaPurposeLabelText;

    private static int searchCount = 0;
    private static int succesfullyFormSentCount = 0;

    private RemoteWebDriver driver;

    public Section2ServiceSelectionBAL(VisaFormTO visaFormTO, PersonalInfoFormTO personalInfoFormTO, RemoteWebDriver remoteWebDriver) {
        this.visaPurposeLabelText = visaFormTO.getVisaPurposeValue();
        this.driver = remoteWebDriver;
        this.citizenshipValue = personalInfoFormTO.getCitizenshipValue();
        this.applicantNumber = personalInfoFormTO.getApplicationsNumber();
        this.familyStatus = personalInfoFormTO.getFamilyStatus();
        this.serviceType = visaFormTO.getServiceType();
        this.visaLabelValue = visaFormTO.getVisaLabelValue();
    }

    public void fillAndSendForm() throws ElementNotFoundTimeoutException, InteractionFailedException, InterruptedException {
        fillForm();
        sendForm();
    }

    private void selectCitizenshipValue() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementName = COUNTRY.getId();
        String elementDescription = COUNTRY.name();
        WebElement element = FormFillerUtils.getElementById(elementName, elementDescription, driver);
        FormFillerUtils.selectOptionByVisibleText(element, elementDescription, citizenshipValue);
    }

    private void selectApplicantsCount() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = APPLICANT_COUNT.getId();
        String elementDescription = APPLICANT_COUNT.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.selectOptionByValue(element, elementDescription, applicantNumber);
    }

    private void selectFamilyStatus() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = FAMILY_STATUS.getId();
        String elementDescription = FAMILY_STATUS.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.selectOptionByValue(element, elementDescription, familyStatus);
    }

    private void clickServiceType(String serviceTypeLabelValue) throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        WebElement element = FormFillerUtils.getElementByLabelValue(serviceTypeLabelValue, serviceTypeLabelValue, driver);
        FormFillerUtils.clickToElement(element, serviceTypeLabelValue);
    }

    private void clickVisaPurpose(String visaPurposeLabelValue) throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        WebElement element = FormFillerUtils.getElementByLabelValue(visaPurposeLabelValue, visaPurposeLabelValue, driver);
        FormFillerUtils.clickToElement(element, visaPurposeLabelValue);
    }

    private void clickToVisa(String visaLabelValue) throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        WebElement element = FormFillerUtils.getElementByLabelValue(visaLabelValue, visaLabelValue, driver);
        FormFillerUtils.clickToElement(element, visaLabelValue);
    }

    private void sendForm() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        String elementDescription = "clickButton".toUpperCase();
        WebElement element = FormFillerUtils.getElementByXPath(elementXpath, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        searchCount++;
        String msg = String.format("Completed search count: %s. SuccessfullyFormSenCount:%s, HandledAppoi.Count:%s, Found count: %s", searchCount, succesfullyFormSentCount, handledAppointmentCount, foundAppointmentCount);
        logger.info(msg);
    }

    private void fillForm() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException {
        logger.info("Starting to fill the form");
        selectCitizenshipValue();
        selectApplicantsCount();
        selectFamilyStatus();
        clickServiceType(serviceType);
        clickVisaPurpose(visaPurposeLabelText);
        clickToVisa(visaLabelValue);
    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
