package com.yilmaznaslan.formhandlers;

import com.yilmaznaslan.AppointmentFinder;
import com.yilmaznaslan.utils.DriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.yilmaznaslan.AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS;
import static com.yilmaznaslan.enums.Section3FormElements.TIME_SLOT;

public class Section3DateSelectionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Section3DateSelectionHandler.class);

    private final RemoteWebDriver driver;

    public Section3DateSelectionHandler(RemoteWebDriver webDriver) {
        this.driver = webDriver;
    }

    public Optional<WebElement> isDateAndTimeVerified() {
        List<WebElement> availableDates = getAvailableDates();
        Collections.reverse(availableDates); // To start from the latest date
        List<String> datesAsString = availableDates.stream().map(this::parseDate).toList();
        return availableDates.stream()
                .filter(availableDate -> {
                    Actions actions = new Actions(driver);
                    actions.moveToElement(availableDate).click().build().perform();
                    DriverUtils.waitUntilFinished(driver);
                    int dateIndex = availableDates.indexOf(availableDate);
                    String date = datesAsString.get(dateIndex);
                    LOGGER.info("Clicked on date: {}", date);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    // Get timeslot element && timeslot options
                    Select webElement = getTimeslotSelect();
                    List<WebElement> availableTimeslots = webElement.getOptions();
                    availableTimeslots
                            .forEach(availableTimeslot -> LOGGER.info("Date: {}, Timeslot: {}", date,
                                    availableTimeslot.getText()));

                    Optional<WebElement> firstAvailableTimeslot = availableTimeslots.stream()
                            .filter(this::isTimeslotVerified)
                            .findFirst();

                    return firstAvailableTimeslot.isPresent();
                }).findFirst();
    }

    private List<WebElement> getAvailableDates() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info("Starting to: {}", methodName);
        String cssSelector = "[data-handler=selectDay]";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        List<WebElement> availableDates = wait.until(
                currentDriver -> currentDriver.findElements(By.cssSelector(cssSelector)));

        LOGGER.info("There are {} available dates", availableDates.size());
        availableDates.forEach(element -> {
            String date = parseDate(element);
            LOGGER.info("Date: {}", date);
        });
        return availableDates;
    }

    private String parseDate(WebElement element) {
        String dateMonth = element.getAttribute("data-month");
        int dateMonthInt = Integer.parseInt(dateMonth) + 1;
        String dateYear = element.getAttribute("data-year");
        String dateDay = element.getText();
        return String.format("%s.%s.%s", dateDay, dateMonthInt, dateYear);
    }

    private boolean isTimeslotVerified(WebElement availableTimeslot) {
        String timeSlot = availableTimeslot.getText();
        return timeSlot != null && timeSlot.contains(":");
    }

    private Select getTimeslotSelect() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        return wait.until(currentDriver -> {
            WebElement selectElement = currentDriver.findElement(By.id(TIME_SLOT.getId()));
            return new Select(selectElement);
        });
    }


    /**
     *
     *                         String timeslot = firstAvailableTimeSlot.getText();
     *                         webElement.selectByValue(firstAvailableTimeslot.get().getAttribute("value"));
     *                         String dateMonth = availableDate.getAttribute("data-month");
     *                         int dateMonthInt = Integer.parseInt(dateMonth) + 1;
     *                         String dateYear = availableDate.getAttribute("data-year");
     *                         String dateDay = availableDate.getText();
     *                         String date = String.format("%s.%s.%s", dateDay, dateMonthInt, dateYear);
     *                         LOGGER.info("Date :{}, Time: {} selection is clicked successfully", date, timeslot);
     *                         return true;
     */

}
