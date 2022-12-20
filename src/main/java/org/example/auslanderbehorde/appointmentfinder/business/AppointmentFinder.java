package org.example.auslanderbehorde.appointmentfinder.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.business.FormFillerUtils;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.util.List;

import static org.example.auslanderbehorde.formfiller.enums.FormParameterEnum.TIME_SLOT;
import static org.example.notifications.Twilio.sendSMS;

public class AppointmentFinder {

    private final Logger logger = LogManager.getLogger(AppointmentFinder.class);

    public static int foundAppointmentCount = 0;

    private final WebDriver driver;

    public AppointmentFinder(WebDriver webDriver) {
        this.driver = webDriver;
    }

    public void handleFindingAppointment() throws ElementNotFoundException, InterruptedException, InteractionFailedException, IOException{
        String elementDescription = "DateSelection".toUpperCase();
        String cssSelector = "[data-handler=selectDay]";
        WebElement element = FormFillerUtils.getElementByCssSelector(cssSelector, elementDescription, driver);
        if(isDateVerified(element)){
            logger.info("Date is verified");
            FormFillerUtils.clickToElement(element, elementDescription);
            handleSelectingTimeslot();
        }
    }

    public void handleSelectingTimeslot() throws ElementNotFoundException, InterruptedException, InteractionFailedException, IOException {
        String elementId = TIME_SLOT.getId();
        String elementDescription = TIME_SLOT.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        if (isTimeslotOptionVerified(element)) {
            foundAppointmentCount++;
            FormFillerUtils.selectOptionByIndex(element, elementDescription, 0);
            Thread.sleep(1000);
            clickNextButton();
            String url = driver.getCurrentUrl();
            logger.info( String.format("Found a place. URL: %s", url));
            logger.info(String.format("Found Appointment count: %s", foundAppointmentCount));
            String myPhoneNumber = System.getenv("myPhoneNumber");
            sendSMS(myPhoneNumber, url);
            initDriverWithHead().get(url);
            FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "withHead");
            FormFillerUtils.saveScreenshot(driver, "withHead");
        } else {
            logger.info("Couldn't verify the timeslots");
        }
        Thread.sleep(1000);
    }

    protected void clickNextButton() throws InterruptedException, ElementNotFoundException, InteractionFailedException, IOException {
        String elementId = "applicationForm:managedForm:proceed";
        String elementDescription = "weiter button".toUpperCase();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
        Thread.sleep(3);
        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "afterClickNext");
        FormFillerUtils.saveScreenshot(driver, "afterClickNext");
    }

    protected boolean isDateVerified(WebElement element) {
        String dateMonth = element.getAttribute("data-month");
        String dateYear = element.getAttribute("data-year");
        String dateDay = element.getText();
        logger.info(String.format("Selected date: Day:%s Month:%s Year:%s", dateDay, dateMonth, dateYear));
        return dateDay != null && dateMonth != null && dateYear != null;
    }

    protected boolean isTimeslotOptionVerified(WebElement element) {
        Select select = new Select(element);
        List<WebElement> availableHours = select.getOptions();
        int availableHoursCount = availableHours.size();
        logger.info(String.format("There are %s available timeslots", availableHoursCount));
        for (int i = 0; i<availableHoursCount; i++) {
            logger.info(String.format("Timeslot: %s, Value: %s", i, availableHours.get(i).getText()));
        }
        if (availableHours.get(0).getText().contains("Bitte")) {
            logger.info("Failed to validate timeslots");
            return false;
        }
        return true;
    }

    private WebDriver initDriverWithHead() {
        logger.info("Initializing driver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-logging");
        return new ChromeDriver(options);
    }

}
