package org.example.formhandlers;

import com.google.common.annotations.VisibleForTesting;
import org.example.enums.FormParameterEnum;
import org.example.enums.SeleniumProcessEnum;
import org.example.enums.SeleniumProcessResultEnum;
import org.example.exceptions.ElementNotFoundTimeoutException;
import org.example.exceptions.FormValidationFailed;
import org.example.utils.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.enums.SeleniumProcessResultEnum.SUCCESSFUL;
import static org.example.utils.DriverUtils.*;
import static org.example.utils.IoUtils.savePage;
import static org.example.utils.LogUtils.logInfo;
import static org.example.utils.LogUtils.logWarn;

public class Section3DateSelectionHandler {

    private final Logger logger = LoggerFactory.getLogger(Section3DateSelectionHandler.class);

    public int handledTimeslotCount = 0;
    public int handledDateCount = 0;

    public RemoteWebDriver driver;

    public Section3DateSelectionHandler(RemoteWebDriver webDriver) {
        this.driver = webDriver;
    }

    public void fillAndSendForm() throws FormValidationFailed, InterruptedException, ElementNotFoundTimeoutException {
        handleAppointmentSelection();
        Thread.sleep(2000);
        handleTimeSelection();
        Thread.sleep(2000);
        sendForm();
    }

    @VisibleForTesting
    protected void handleAppointmentSelection() {
        String elementDescription = "DateSelection".toUpperCase();
        MDC.put("elementDescription", elementDescription);

        logger.info("Starting to find an appointment date");
        handledDateCount++;
        String cssSelector = "[data-handler=selectDay]";
        savePage(driver, this.getClass().getSimpleName(), "handleAppointmentSelection");
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS))
                .until(__ -> driver.findElement(By.cssSelector(cssSelector)));
        if (isDateVerified(element)) {
            logger.info("Date is verified");
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().build().perform();
            logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, SUCCESSFUL.name());
        }
    }

    @VisibleForTesting
    protected void handleTimeSelection() throws FormValidationFailed, InterruptedException {
        savePage(driver, this.getClass().getSimpleName(), "handleTimeSelection");
        handledTimeslotCount++;

        // Get timeslot element && timeslot options
        Select webElement = getAvailableTimeslotOptions();

        // Verify timeslot options
        if (isTimeslotOptionVerified(webElement)) {
            selectTimeslot(webElement);
        } else {
            throw new FormValidationFailed("Validating the time slots has failed");
        }
    }

    @VisibleForTesting
    protected Select getAvailableTimeslotOptions() {
        String elementName = FormParameterEnum.TIME_SLOT.getName();
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS))
                .until(__ -> {
                    WebElement selectElement = driver.findElements(By.tagName("select")).stream()
                            .filter(element1 -> element1.getAttribute("name").equals(elementName))
                            .collect(Collectors.toList()).get(0);
                    return selectElement;
                });
        return new Select(element);
    }

    @VisibleForTesting
    protected void selectTimeslot(Select select) throws InterruptedException {
        String elementDescription = "TIMESLOT";
        List<WebElement> availableHours = select.getOptions();
        for (int i = 0; i < availableHours.size(); i++) {
            String timeSlot = availableHours.get(i).getText();
            if (timeSlot != null || !timeSlot.equals("")) {
                logger.info(String.format("Available timeslot: Timeslot: %s, Value: %s", i, timeSlot));
                String selectValue = availableHours.get(0).getText();
                select.selectByIndex(i);
                logInfo(elementDescription, SeleniumProcessEnum.SELECTING_OPTION, SUCCESSFUL.name(), "Value: " + selectValue);
                break;
            }
        }
        Thread.sleep(1000);
    }

    @VisibleForTesting
    protected void sendForm() throws InterruptedException, ElementNotFoundTimeoutException {
        String elementId = "applicationForm:managedForm:proceed";
        String elementDescription = "weiter button".toUpperCase();
        WebElement element = DriverUtils.getElementById(elementId, elementDescription, driver);
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().build().perform();
        savePage(driver, this.getClass().getSimpleName(), "after_send");
    }

    @VisibleForTesting
    protected boolean isDateVerified(WebElement element) {
        String dateMonth = element.getAttribute("data-month");
        String dateYear = element.getAttribute("data-year");
        String dateDay = element.getText();
        logger.info(String.format("Selected date: Day:%s Month:%s Year:%s", dateDay, dateMonth, dateYear));
        return dateDay != null && dateMonth != null && dateYear != null;
    }

    @VisibleForTesting
    protected boolean isTimeslotOptionVerified(Select select) {
        List<WebElement> availableHours = select.getOptions();
        int availableHoursCount = availableHours.size();
        logger.info(String.format("There are %s available timeslots", availableHoursCount));
        return availableHours.stream()
                .peek(availableHour -> logger.info(String.format("Timeslot: %s, Value: %s", availableHours.indexOf(availableHour), availableHour.getText())))
                .map(WebElement::getText)
                .anyMatch(timeSlot -> timeSlot != null && !timeSlot.equals("") && timeSlot.contains(":"));
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
                    savePage(driver, this.getClass().getSimpleName(), "date_selecntion_in");
                    return true;
                }
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
