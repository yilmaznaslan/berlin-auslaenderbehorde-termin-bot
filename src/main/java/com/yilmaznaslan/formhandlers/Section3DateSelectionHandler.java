package com.yilmaznaslan.formhandlers;

import com.google.common.annotations.VisibleForTesting;
import com.yilmaznaslan.AppointmentFinder;
import com.yilmaznaslan.enums.Section3FormElements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

import static com.yilmaznaslan.AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS;


public class Section3DateSelectionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Section3DateSelectionHandler.class);

    private final RemoteWebDriver driver;

    public Section3DateSelectionHandler(RemoteWebDriver webDriver) {
        this.driver = webDriver;
    }

    public boolean isDateAndTimeVerified() throws InterruptedException {
        List<WebElement> dates = listDatesAndClickToFirstAvailable();
        WebElement element = dates.get(0);
        if (isDateVerified(element)) {
            LOGGER.info("Date is verified");
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().build().perform();
            LOGGER.info("Date selection is clicked successfully");
        } else {
            return false;
        }

        // Get timeslot element && timeslot options
        Select webElement = getAvailableTimeslotOptions();

        // Verify timeslot options
        if (isTimeslotOptionVerified(webElement)) {
            selectTimeslot(webElement);
            return true;
        } else {
            LOGGER.error("Validating the time slots has failed");
        }
        return false;

    }

    private List<WebElement> listDatesAndClickToFirstAvailable() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info("Starting to: {}", methodName);
        String cssSelector = "[data-handler=selectDay]";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        List<WebElement> elements = wait.until(
                currentDriver -> currentDriver.findElements(By.cssSelector(cssSelector)));
        elements.forEach(element -> {
            String dateMonth = element.getAttribute("data-month");
            String dateYear = element.getAttribute("data-year");
            String dateDay = element.getText();
            LOGGER.info("Available date: Day: {}, Month: {} Year: {}", dateDay, dateMonth, dateYear);
        });
        return elements;
    }

    @VisibleForTesting
    protected boolean isDateVerified(WebElement element) {
        String dateMonth = element.getAttribute("data-month");
        String dateYear = element.getAttribute("data-year");
        String dateDay = element.getText();
        LOGGER.info("Selected date: Day: {}, Month: {} Year: {}", dateDay, dateMonth, dateYear);
        return dateDay != null && dateMonth != null && dateYear != null;
    }

    @VisibleForTesting
    protected boolean isTimeslotOptionVerified(Select select) {
        List<WebElement> availableHours = select.getOptions();
        int availableHoursCount = availableHours.size();
        LOGGER.info("There are %{} available timeslots", availableHoursCount);
        availableHours.forEach(availableHour -> LOGGER.info("Timeslot: {}, Value: {}", availableHours.indexOf(availableHour),
                availableHour.getText()));
        return availableHours.stream()
                .map(WebElement::getText)
                .anyMatch(timeSlot -> timeSlot != null && timeSlot.contains(":"));
    }

    @VisibleForTesting
    protected Select getAvailableTimeslotOptions() {
        String elementName = Section3FormElements.TIME_SLOT.getName();
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS))
                .until(currentDriver -> {
                    WebElement selectElement = currentDriver.findElements(By.tagName("select")).stream()
                            .filter(element1 -> element1.getAttribute("name").equals(elementName))
                            .toList().get(0);
                    return selectElement;
                });
        return new Select(element);
    }

    @VisibleForTesting
    protected void selectTimeslot(Select select) throws InterruptedException {
        List<WebElement> availableHours = select.getOptions();
        for (int i = 0; i < availableHours.size(); i++) {
            String timeSlot = availableHours.get(i).getText();
            if (timeSlot != null || !timeSlot.equals("")) {
                LOGGER.info("Available timeslot: Timeslot: {}, Value: {}", i, timeSlot);
                String selectValue = availableHours.get(0).getText();
                select.selectByIndex(i);
                LOGGER.info("TimeSlot selected succesfully: {}", selectValue);
                break;
            }
        }
        Thread.sleep(1000);
    }

}
