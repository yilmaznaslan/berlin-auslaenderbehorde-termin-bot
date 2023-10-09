package com.yilmaznaslan.formhandlers;

import com.google.common.annotations.VisibleForTesting;
import com.yilmaznaslan.AppointmentFinder;
import com.yilmaznaslan.enums.MdcVariableEnum;
import com.yilmaznaslan.enums.Section2FormElementsEnum;
import com.yilmaznaslan.forms.PersonalInfoFormTO;
import com.yilmaznaslan.forms.VisaFormTO;
import com.yilmaznaslan.utils.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;

import static com.yilmaznaslan.enums.Section2FormElementsEnum.*;

/**
 * This class is responsible for filling the form in section 2
 */
public class Section2ServiceSelectionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Section2ServiceSelectionHandler.class);
    private static final boolean SHOULD_ACTION_USED = false;
    private static final String LABEL = "label";
    private static final String LOG_MSG = "Starting to {}";

    private final String citizenshipValue;
    private final String citizenshipValueOfFamilyMember;
    private final String applicantNumber;
    private final String familyStatus;
    private final String serviceTypeLabelValue;
    private final String visaLabelValue;
    private final String visaPurposeLabelValue;
    private final RemoteWebDriver driver;
    private int searchCount = 0;
    private String sessionUrl = null;

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
        fillForm();
        while (isSessionActive()) {
            sendForm();
            try {
                DriverUtils.waitUntilFinished(driver);
                LOGGER.info("Page is loaded");
            } catch (Exception e) {
                LOGGER.info("Page is not loaded due to some error. Will try again.Reason", e);
                return false;
            }
            if (isDateSelectionOpened()) {
                if (isErrorMessageShow()) {
                    LOGGER.info("Calender page is opened but there is alert or error");
                    return false;
                } else {
                    LOGGER.info("Calender page is opened and there is no alert");
                    return true;
                }

            }
            Thread.sleep(AppointmentFinder.FORM_REFRESH_PERIOD_IN_SECONDS * 1000);
        }
        return false;
    }

    private void fillForm() {
        LOGGER.info("Starting to fill the form in section 2");
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
        searchCount = searchCount + 1;
    }

    private void selectCitizenshipValue() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String elementDescription = Section2FormElementsEnum.COUNTRY.name();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);
        LOGGER.debug(LOG_MSG, methodName);

        String elementName = Section2FormElementsEnum.COUNTRY.getName();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(ddriver -> {
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
                    LOGGER.debug("Successfully selected the citizenship value");
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
        String elementDescription = Section2FormElementsEnum.COUNTRY_OF_FAMILY_MEMBER.name();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);
        LOGGER.debug(LOG_MSG, methodName);
        String elementName = Section2FormElementsEnum.COUNTRY_OF_FAMILY_MEMBER.getName();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(currentDriver -> {
            try {
                WebElement element = currentDriver.findElement(By.cssSelector("select[name='" + elementName + "']"));
                Select select = new Select(element);
                select.selectByVisibleText(citizenshipValueOfFamilyMember);
                WebElement option = select.getFirstSelectedOption();
                String selectValue = option.getText();
                if (selectValue.equals(citizenshipValueOfFamilyMember)) {
                    LOGGER.debug("Successfully selected the citizenship value");
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
        String elementDescription = Section2FormElementsEnum.APPLICANT_COUNT.name();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);
        LOGGER.debug(LOG_MSG, methodName);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(currentWebdriver -> {
            try {
                WebElement element = currentWebdriver.findElement(By.cssSelector("select[name='personenAnzahl_normal']"));
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
        String elementDescription = FAMILY_STATUS.name();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);
        LOGGER.debug(LOG_MSG, methodName);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(currentDriver -> {
            try {
                WebElement webElement = currentDriver.findElement(By.cssSelector("select[name='lebnBrMitFmly']"));
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
        String elementDescription = SERVICE_TYPE.name();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);
        LOGGER.debug(LOG_MSG, methodName);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(currentDriver -> {
            try {
                WebElement selectElement = currentDriver.findElements(By.tagName(LABEL)).stream().filter(webElement -> webElement.getText().equals(serviceTypeLabelValue)).findFirst().orElseThrow(() -> new NoSuchElementException("Unable to locate element with text: " + serviceTypeLabelValue));
                selectElement.click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    private void clickVisaPurpose() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug("Starting to: {}", methodName);
        String elementDescription = VISA_PURPOSE.name();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(currentDriver -> {
            try {
                currentDriver.findElements(By.tagName(LABEL)).stream().filter(webElement -> webElement.getText().equals(visaPurposeLabelValue)).findFirst().get().click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    private void clickToVisa() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String elementDescription = VISA.name();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);
        LOGGER.debug("Starting to: {}", methodName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(ddriver -> {
            try {
                driver.findElements(By.tagName(LABEL)).stream().filter(webElement -> webElement.getText().equals(visaLabelValue)).findFirst().get().click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    private void sendForm() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(LOG_MSG, methodName);

        String elementXpath = "//*[@id=\"applicationForm:managedForm:proceed\"]";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(currentDriver -> {
            try {
                WebElement element = currentDriver.findElement(By.xpath(elementXpath));
                if (element.isDisplayed() && element.isEnabled()) {

                    if (SHOULD_ACTION_USED) {
                        Actions actions = new Actions(currentDriver);
                        actions.moveToElement(element).click().build().perform();
                    } else {
                        element.click();
                    }
                    LOGGER.info("Form is sent");
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
    protected boolean isErrorMessageShow() {
        String elementDescription = ERROR_MESSAGE.getName();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);
        String elementXPath = "//*[@id=\"messagesBox\"]/ul/li";

        try {
            WebElement element = driver.findElement(By.xpath(elementXPath));
            String errorMessage = element.getText();
            LOGGER.info("Error message: {}", errorMessage);
            return true;
        } catch (NoSuchElementException exception) {
            LOGGER.info("There is no error message");
            return false;
        } catch (Exception exception) {
            LOGGER.info("An exception occurred while checking for the error message", exception);
            return false;
        }
    }

    @VisibleForTesting
    protected boolean isDateSelectionOpened() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String elementDescription = ACTIVE_STEP.name();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);
        LOGGER.info(LOG_MSG, methodName);

        try {
            String elementXPath = "//*[@id=\"main\"]/div[2]/div[4]/div[2]/div/div[1]/ul/li[2]";

            // Initialize WebDriverWait and set the maximum time to wait
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));

            // Wait for the element to become visible
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementXPath)));

            String activeStepText = element.getText().replace("\n", "");
            LOGGER.info("Value of the {} is: {}", elementDescription, activeStepText);

            return activeStepText.contains("Date selection");
        } catch (NoSuchElementException e) {
            LOGGER.info("Element not found. Reason", e);
            return false;
        } catch (Exception exception) {
            LOGGER.info("Active tab info is not available. Reason", exception);
            return false;
        }
    }

    private boolean isSessionActive() {

        String xpath = "//*[@id=\"progressBar\"]/div";
        WebElement element = driver.findElement(By.xpath(xpath));
        String timeText = element.getText();
        String[] minuteSecond = timeText.split(":");
        int minute = Integer.parseInt(minuteSecond[0]);
        int second = Integer.parseInt(minuteSecond[1]);

        LOGGER.info("Remaining time: {}. Minute: {}, Second: {}", timeText, minute, second);
        return minute >= 5;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public String getSessionUrl() {
        return sessionUrl;
    }
}
