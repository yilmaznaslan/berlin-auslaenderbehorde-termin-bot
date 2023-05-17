package org.example.formhandlers;


import com.google.common.annotations.VisibleForTesting;
import org.example.enums.MdcVariableEnum;
import org.example.enums.Section2FormElementsEnum;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;

import static org.example.Config.FORM_REFRESH_PERIOD_IN_SECONDS;
import static org.example.Config.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS;
import static org.example.TerminFinder.searchCount;
import static org.example.enums.Section2FormElementsEnum.FAMILY_STATUS;
import static org.example.utils.IoUtils.increaseCalenderOpenedMetric;
import static org.example.utils.IoUtils.savePage;

/**
 * Business Access Layer for filling the form
 */
public class Section2ServiceSelectionHandler implements IFormHandler {

    private final Logger logger = LoggerFactory.getLogger(Section2ServiceSelectionHandler.class);
    private final String citizenshipValue;
    private final String citizenshipValueOfFamilyMember;
    private final String applicantNumber;
    private final String familyStatus;
    private final String serviceTypeLabelValue;
    private final String visaLabelValue;
    private final String visaPurposeLabelValue;
    private final RemoteWebDriver driver;
    private int searchCountWithCalenderOpened = 0;

    public Section2ServiceSelectionHandler(VisaFormTO visaFormTO, PersonalInfoFormTO personalInfoFormTO, RemoteWebDriver remoteWebDriver) {
        this.visaPurposeLabelValue = visaFormTO.getVisaPurposeValue();
        this.driver = remoteWebDriver;
        this.citizenshipValue = personalInfoFormTO.getCitizenshipValue();
        this.citizenshipValueOfFamilyMember = personalInfoFormTO.getCitizenshipValueOfFamilyMember();
        this.applicantNumber = personalInfoFormTO.getNumberOfApplicants();
        this.familyStatus = personalInfoFormTO.getIsThereFamilyMember();
        this.serviceTypeLabelValue = visaFormTO.getServiceType();
        this.visaLabelValue = visaFormTO.getVisaLabelValue();
    }

    public boolean fillAndSendForm() throws InterruptedException {
        while (true) {
            fillForm();
            sendForm();
            if (isCalenderFound()) {
                break;
            }
            Thread.sleep(FORM_REFRESH_PERIOD_IN_SECONDS * 1000);
        }
        return true;
    }

    private void fillForm() throws InterruptedException {
        logger.info("Starting to fill the form in section 2");
        selectCitizenshipValue();
        selectNumberOfApplicants();
        selectFamilyStatus();
        if (familyStatus.equals("yes")) {
            selectCitizenshipValueOfFamilyMember();
        }
        clickServiceType();
        if (visaPurposeLabelValue != null) {
            clickVisaPurpose();
        }
        clickToVisa();
        Thread.sleep(2000);
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(__ -> {
            try {
                WebElement element = driver.findElement(By.cssSelector("select[name='" + elementName + "']"));
                Select select = new Select(element);
                select.selectByVisibleText(citizenshipValue);

                // Double check if it is selected
                element = driver.findElement(By.cssSelector("select[name='" + elementName + "']"));
                select = new Select(element);
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(__ -> {
            try {
                WebElement element = driver.findElement(By.cssSelector("select[name='" + elementName + "']"));
                Select select = new Select(element);
                select.selectByVisibleText(citizenshipValueOfFamilyMember);
                WebElement option = select.getFirstSelectedOption();
                String selectValue = option.getText();
                if (selectValue.equals(citizenshipValueOfFamilyMember)) {
                    logger.info("Successfully selected the citizenship value");
                    return true;
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        });
    }

    private void selectNumberOfApplicants() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to " + methodName);
        String elementDescription = Section2FormElementsEnum.APPLICANT_COUNT.name();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(__ -> {
            try {
                WebElement element = driver.findElement(By.cssSelector("select[name='personenAnzahl_normal']"));
                Select select = new Select(element);
                select.selectByVisibleText(applicantNumber);
                return true;
            } catch (Exception exception) {
                return false;
            }
        });

    }

    private void selectFamilyStatus() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to " + methodName);
        String elementDescription = FAMILY_STATUS.name();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(__ -> {
            try {
                WebElement webElement = driver.findElement(By.cssSelector("select[name='lebnBrMitFmly']"));
                Select select = new Select(webElement);
                select.selectByVisibleText(familyStatus);
                return true;
            } catch (Exception e) {
                return false;

            }
        });
    }

    protected void clickServiceType() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to " + methodName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(__ -> {
            try {
                driver.findElements(By.tagName("label")).stream()
                        .filter(webElement -> webElement.getText().equals(visaPurposeLabelValue))
                        .findFirst()
                        .get()
                        .click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    private void clickToVisa() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to " + methodName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(__ -> {
            try {
                driver.findElements(By.tagName("label")).stream()
                        .filter(webElement -> webElement.getText().equals(visaLabelValue))
                        .findFirst()
                        .get()
                        .click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    // This method is slow 10 seconds
    @VisibleForTesting
    protected boolean isErrorMessageShow() {
        String elementXPath = "//*[@id=\"messagesBox\"]/ul/li";
        String errorMsg = "There are currently no dates available for the selected service! Please try again later.";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean result = false;
        try {
            result = wait.until(__ -> {
                try {
                    WebElement element = driver.findElement(By.xpath(elementXPath));
                    logger.info("ErrorMessage: {}", element.getText());
                    return element.getText().contains(errorMsg);
                } catch (Exception exception) {
                    return false;
                }
            });
        } catch (TimeoutException exception) {
            // do nothing and continue
        }
        return result;
    }

    protected void sendForm() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to " + methodName);
        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(__ -> {
            try {
                WebElement element = driver.findElement(By.xpath(elementXpath));
                if (element.isDisplayed() && element.isEnabled()) {
                    //Actions actions = new Actions(driver);
                    //actions.moveToElement(element).click().build().perform();
                    if (!isErrorMessageShow()) {
                        element.click();
                    }
                    searchCount = searchCount + 1;
                    String msg = String.format("Successfully send form count is: %s", searchCount);
                    logger.info(msg);
                    return true;
                } else {
                    return false;
                }

            } catch (Exception exception) {
                return false;
            }
        });
    }

    @VisibleForTesting
    protected boolean isCalenderFound() {
        String elementDescription = "active step";
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);

        String activeTabXPath = "//*[@id=\"main\"]/div[2]/div[4]/div[2]/div/div[1]/ul/li[2]";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement activeStepElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(activeTabXPath)));
        String activeStepText = activeStepElement.getText();
        logger.info(String.format("Value of the %s is: %s", elementDescription, activeStepText));
        if (activeStepText.contains("Date selection")) {
            searchCountWithCalenderOpened++;
            increaseCalenderOpenedMetric();
            savePage(driver, this.getClass().getSimpleName(), "date_selection_in");
            logger.info("Calender page is opened");
            return true;
        }
        logger.info("Calender page is not opened. Search count: {}. SearchCountWithCalenderOpened: {}", searchCount, searchCountWithCalenderOpened);
        return false;
    }
}
