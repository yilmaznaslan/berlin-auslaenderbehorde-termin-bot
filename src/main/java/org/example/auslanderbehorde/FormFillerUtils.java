package org.example.auslanderbehorde;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FormFillerUtils {
    private static final Logger logger = LoggerFactory.getLogger(FormFiller.class);
    static final int TIMEOUT_FOR_INTERACTING_IN_SECONDS = 10;
    static final int SLEEP_DURATION_IN_MILISECONDS = 1500;

    public static WebElement getElementByName(String elementName, String elementDescription, WebDriver driver) throws InterruptedException, ElementNotFoundException {
        WebElement element = null;
        int i = 1;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                element = driver.findElement(By.name(elementName));
                logger.info("Element: {}. Process: Getting by elementName. Result: Successful", elementDescription);
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logger.warn("Element: {}. Process: Getting by elementName. Result: Failed. Reason:{}", elementDescription, e.getMessage());
            }
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn("Element: {}. Process: Getting by elementName. Result: Failed. Reason: Couldn't get elementByName within timeout", elementDescription);
            throw new ElementNotFoundException(elementDescription);

        }
        return element;
    }

    public static WebElement getElementByXPath(String xpath, String elementDescription, WebDriver driver) throws InterruptedException, ElementNotFoundException {
        WebElement element = null;
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                element = driver.findElement(By.xpath(xpath));
                logger.info("Element: {}. Process: Getting by elementXPath. Result: Successful", elementDescription);
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logger.warn("Element: {}. Process: Getting by elementXPath. Result: Failed", elementDescription);
            }
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn("Element: {}. Process: Getting by elementXPath. Result: Failed. Reason: Couldn't click within timeout", elementDescription);
            throw new ElementNotFoundException(elementDescription);

        }
        return element;
    }

    public static WebElement getElementByCssSelector(String cssSelector, String elementDescription, WebDriver driver) throws InterruptedException, ElementNotFoundException {
        WebElement element = null;
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                element = driver.findElement(By.cssSelector(cssSelector));
                logger.info("Element: {}. Process: Getting by elementCssSelector. Result: Successful", elementDescription);
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logger.warn("Element: {}. Process: Getting by elementXPath. Result: Failed", elementDescription);
            }
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn("Element: {}. Process: Getting by elementXPath. Result: Failed. Reason: Couldn't click within timeout", elementDescription);
            throw new ElementNotFoundException(cssSelector);

        }
        return element;
    }

    public static void clickToElement(WebElement element, String elementDescription) throws InteractionFailedException {
        if (element == null){
            logger.warn("Element:{} is null, Process: Click can not be continued", elementDescription);
            return;
        }
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                element.click();
                logger.info("Element: {}. Process: Click, Result: Successful ", elementDescription);
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logger.warn("Element: {}. Process: Click, Result: Failed. Exception: ", elementDescription, e);
            }
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn("Element: {}. Process: Click, Result: Failed Reason: Couldn't click within timeout", elementDescription);
            throw new InteractionFailedException("");
        }
    }

    public static void selectOption(WebElement element, String elementDescription, String optionValue) {
        if (element == null){
            logger.warn("Element:{} is null, Process: Select can not be continued");
            return;
        }
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                Select select = new Select(element);
                select.selectByValue(optionValue);
                WebElement option = select.getFirstSelectedOption();
                String selectValue = option.getText();
                logger.info("Element: {}, Process: Select, Result: Successful, Value:{}", elementDescription, selectValue);
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logger.warn("Element: {}. Process: Select, Result: Failed Reason:{}", elementDescription, e.getMessage());
            }
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn("Element: {}. Process: Select, Result: Failed Reason: Couldn't select within timeout", elementDescription);
        }
    }

    public static void selectOptionByIndex(WebElement element, String elementDescription) {
        if (element == null){
            logger.warn("Element:{} is null, process can not be continued");
            return;
        }
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                Select select = new Select(element);
                List<WebElement> availableHours = select.getOptions();
                int hoursSize = availableHours.size();
                logger.info("{} hours are found.", hoursSize);
                int targetHour = hoursSize - 1;
                select.selectByIndex(targetHour);
                String selectValue = availableHours.get(targetHour).getText();
                logger.info("Selected the value: {}.Select: {}", selectValue, select);
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logger.warn("Element: {}. Process: Select, Result: Failed Reason:{}", elementDescription, e.getMessage());
            }
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn("Element: {}. Process: Select, Result: Failed Reason: Couldn't select within timeout", elementDescription);
        }

    }

    public static void saveSourceCodeToFile(String content) {
        String filePath = FormFiller.class.getResource("/").getPath();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateAsStr = dtf.format(now);
        File newTextFile = new File(filePath+"/page_"+dateAsStr+".html");

        FileWriter fw;
        try {
            fw = new FileWriter(newTextFile);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void saveScreenshot(WebDriver driver) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateAsStr = dtf.format(now);
        File scrFile1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String filePath = FormFiller.class.getResource("/").getPath();
        FileUtils.copyFile(scrFile1, new File(filePath+"/screenshot_"+ dateAsStr +".png"));

    }
}
