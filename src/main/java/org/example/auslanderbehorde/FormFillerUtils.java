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
import java.util.List;

public class FormFillerUtils {
    private static final Logger logger = LoggerFactory.getLogger(FormFiller.class);
    private static final int TIMEOUT_FOR_INTERACTING_IN_SECONDS = 10;
    private static final int SLEEP_DURATION_IN_MILISECONDS = 500;

    public static WebElement getElementByName(String elementName, String elementDescription, WebDriver driver) throws InterruptedException {
        WebElement element = null;
        int i = 1;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                element = driver.findElement(By.name(elementName));
                logger.info("Element: {}. Process: Getting by elementName. Result: Successfully", elementDescription);
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logger.warn("Element: {}. Process: Getting by elementName. Result: Failed. Reason:{}", elementDescription, e.getMessage());
            }
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn("Element: {}. Process: Getting by elementNAme. Result: Failed. Reason: Couldn't click within timeout", elementDescription);
        }
        return element;
    }

    public static WebElement getElementByXPath(String xpath, String elementDescription, WebDriver driver) throws InterruptedException {
        WebElement element = null;
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                element = driver.findElement(By.xpath(xpath));
                logger.info("Element: {}. Process: Getting by elementXPath. Result: Successfully", elementDescription);
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logger.warn("Element: {}. Process: Getting by elementXPath. Result: Failed. Reason:{}", elementDescription, e.getMessage());
            }
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn("Element: {}. Process: Getting by elementXPath. Result: Failed. Reason: Couldn't click within timeout", elementDescription);
        }
        return element;
    }

    public static void clickToElement(WebElement element, String elementDescription) {
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                element.click();
                logger.info("Element: {}. Process: Click, Result: Successfully ", elementDescription);
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logger.warn("Element: {}. Process: Click, Result: Failed Reason:{}", elementDescription, e.getMessage());
            }
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn("Element: {}. Process: Click, Result: Failed Reason: Couldn't click within timeout", elementDescription);
        }
    }

    public static void selectOption(WebElement element, String elementDescription, String optionValue) {
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                Select select = new Select(element);
                select.selectByValue(optionValue);
                WebElement option = select.getFirstSelectedOption();
                String selectValue = option.getText();
                logger.debug("Selected the value: {}", selectValue);
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

    private static boolean isElementInteractable(WebElement element, String elementName) {
        boolean isElementDisplayed = element.isDisplayed();
        boolean isElementEnabled = element.isEnabled();

        logger.debug("Element: {} is enabled: {}", elementName, isElementEnabled);
        logger.debug("Element: {} displayed: {}", elementName, isElementDisplayed);

        return !(isElementDisplayed & isElementEnabled);
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
