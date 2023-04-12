package org.example.formhandlers;

import com.google.common.annotations.VisibleForTesting;
import org.example.enums.Section3FormElements;
import org.example.exceptions.FormValidationFailed;
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

import static org.example.Config.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS;
import static org.example.utils.IoUtils.savePage;

public class Section3DateSelectionHandler implements IFormHandler {

    private final Logger logger = LoggerFactory.getLogger(Section3DateSelectionHandler.class);

    public int handledTimeslotCount = 0;
    public int handledDateCount = 0;
    public boolean isHandlingSuccessful = false;

    public RemoteWebDriver driver;

    public Section3DateSelectionHandler(RemoteWebDriver webDriver) {
        this.driver = webDriver;
    }

    public boolean fillAndSendForm() throws FormValidationFailed, InterruptedException {
        handleAppointmentSelection();
        Thread.sleep(2000);
        handleTimeSelection();
        Thread.sleep(2000);
        sendForm();
        return isHandlingSuccessful;
    }

    @Override
    public RemoteWebDriver getDriver() {
        return driver;
    }

    @VisibleForTesting
    protected void handleAppointmentSelection() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to " + methodName);

        String elementDescription = "DateSelection".toUpperCase();
        MDC.put("elementDescription", elementDescription);

        logger.info("Starting to find an appointment date");
        handledDateCount++;
        String cssSelector = "[data-handler=selectDay]";
        savePage(driver, this.getClass().getSimpleName(), "handleAppointmentSelection");
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS))
                .until(__ -> driver.findElement(By.cssSelector(cssSelector)));
        if (isDateVerified(element)) {
            logger.info("Date is verified");
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().build().perform();
            logger.info("Data selection is clicked successfully");
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
        String elementName = Section3FormElements.TIME_SLOT.getName();
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS))
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
                logger.info("TimeSlot selected succesfully:", selectValue);
                break;
            }
        }
        Thread.sleep(1000);
    }

    @VisibleForTesting
    protected void sendForm() {
        String elementId = "applicationForm:managedForm:proceed";
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS))
                .until(__ -> driver.findElement(By.id(elementId)));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().build().perform();
        savePage(driver, this.getClass().getSimpleName(), "after_send");
        isHandlingSuccessful = true;
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


}
