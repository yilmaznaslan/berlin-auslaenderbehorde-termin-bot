package org.example.formhandlers;


import org.example.enums.Section4FormParameterEnum;
import org.example.enums.SeleniumProcessEnum;
import org.example.enums.SeleniumProcessResultEnum;
import org.example.exceptions.ElementNotFoundTimeoutException;
import org.example.exceptions.InteractionFailedException;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;
import org.example.utils.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import static org.example.enums.Section4FormParameterEnum.RESIDENCE_PERMIT_NUMBER;
import static org.example.enums.Section4FormParameterEnum.RESIDENCE_PERMIT_NUMBER_EXTENSION;
import static org.example.utils.DriverUtils.SLEEP_DURATION_IN_MILLISECONDS;
import static org.example.utils.DriverUtils.TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS;
import static org.example.utils.IoUtils.savePage;
import static org.example.utils.LogUtils.logWarn;

/**
 * Business Access Layer for filling the Section 4: Angaben
 */
public class Section4VisaFormHandler {

    private final Logger logger = LoggerFactory.getLogger(Section4VisaFormHandler.class);
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
        savePage(driver,this.getClass().getSimpleName(), "" );
        fillForm();

        savePage(driver,this.getClass().getSimpleName(), "after_filling" );
        sendForm();
        savePage(driver,this.getClass().getSimpleName(), "after_send" );
    }

    protected void fillForm() throws ElementNotFoundTimeoutException, InterruptedException{
        logger.info("Starting to fill the section 4 form: Angaben, for servicetype: {}.", serviceType);
        enterFirstName();
        enterLastName();
        enterBirthdate();
        enterEmail();

        if (serviceType.equals("Extend a residence title")) {
            enterResidencePermitId(RESIDENCE_PERMIT_NUMBER_EXTENSION.getName());
            return;
        }

        if (serviceType.equals("Apply for a residence title")) {
            selectResidencePermit();
            if (isResidencePermitPresent) {
                enterResidencePermitId(RESIDENCE_PERMIT_NUMBER.getName());
            }
            return;
        }
        logger.error("This shouldn't happen");
        throw new RuntimeException("Failed to enter residence permit id");

 }

    protected void enterFirstName() throws InterruptedException, ElementNotFoundTimeoutException {
        String elementDescription = Section4FormParameterEnum.FIRSTNAME.name();
        String elementName = Section4FormParameterEnum.FIRSTNAME.getName();
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                WebElement element = driver.findElements(By.tagName("input")).stream().filter(element1 -> element1.getAttribute("name").equals(elementName)).collect(Collectors.toList()).get(0);
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
        String elementDescription = Section4FormParameterEnum.LASTNAME.name();
        String elementName = Section4FormParameterEnum.LASTNAME.getName();
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                WebElement element = driver.findElements(By.tagName("input")).stream().
                        filter(element1 -> element1.getAttribute("name").equals(elementName)).collect(Collectors.toList()).get(0);
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
        String elementDescription = Section4FormParameterEnum.EMAIL.name();
        String elementName = Section4FormParameterEnum.EMAIL.getName();
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                WebElement element = driver.findElements(By.tagName("input")).stream()
                        .filter(element1 -> element1.getAttribute("name").equals(elementName)).collect(Collectors.toList()).get(0);
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
        String elementDescription = Section4FormParameterEnum.BIRTHDATE.name();
        String elementName = Section4FormParameterEnum.BIRTHDATE.getName();
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                WebElement element = driver.findElements(By.tagName("input")).stream()
                        .filter(element1 -> element1.getAttribute("name").equals(elementName)).collect(Collectors.toList()).get(0);
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

    protected void enterResidencePermitId(String elementName) throws ElementNotFoundTimeoutException, InterruptedException {
        logger.info("Entering the residence permit id");
        String elementDescription = "residenetPermit";
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                WebElement element = driver.findElements(By.tagName("input")).stream()
                        .filter(element1 -> element1.getAttribute("name").equals(elementName)).collect(Collectors.toList()).get(0);
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

    protected void selectResidencePermit() throws InterruptedException, ElementNotFoundTimeoutException{
        String elementDescription = Section4FormParameterEnum.RESIDENCE_PERMIT.getName();
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                WebElement element = driver.findElements(By.tagName("select")).stream().filter(element1 -> element1.getAttribute("name").equals(elementDescription)).collect(Collectors.toList()).get(0);
                if (isResidencePermitPresent) {
                    DriverUtils.selectOptionByValue(element, elementDescription, "1");
                } else {
                    DriverUtils.selectOptionByValue(element, elementDescription, "0");
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
        WebElement element = DriverUtils.getElementById(elementId, elementDescription, driver);
        DriverUtils.clickToElement(element, elementDescription);
    }

    public RemoteWebDriver getDriver() {
        return this.driver;
    }
}
