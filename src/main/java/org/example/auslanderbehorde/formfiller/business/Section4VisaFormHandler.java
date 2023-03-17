package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessEnum;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessResultEnum;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.model.PersonalInfoFormTO;
import org.example.auslanderbehorde.formfiller.model.VisaFormTO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.stream.Collectors;

import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.*;
import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.logWarn;
import static org.example.auslanderbehorde.formfiller.enums.FormParameterEnum.COUNTRY;
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
        String elementDescription = FIRSTNAME.name();
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                WebElement element = driver.findElements(By.tagName("input")).stream().filter(element1 -> element1.getAttribute("name").equals("antragsteller_vname")).collect(Collectors.toList()).get(0);
                element.sendKeys(firstName);
                Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
                break;
            } catch (Exception e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.firstName(), SeleniumProcessResultEnum.FAILED.firstName(), "");
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new ElementNotFoundTimeoutException(elementDescription);
        }
    }

    protected void enterLastName() throws InterruptedException, ElementNotFoundTimeoutException {
        String elementDescription = LASTNAME.name();
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                WebElement element = driver.findElements(By.tagName("input")).stream().filter(element1 -> element1.getAttribute("name").equals("antragsteller_nname")).collect(Collectors.toList()).get(0);
                element.sendKeys(lastName);
                Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
                break;
            } catch (Exception e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.firstName(), SeleniumProcessResultEnum.FAILED.firstName(), "");
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new ElementNotFoundTimeoutException(elementDescription);
        }
    }

    protected void enterEmail() throws InterruptedException, ElementNotFoundTimeoutException {
        String elementDescription = EMAIL.name();
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                WebElement element = driver.findElements(By.tagName("input")).stream().filter(element1 -> element1.getAttribute("name").equals("antragsteller_email")).collect(Collectors.toList()).get(0);
                element.sendKeys(emailAddress);
                Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
                break;
            } catch (Exception e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.firstName(), SeleniumProcessResultEnum.FAILED.firstName(), "");
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new ElementNotFoundTimeoutException(elementDescription);
        }

    }

    protected void enterBirthdate() throws InterruptedException, ElementNotFoundTimeoutException {
        String elementDescription = BIRTHDATE.name();
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                WebElement element = driver.findElements(By.tagName("input")).stream().filter(element1 -> element1.getAttribute("name").equals("antragsteller_gebDatum")).collect(Collectors.toList()).get(0);
                element.sendKeys(birthdate);
                Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
                break;
            } catch (Exception e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.firstName(), SeleniumProcessResultEnum.FAILED.firstName(), "");
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new ElementNotFoundTimeoutException(elementDescription);
        }
    }

    protected void enterResidencePermitId(String elementId) throws ElementNotFoundTimeoutException, InterruptedException {
        logger.info("Entering the residence permit id");
        String elementDescription = RESIDENCE_PERMIT_NUMBER.name();
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                WebElement element = driver.findElements(By.tagName("input")).stream().filter(element1 -> element1.getAttribute("name").equals("antragsteller_nrAufenthaltserlaubnis2")).collect(Collectors.toList()).get(0);
                element.sendKeys(residencePermitId);
                Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
                break;
            } catch (Exception e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.firstName(), SeleniumProcessResultEnum.FAILED.firstName(), "");
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new ElementNotFoundTimeoutException(elementDescription);
        }
    }

    protected void selectResidencePermit() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementDescription = RESIDENCE_PERMIT.getName();
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                WebElement element = driver.findElements(By.tagName("select")).stream().filter(element1 -> element1.getAttribute("name").equals("sel_aufenthaltserlaubnis")).collect(Collectors.toList()).get(0);
                if (isResidencePermitPresent) {
                    FormFillerUtils.selectOptionByValue(element, elementDescription, "1");
                } else {
                    FormFillerUtils.selectOptionByValue(element, elementDescription, "0");
                }
                Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
                break;
            } catch (Exception e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.firstName(), SeleniumProcessResultEnum.FAILED.firstName(), "");
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new ElementNotFoundTimeoutException(elementDescription);
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
