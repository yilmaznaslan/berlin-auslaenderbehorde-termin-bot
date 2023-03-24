package org.example.business.formhandlers;

import com.google.common.annotations.VisibleForTesting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.FormParameterEnum;
import org.example.enums.SeleniumProcessEnum;
import org.example.enums.SeleniumProcessResultEnum;
import org.example.exceptions.ElementNotFoundTimeoutException;
import org.example.exceptions.InteractionFailedException;
import org.example.utils.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.enums.SeleniumProcessResultEnum.SUCCESSFUL;
import static org.example.utils.DriverUtils.*;
import static org.example.utils.IoUtils.savePage;
import static org.example.utils.LogUtils.logInfo;
import static org.example.utils.LogUtils.logWarn;

public class Section3DateSelectionHandler {

    private final Logger logger = LogManager.getLogger(Section3DateSelectionHandler.class);

    public static int foundAppointmentCount = 0;
    public static int handledAppointmentCount = 0;

    private RemoteWebDriver driver;

    public Section3DateSelectionHandler(RemoteWebDriver webDriver) {
        this.driver = webDriver;
    }

    public void fillAndSendForm() throws Exception {
        savePage(driver, this.getClass().getSimpleName(), "");
        handleFindingDate();
        handleSelectingTimeslotAndSendForm();
    }

    @VisibleForTesting
    protected void handleFindingDate() throws Exception {
        String elementDescription = "DateSelection".toUpperCase();
        logger.info("Starting to find an appointment date");
        handledAppointmentCount++;
        String cssSelector = "[data-handler=selectDay]";
        savePage(driver, this.getClass().getSimpleName(), "handling_date");
        Exception exception = null;
        int i = 1;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                WebElement element = driver.findElement(By.cssSelector(cssSelector));
                if (isDateVerified(element)) {
                    logger.info("Date is verified");
                    element.click();
                    logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SUCCESSFUL.name());
                    break;
                }
            } catch (Exception e) {
                exception = e;
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            throw exception;
        }

    }

    private void handleSelectingTimeslotAndSendForm() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException {
        String elementDescription = FormParameterEnum.TIME_SLOT.name();
        WebElement element = null;
        String elementName = FormParameterEnum.TIME_SLOT.getName();
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                element = driver.findElements(By.tagName("input")).stream().filter(element1 -> element1.getAttribute("name").equals(elementName)).collect(Collectors.toList()).get(0);
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
        if (isTimeslotOptionVerified(element)) {
            foundAppointmentCount++;
            Select select = new Select(element);
            List<WebElement> availableHours = select.getOptions();
            String selectValue = availableHours.get(0).getText();
            select.selectByIndex(0);
            logInfo(elementDescription, SeleniumProcessEnum.SELECTING_OPTION, SUCCESSFUL.name(), "Value: " + selectValue);
            Thread.sleep(1000);
            sendForm();
            savePage(driver, this.getClass().getSimpleName(), "after_send");
            Thread.sleep(5000);
            String url = driver.getCurrentUrl();
            logger.info(String.format("Found a place. URL: %s", url));
            logger.info(String.format("Found Appointment count: %s", foundAppointmentCount));

            Thread.sleep(100);

        }
    }

    private void sendForm() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = "applicationForm:managedForm:proceed";
        String elementDescription = "weiter button".toUpperCase();
        WebElement element = DriverUtils.getElementById(elementId, elementDescription, driver);
        DriverUtils.clickToElement(element, elementDescription);
    }

    @VisibleForTesting
    protected boolean isDateVerified(WebElement element) {
        String dateMonth = element.getAttribute("data-month");
        String dateYear = element.getAttribute("data-year");
        String dateDay = element.getText();
        logger.info(String.format("Selected date: Day:%s Month:%s Year:%s", dateDay, dateMonth, dateYear));
        return dateDay != null && dateMonth != null && dateYear != null;
    }

    private boolean isTimeslotOptionVerified(WebElement element) {
        Select select = new Select(element);
        List<WebElement> availableHours = select.getOptions();
        int availableHoursCount = availableHours.size();
        logger.info(String.format("There are %s available timeslots", availableHoursCount));
        for (int i = 0; i < availableHoursCount; i++) {
            logger.info(String.format("Timeslot: %s, Value: %s", i, availableHours.get(i).getText()));
        }
        if (availableHours.get(0).getText().contains("Bitte")) {
            logger.info("Failed to validate timeslots");
            return false;
        }
        return true;
    }

    public RemoteWebDriver getDriver() {
        return driver;
    }

    public boolean isCalenderFound() throws InterruptedException {
        int i = 1;
        String elementDescription = "active step";
        while (i <= TIMEOUT_FOR_GETTING_CALENDER_ELEMENT_IN_SECONDS) {
            try {
                String activeTabXPath = "//*[@id=\"main\"]/div[2]/div[4]/div[2]/div/div[1]/ul/li[2]";
                WebElement activeStepElement = driver.findElement(By.xpath(activeTabXPath));
                String activeStepText = activeStepElement.getText();
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_TEXT, SUCCESSFUL.name(), activeStepText);
                if (activeStepText.contains("Terminauswahl") || activeStepText.contains("Date selection")) {
                    return true;
                }
                String asd = "//*[@id=\"xi-div-1\"]/div[3]";
                String elementDescription1 = "calender".toUpperCase();
                WebElement calender = driver.findElement(By.xpath(asd));
                savePage(driver, this.getClass().getSimpleName(), "date_selecntion_in");
                return true;
            } catch (Exception e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.firstName(), SeleniumProcessResultEnum.FAILED.firstName(), "");
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_CALENDER_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.name(), SeleniumProcessResultEnum.FAILED.name(), "");
        }
        return false;

    }
}
