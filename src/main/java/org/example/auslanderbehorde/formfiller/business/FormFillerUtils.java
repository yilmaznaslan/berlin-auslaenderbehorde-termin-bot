package org.example.auslanderbehorde.formfiller.business;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessEnum;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessResultEnum;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FormFillerUtils {
    private static final Logger logger = LogManager.getLogger(FormFillerUtils.class);
    static final int TIMEOUT_FOR_INTERACTING_IN_SECONDS = 25;
    static final int SLEEP_DURATION_IN_MILISECONDS = 1500;
    public static long formId;

    public static WebElement getElementById(String id, String elementDescription, WebDriver driver) throws InterruptedException, ElementNotFoundTimeoutException {
        WebElement element = null;
        int i = 1;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                element = driver.findElement(By.id(id));
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_BY_ID, SeleniumProcessResultEnum.SUCCESSFUL.name());
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.name(), SeleniumProcessResultEnum.FAILED.name(), e);
            }
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn("Element: {}. Process: Getting by elementId. Result: Failed. Reason: Couldn't get elementById within timeout", elementDescription);
            throw new ElementNotFoundTimeoutException(elementDescription);

        }
        return element;
    }

    public static WebElement getElementByName(String elementName, String elementDescription, WebDriver driver) throws InterruptedException, ElementNotFoundTimeoutException {
        WebElement element = null;
        int i = 1;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                element = driver.findElement(By.name(elementName));
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_BY_NAME, "Successful");
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logger.warn("Element: {}. Process: Getting by elementName. Result: Failed. Reason:{}", elementDescription, e.getMessage());
            }
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_NAME.name(), SeleniumProcessResultEnum.FAILED.name(), e);
            throw new ElementNotFoundTimeoutException(elementDescription);

        }
        return element;
    }

    public static WebElement getElementByXPath(String xpath, String elementDescription, WebDriver driver) throws InterruptedException, ElementNotFoundTimeoutException {
        WebElement element = null;
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                element = driver.findElement(By.xpath(xpath));
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_BY_XPATH, SeleniumProcessResultEnum.SUCCESSFUL.name());
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_XPATH.name(), SeleniumProcessResultEnum.FAILED.name(), e);
            }
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn("Element: {}. Process: Getting by elementXPath. Result: Failed. Reason: Couldn't get the element within timeout", elementDescription);
            throw new ElementNotFoundTimeoutException(elementDescription);

        }
        return element;
    }

    public static WebElement getElementByCssSelector(String cssSelector, String elementDescription, WebDriver driver) throws InterruptedException, ElementNotFoundTimeoutException {
        WebElement element = null;
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                element = driver.findElement(By.cssSelector(cssSelector));
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_BY_CSS_SELECTOR, SeleniumProcessResultEnum.SUCCESSFUL.name());
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_CSS_SELECTOR.name(), SeleniumProcessResultEnum.FAILED.name(), e);
            }
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logger.warn("Element: {}. Process: Getting by elementCssSelector. Result: Failed. Reason: Couldn't get within timeout", elementDescription);
            throw new ElementNotFoundTimeoutException(cssSelector);

        }
        return element;
    }

    public static void clickToElement(WebElement element, String elementDescription) throws InteractionFailedException {
        if (element == null) {
            logger.warn("Element:{} is null, Process: Click can not be continued", elementDescription);
            return;
        }
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                element.click();
                logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, "Successful");
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

    public static void selectOptionByValue(WebElement element, String elementDescription, String optionValue) {
        if (element == null) {
            logger.warn("Element:{} is null, Process: Select can not be continued", elementDescription);
            return;
        }
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                Select select = new Select(element);
                select.selectByValue(optionValue);
                WebElement option = select.getFirstSelectedOption();
                String selectValue = option.getText();
                logInfo(elementDescription, SeleniumProcessEnum.SELECTING_OPTION, "Successful", "value" + selectValue);
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

    public static void selectOptionByIndex(WebElement element, String elementDescription, int index) {
        if (element == null) {
            logger.warn("Element:{} is null, process can not be continued", elementDescription);
            return;
        }
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                Select select = new Select(element);
                List<WebElement> availableHours = select.getOptions();
                String selectValue = availableHours.get(index).getText();
                select.selectByIndex(index);
                logInfo(elementDescription, SeleniumProcessEnum.SELECTING_OPTION, SeleniumProcessResultEnum.SUCCESSFUL.name(), "Value: " + selectValue);
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

    public static void saveSourceCodeToFile(String content, String suffix) {
        String filePath = FormFillerBAL.class.getResource("/").getPath();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateAsStr = dtf.format(now);
        File newTextFile = new File(filePath + "/page_" + suffix + dateAsStr + ".html");

        FileWriter fw;
        try {
            fw = new FileWriter(newTextFile);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void saveScreenshot(WebDriver driver, String suffix) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateAsStr = dtf.format(now);
        File scrFile1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String filePath = FormFillerBAL.class.getResource("/").getPath();
        FileUtils.copyFile(scrFile1, new File(filePath + "/screenshot_" + suffix  + dateAsStr + "_0.png"));

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("scroll(0, 1000);");
        FileUtils.copyFile(scrFile1, new File(filePath + "/screenshot_" + suffix  + dateAsStr + "_1.png"));

    }

    public static void logInfo(String elementDescription, SeleniumProcessEnum process, String status) {
        ThreadContext.put("formId", String.valueOf(formId));
        ThreadContext.put("elementDescription", elementDescription);
        ThreadContext.put("seleniumProcess", process.name());
        ThreadContext.put("seleniumStatus", status);
        logger.info(elementDescription + " " + process + " " + status);
        //ThreadContext.clearAll();
    }

    public static void logInfo(String elementDescription, SeleniumProcessEnum process, String status, String msg) {
        ThreadContext.put("formId", String.valueOf(formId));
        ThreadContext.put("elementDescription", elementDescription);
        ThreadContext.put("seleniumProcess", process.name());
        ThreadContext.put("seleniumStatus", status);
        logger.info(elementDescription + " " + process + " " + status + msg);
        //ThreadContext.clearAll();
    }

    public static void logWarn(String elementDescription, String process, String status, Throwable e) {
        ThreadContext.put("formId", String.valueOf(formId));
        ThreadContext.put("elementDescription", elementDescription);
        ThreadContext.put("seleniumProcess", process);
        ThreadContext.put("seleniumStatus", status);
        logger.warn(elementDescription + " " + process + " " + status, e);
        //ThreadContext.clearAll();
    }
}
