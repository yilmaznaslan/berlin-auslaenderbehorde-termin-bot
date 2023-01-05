package org.example.auslanderbehorde.appointmentfinder.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.business.FormFillerUtils;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessEnum;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessResultEnum;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.util.List;

import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.logInfo;
import static org.example.auslanderbehorde.formfiller.enums.FormParameterEnum.TIME_SLOT;
import static org.example.notifications.TwilioAdapter.makeCall;
import static org.example.notifications.TwilioAdapter.sendSMS;

public class AppointmentFinder {

    private final Logger logger = LogManager.getLogger(AppointmentFinder.class);

    public static int foundAppointmentCount = 0;
    public static int handledAppointmentCount = 0;

    private final WebDriver driver;

    public AppointmentFinder(WebDriver webDriver) {
        this.driver = webDriver;
    }

    public void handleFindingAppointment() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException, IOException{
        handledAppointmentCount++;
        String elementDescription = "DateSelection".toUpperCase();
        String cssSelector = "[data-handler=selectDay]";
        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "dateSelection");
        FormFillerUtils.saveScreenshot(driver, "dateSelection");
        WebElement element = FormFillerUtils.getElementByCssSelector(cssSelector, elementDescription, driver);
        if(isDateVerified(element)){
            logger.info("Date is verified");
            element.click();
            logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, "Successful");
            handleSelectingTimeslot();
        }
    }

    public void handleSelectingTimeslot() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException, IOException {
        String elementId = TIME_SLOT.getId();
        String elementDescription = TIME_SLOT.name();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        if (isTimeslotOptionVerified(element)) {
            foundAppointmentCount++;
            Select select = new Select(element);
            List<WebElement> availableHours = select.getOptions();
            String selectValue = availableHours.get(0).getText();
            select.selectByIndex(0);
            logInfo(elementDescription, SeleniumProcessEnum.SELECTING_OPTION, SeleniumProcessResultEnum.SUCCESSFUL.name(), "Value: " + selectValue);
            Thread.sleep(1000);
            clickNextButton();
            Thread.sleep(5000);
            String url = driver.getCurrentUrl();
            logger.info( String.format("Found a place. URL: %s", url));
            logger.info(String.format("Found Appointment count: %s", foundAppointmentCount));
//            String myPhoneNumber = System.getenv("myPhoneNumber");
//            makeCall(myPhoneNumber);
//            sendSMS(myPhoneNumber, url);
            int i = 0;
            while(i<1){
                url = driver.getCurrentUrl();
                logger.info( String.format("Found a place. URL: %s", url));
                FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "timeslot_"+i);
                FormFillerUtils.saveScreenshot(driver, "timeslot_"+i);
                i = i +1;
                Thread.sleep(100);
            }

        }
    }

    protected void clickNextButton() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = "applicationForm:managedForm:proceed";
        String elementDescription = "weiter button".toUpperCase();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
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
