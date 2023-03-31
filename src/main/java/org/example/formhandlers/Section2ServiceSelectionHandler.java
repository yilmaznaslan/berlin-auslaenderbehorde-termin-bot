package org.example.formhandlers;


import com.google.common.annotations.VisibleForTesting;
import org.example.enums.MdcVariableEnum;
import org.example.enums.Section2FormElementsEnum;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;

import static org.example.enums.Section2FormElementsEnum.FAMILY_STATUS;
import static org.example.utils.DriverUtils.TIMEOUT_FOR_GETTING_CALENDER_ELEMENT_IN_SECONDS;
import static org.example.utils.DriverUtils.TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS;
import static org.example.utils.IoUtils.savePage;

/**
 * Business Access Layer for filling the form
 */
public class Section2ServiceSelectionHandler implements IFormHandler {

    private final Logger logger = LoggerFactory.getLogger(Section2ServiceSelectionHandler.class);
    private final String citizenshipValue;
    private final String applicantNumber;
    private final String familyStatus;
    private final String serviceTypeLabelValue;
    private final String visaLabelValue;
    private final String visaPurposeLabelValue;

    private static int searchCount = 0;

    public RemoteWebDriver driver;

    public Section2ServiceSelectionHandler(VisaFormTO visaFormTO, PersonalInfoFormTO personalInfoFormTO, RemoteWebDriver remoteWebDriver) {
        this.visaPurposeLabelValue = visaFormTO.getVisaPurposeValue();
        this.driver = remoteWebDriver;
        this.citizenshipValue = personalInfoFormTO.getCitizenshipValue();
        this.applicantNumber = personalInfoFormTO.getApplicationsNumber();
        this.familyStatus = personalInfoFormTO.getFamilyStatus();
        this.serviceTypeLabelValue = visaFormTO.getServiceType();
        this.visaLabelValue = visaFormTO.getVisaLabelValue();
    }

    public boolean fillAndSendForm() throws InterruptedException {
        logger.info("Starting to fill the form in section 2");
        selectCitizenshipValue();
        selectApplicantsCount();
        selectFamilyStatus();
        if (familyStatus.equals("1")){
            selectCitizenshipValueOfFamilyMember();
        }
        clickServiceType();
        if (visaPurposeLabelValue != null) {
            clickVisaPurpose();
        }
        clickToVisa();
        Thread.sleep(2000);
        sendForm();
        return isCalenderFound();
    }

    @Override
    public RemoteWebDriver getDriver() {
        return driver;
    }

    private void selectCitizenshipValue() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to " + methodName);

        String elementDescription = Section2FormElementsEnum.COUNTRY.name();
        String elementName = Section2FormElementsEnum.COUNTRY.getName();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS));

        wait.until(__ -> {
            try {
                WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("select[name='" + elementName + "']")));
                wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(element)));
                wait.until(ExpectedConditions.visibilityOf(element));
                Select select = new Select(element);
                select.selectByVisibleText(citizenshipValue);
                WebElement option = select.getFirstSelectedOption();
                String selectValue = option.getText();
                if (selectValue.equals(citizenshipValue)) {
                    logger.info("Successfully selected the citizenship value");
                    return true;
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        });

    }

    private void selectCitizenshipValueOfFamilyMember() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to " + methodName);

        String elementDescription = Section2FormElementsEnum.COUNTRY_OF_FAMILY_MEMBER.name();
        String elementName = Section2FormElementsEnum.COUNTRY_OF_FAMILY_MEMBER.getName();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS));

        wait.until(__ -> {
            try {
                WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("select[name='" + elementName + "']")));
                wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(element)));
                wait.until(ExpectedConditions.visibilityOf(element));
                Select select = new Select(element);
                select.selectByVisibleText(citizenshipValue);
                WebElement option = select.getFirstSelectedOption();
                String selectValue = option.getText();
                if (selectValue.equals(citizenshipValue)) {
                    logger.info("Successfully selected the citizenship value");
                    return true;
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        });

    }

    private void selectApplicantsCount() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to " + methodName);

        String elementDescription = Section2FormElementsEnum.APPLICANT_COUNT.name();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS));
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("select[name='personenAnzahl_normal']")));
        wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(element)));

        Select select = new Select(element);
        select.selectByValue(applicantNumber);
        WebElement option = select.getFirstSelectedOption();
        String selectValue = option.getText();
        if (selectValue.equals(applicantNumber)) {
            logger.info("Successfully selected the applicants number");
        }
    }

    private void selectFamilyStatus() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to " + methodName);

        String elementDescription = FAMILY_STATUS.name();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("select[name='lebnBrMitFmly']")));
        Select select = new Select(element);
        select.selectByValue(familyStatus);
    }

    protected void clickServiceType() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to " + methodName);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS));
        wait.until(__ -> {
            try {
                WebElement selectElement = driver.findElements(By.tagName("label")).stream().filter(webElement -> webElement.getText().equals(serviceTypeLabelValue)).findFirst().orElseThrow(() -> new NoSuchElementException("Unable to locate element with text: " + serviceTypeLabelValue));
                selectElement.click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });

    }

    private void clickVisaPurpose() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to " + methodName);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS));
        WebElement element = wait.until(__ -> {
            WebElement selectElement = driver.findElements(By.tagName("label")).stream().filter(webElement -> webElement.getText().equals(visaPurposeLabelValue)).findFirst().orElseThrow(() -> new NoSuchElementException("Unable to locate element with text: " + serviceTypeLabelValue));
            return selectElement;
        });
        wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(element)));
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    private void clickToVisa() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to " + methodName);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS));
        WebElement element = wait.until(__ -> {
            WebElement selectElement = driver.findElements(By.tagName("label")).stream().filter(webElement -> webElement.getText().equals(visaLabelValue)).findFirst().orElseThrow(() -> new NoSuchElementException("Unable to locate element with text: " + serviceTypeLabelValue));
            return selectElement;
        });
        wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(element)));
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    private void sendForm() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to " + methodName);

        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementXpath)));
        element.click();
        searchCount++;
        String msg = String.format("SuccessfullyFormSenCount:%s", searchCount);
        logger.info(msg);
    }

    @VisibleForTesting
    protected boolean isCalenderFound() {
        String elementDescription = "active step";
        String activeTabXPath = "//*[@id=\"main\"]/div[2]/div[4]/div[2]/div/div[1]/ul/li[2]";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_CALENDER_ELEMENT_IN_SECONDS));
        WebElement activeStepElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(activeTabXPath)));
        String activeStepText = activeStepElement.getText();
        logger.info(String.format("Value of the %s is: %s", elementDescription, activeStepText));
        if (activeStepText.contains("Date selection")) {
            savePage(driver, this.getClass().getSimpleName(), "date_selecntion_in");
            logger.info("Calender page is opened");
            return true;
        }
        logger.info("Calender page is not opened");
        return false;
    }
}
