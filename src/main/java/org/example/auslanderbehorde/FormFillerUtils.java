package org.example.auslanderbehorde;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

    public static List<WebElement> getElementsByTagName(String tagName, WebDriver driver) throws InterruptedException, ElementNotFoundException {
        List<WebElement> webElements = new ArrayList<WebElement>();

        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                webElements = driver.findElements(By.tagName(tagName));
                logger.info("Element: {}. Process: Getting by elementXPath. Result: Successful", tagName);
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logger.warn("Element: {}. Process: Getting by elementXPath. Result: Failed", tagName);
            }
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn("Element: {}. Process: Getting by elementXPath. Result: Failed. Reason: Couldn't click within timeout", tagName);
            throw new ElementNotFoundException(tagName);

        }

        return webElements;
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

    public static void writeSourceCodeToFile(String content) {
        String filePath = FormFiller.class.getClassLoader().getResource("org/example/FormFiller/terminDateSelect.html").getPath();

        File newTextFile = new File(filePath);

        FileWriter fw;
        try {
            fw = new FileWriter(newTextFile);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
