package com.yilmaznaslan.formhandlers;

import com.google.common.annotations.VisibleForTesting;
import com.yilmaznaslan.enums.MdcVariableEnum;
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

import static com.yilmaznaslan.AppointmentFinder.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS;
import static com.yilmaznaslan.utils.IoUtils.savePage;


public class Section3DateSelectionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(Section3DateSelectionHandler.class);

    private final RemoteWebDriver driver;

    public Section3DateSelectionHandler(RemoteWebDriver webDriver) {
        this.driver = webDriver;
    }

    @VisibleForTesting
    protected void handleAppointmentDateSelection() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String elementDescription = "DateSelection".toUpperCase();
        MDC.put(MdcVariableEnum.elementDescription.name(), elementDescription);
        LOGGER.info("Starting to: {}", methodName);

        String cssSelector = "[data-handler=selectDay]";
        savePage(driver, "handleAppointmentSelection");
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS))
                .until(currentDriver -> currentDriver.findElement(By.cssSelector(cssSelector)));
        if (isDateVerified(element)) {
            LOGGER.info("Date is verified");
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().build().perform();
            LOGGER.info("Date selection is clicked successfully");
        }
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
        LOGGER.info(String.format("There are %s available timeslots", availableHoursCount));
        return availableHours.stream()
                .peek(availableHour -> LOGGER.info(String.format("Timeslot: %s, Value: %s", availableHours.indexOf(availableHour), availableHour.getText())))
                .map(WebElement::getText)
                .anyMatch(timeSlot -> timeSlot != null && !timeSlot.equals("") && timeSlot.contains(":"));
    }

}
