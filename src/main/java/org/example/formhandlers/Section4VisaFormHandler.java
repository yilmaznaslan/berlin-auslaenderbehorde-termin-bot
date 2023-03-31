package org.example.formhandlers;


import com.google.common.annotations.VisibleForTesting;
import org.example.enums.Section4FormParameterEnum;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.example.enums.Section4FormParameterEnum.RESIDENCE_PERMIT_NUMBER;
import static org.example.enums.Section4FormParameterEnum.RESIDENCE_PERMIT_NUMBER_EXTENSION;
import static org.example.utils.DriverUtils.TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS;
import static org.example.utils.DriverUtils.TIMEOUT_FOR_INTERACTING_IN_SECONDS;
import static org.example.utils.IoUtils.savePage;

/**
 * Business Access Layer for filling the Section 4: Angaben
 */
public class Section4VisaFormHandler {

    private static final Logger logger = LoggerFactory.getLogger(Section4VisaFormHandler.class);
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final String birthdate;
    private final Boolean isResidencePermitPresent;
    private final String residencePermitId;
    private final String serviceType;
    public final RemoteWebDriver driver;

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

    public void fillAndSendForm() {
        savePage(driver, this.getClass().getSimpleName(), "");
        fillForm();

        savePage(driver, this.getClass().getSimpleName(), "after_filling");
        sendForm();
        savePage(driver, this.getClass().getSimpleName(), "after_send");
    }

    protected void fillForm() {
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

    @VisibleForTesting
    protected void enterFirstName() {
        String elementName = Section4FormParameterEnum.FIRSTNAME.getName();
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='" + elementName + "']")));
        element.sendKeys(firstName);
    }

    @VisibleForTesting
    protected void enterLastName() {
        String elementName = Section4FormParameterEnum.LASTNAME.getName();
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='" + elementName + "']")));
        element.sendKeys(lastName);
    }

    @VisibleForTesting
    protected void enterEmail() {
        String elementName = Section4FormParameterEnum.EMAIL.getName();
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='" + elementName + "']")));
        element.sendKeys(emailAddress);
    }

    @VisibleForTesting
    protected void enterBirthdate() {
        String elementName = Section4FormParameterEnum.BIRTHDATE.getName();
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='" + elementName + "']")));
        element.sendKeys(birthdate);
    }

    @VisibleForTesting
    protected void enterResidencePermitId(String elementName) {
        logger.info("Entering the residence permit id");
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='" + elementName + "']")));
        element.sendKeys(residencePermitId);
    }

    @VisibleForTesting
    protected void selectResidencePermit() {
        String elementName = Section4FormParameterEnum.RESIDENCE_PERMIT.getName();
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("select[name='" + elementName + "']")));
        if (isResidencePermitPresent) {
            selectOptionByValue(element, elementName, "1");
        } else {
            selectOptionByValue(element, elementName, "0");
        }
    }

    private void selectOptionByValue(WebElement element, String elementDescription, String optionValue) {
        if (element == null) {
            logger.warn("Element:{} is null, Process: Select can not be continued", elementDescription);
            return;
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_IN_SECONDS));
        Select select = new Select(wait.until(ExpectedConditions.elementToBeClickable(element)));
        select.selectByValue(optionValue);
        WebElement option = select.getFirstSelectedOption();
        String selectValue = option.getText();
        logger.info("Selected value:", selectValue);
    }


    protected void sendForm() {
        String elementId = "applicationForm:managedForm:proceed";
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS))
                .until(__ -> driver.findElement(By.id(elementId)));
        element.click();
    }


}
