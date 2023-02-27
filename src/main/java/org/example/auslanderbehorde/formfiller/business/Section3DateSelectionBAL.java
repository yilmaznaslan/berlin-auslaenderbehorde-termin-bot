package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessEnum;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessResultEnum;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.util.List;

import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS;
import static org.example.auslanderbehorde.formfiller.business.FormFillerUtils.logInfo;
import static org.example.auslanderbehorde.formfiller.enums.FormParameterEnum.TIME_SLOT;

public class Section3DateSelectionBAL {

    private final Logger logger = LogManager.getLogger(Section3DateSelectionBAL.class);

    public static int foundAppointmentCount = 0;
    public static int handledAppointmentCount = 0;

    private RemoteWebDriver driver;

    public Section3DateSelectionBAL(RemoteWebDriver webDriver) {
        this.driver = webDriver;
    }

    public void fillAndSendForm() throws InteractionFailedException, ElementNotFoundTimeoutException, IOException, InterruptedException {
        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "section3");
        FormFillerUtils.saveScreenshot(driver, "section3");
        handleFindingDate();
        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "section3_aftersend");
        FormFillerUtils.saveScreenshot(driver, "section3_aftersend");
    }

    private void handleFindingDate() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException, IOException {
        logger.info("Starting to find an appointment date");
        handledAppointmentCount++;
        String elementDescription = "DateSelection".toUpperCase();
        String cssSelector = "[data-handler=selectDay]";
        FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "section3_dateSelection");
        FormFillerUtils.saveScreenshot(driver, "section3_dateSelection");
        WebElement element = FormFillerUtils.getElementByCssSelector(cssSelector, elementDescription, driver);
        if (isDateVerified(element)) {
            logger.info("Date is verified");
            element.click();
            logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, "Successful");
            handleSelectingTimeslotAndSendForm();
        }
    }

    private void handleSelectingTimeslotAndSendForm() throws ElementNotFoundTimeoutException, InterruptedException, InteractionFailedException {
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
            sendForm();
            Thread.sleep(5000);
            String url = driver.getCurrentUrl();
            logger.info(String.format("Found a place. URL: %s", url));
            logger.info(String.format("Found Appointment count: %s", foundAppointmentCount));

            int i = 0;
            url = driver.getCurrentUrl();
            logger.info(String.format("Found a place. URL: %s", url));
            FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "timeslot_" + i);
            FormFillerUtils.saveScreenshot(driver, "timeslot_" + i);
            Thread.sleep(100);

        }
    }

    private void sendForm() throws InterruptedException, ElementNotFoundTimeoutException, InteractionFailedException {
        String elementId = "applicationForm:managedForm:proceed";
        String elementDescription = "weiter button".toUpperCase();
        WebElement element = FormFillerUtils.getElementById(elementId, elementDescription, driver);
        FormFillerUtils.clickToElement(element, elementDescription);
    }

    private boolean isDateVerified(WebElement element) {
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

    public boolean isCalenderOpened() throws InteractionFailedException {
        String stageXPath = ".//ul/li[2]/span";
        String elementDescription = "activeSectionTab".toUpperCase();
        int i = 1;
        String stageText;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                WebElement element = FormFillerUtils.getElementByXPath(stageXPath, elementDescription, driver);
                stageText = element.getText();
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_TEXT, SeleniumProcessResultEnum.SUCCESSFUL.name(), String.format("Value: %s", stageText));
                if (stageText.equals("Terminauswahl")) {
                    return true;
                }

                if (isCalenderFound()){
                    return true;
                }

            } catch (StaleElementReferenceException | InterruptedException | ElementNotFoundTimeoutException e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_TEXT.firstName(), SeleniumProcessResultEnum.FAILED.firstName(), e);
            }
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logger.warn("Element: {}. Process: Getting by elementXPath. Result: Failed. Reason: Couldn't get the element within timeout", elementDescription);
            throw new InteractionFailedException(elementDescription);
        }
        return false;
    }

    public RemoteWebDriver getDriver() {
        return driver;
    }

    boolean isCalenderFound(){
        try {
            String asd = "//*[@id=\"xi-div-1\"]/div[3]";
            String elementDescription1 = "calender".toUpperCase();
            WebElement calender = FormFillerUtils.getElementByXPathCalender(asd, elementDescription1, driver);
            FormFillerUtils.saveSourceCodeToFile(driver.getPageSource(), "dateSelection_in");
            FormFillerUtils.saveScreenshot(driver, "dateSelection_in");
            return true;
            //return stageText.contains("Terminauswahl") && calender.isDisplayed();
        } catch (ElementNotFoundTimeoutException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
    }
}
