package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class FormFillerUtils {
    private static final Logger logger = LoggerFactory.getLogger(FormFiller.class);
    private static final int getElementTimeout = 10;
    private static final int SLEEP_DURATION_IN_MILISECONDS = 1000;

    public static WebElement getElementByName(String elementName, String elementDescription, WebDriver driver) throws InterruptedException {
        logger.info("Element: {}. Getting by elementName: {}", elementDescription.toUpperCase(Locale.ROOT), elementName);
        int i = 1;
        while (true) {
            try {
                WebElement element = driver.findElement(By.name(elementName));
                logger.info("Element: {}. Getting process successful", elementDescription.toUpperCase(Locale.ROOT));
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);

                return element;
            } catch (Exception e) {
                logger.warn("Element: {}. Getting process failed. Retrying ...", elementDescription.toUpperCase(Locale.ROOT) );
            }

            if (i >= getElementTimeout) {
                logger.warn("Element: {}. Getting faced timeout. Process can not continue", elementDescription.toUpperCase(Locale.ROOT));
                return null;
            }
            i++;
            Thread.sleep(1000);
        }
    }

    public static WebElement getElementByXPath(String xpath, String elementDescription, WebDriver driver) throws InterruptedException {
        logger.info("Element: {}. Getting by elementXPath: {}", elementDescription.toUpperCase(Locale.ROOT), xpath);
        int i = 0;
        while (true) {
            try {
                WebElement element = driver.findElement(By.xpath(xpath));
                logger.debug("Element: {}. Getting process successful", elementDescription.toUpperCase(Locale.ROOT));
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                return element;
            } catch (Exception e) {
                logger.warn("Element: {}. Getting process failed. Retrying ...", elementDescription.toUpperCase(Locale.ROOT));
            }
            if (i >= getElementTimeout) {
                logger.warn("Element: {}. Getting  faced timeout. Process can not continue", elementDescription.toUpperCase(Locale.ROOT));
                return null;
            }
            i++;
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
        }
    }

    public static void clickToElement(WebElement element, String elementName) throws InterruptedException {
        int i = 0;
        while (isElementInteractable(element, elementName)) {
            logger.info("Element: {}. Element is not yet intractable", elementName.toUpperCase());
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            if (i >= getElementTimeout) {
                logger.warn("Element: {}. Clicking faced timeout. Process can not continue", elementName.toUpperCase(Locale.ROOT));
            }
            i++;
        }
        logger.info("Element: {}. Element is intractable", elementName.toUpperCase(Locale.ROOT));
        element.click();
        Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
    }

    public static void selectOption(WebElement element, String elementName, String optionValue) throws InterruptedException {
        int i = 0;
        while (isElementInteractable(element, elementName)) {
            logger.info("Element: {} is not yet intractable", elementName.toUpperCase(Locale.ROOT));
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            if (i >= getElementTimeout) {
                logger.warn("Clicking the element:{} faced timeout. Process can not continue", elementName);
            }
            i++;
        }
        logger.info("Element: {} is intractable", elementName.toUpperCase(Locale.ROOT));
        Select select = new Select(element);
        i = 0;
        while (true) {
            try {
                select.selectByValue(optionValue);
                WebElement option = select.getFirstSelectedOption();
                String selectValue = option.getText();
                logger.debug("Selected the value: {}", selectValue);
                break;
            } catch (Exception e) {
                logger.warn("ElementName: {} is not selectable yet", elementName);
            }
            if (i >= getElementTimeout) {
                logger.warn("Clicking the element:{} faced timeout. Process can not continue", elementName);
            }
            i++;

        }
        Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
    }

    public static boolean isElementInteractable(WebElement element, String elementName) {
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
