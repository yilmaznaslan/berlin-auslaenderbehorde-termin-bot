package org.example.auslanderbehorde.formfiller.business;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessEnum;
import org.example.auslanderbehorde.formfiller.enums.SeleniumProcessResultEnum;
import org.example.auslanderbehorde.formfiller.exceptions.ElementNotFoundTimeoutException;
import org.example.auslanderbehorde.formfiller.exceptions.InteractionFailedException;
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
    static final int TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS = 25;
    static final int SLEEP_DURATION_IN_MILISECONDS = 1500;
    public static long formId;

    public static WebElement getElementById(String id, String elementDescription, WebDriver driver) throws InterruptedException, ElementNotFoundTimeoutException {
        WebElement element = null;
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                element = driver.findElement(By.id(id));
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_BY_ID, SeleniumProcessResultEnum.SUCCESSFUL.name());
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            }
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new ElementNotFoundTimeoutException(elementDescription);

        }
        return element;
    }

    public static WebElement getElementByXPath(String xpath, String elementDescription, WebDriver driver) throws InterruptedException, ElementNotFoundTimeoutException {
        WebElement element = null;
        int i = 0;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                element = driver.findElement(By.xpath(xpath));
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_BY_XPATH, SeleniumProcessResultEnum.SUCCESSFUL.name());
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {

            }
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_XPATH.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new ElementNotFoundTimeoutException(elementDescription);

        }
        return element;
    }

    public static WebElement getElementByCssSelector(String cssSelector, String elementDescription, WebDriver driver) throws InterruptedException, ElementNotFoundTimeoutException {
        WebElement element = null;
        int i = 0;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                element = driver.findElement(By.cssSelector(cssSelector));
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_BY_CSS_SELECTOR, SeleniumProcessResultEnum.SUCCESSFUL.name());
                Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
                break;
            } catch (Exception e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_CSS_SELECTOR.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            }
            Thread.sleep(SLEEP_DURATION_IN_MILISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_CSS_SELECTOR.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new ElementNotFoundTimeoutException(cssSelector);

        }
        return element;
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
        FileUtils.copyFile(scrFile1, new File(filePath + "/screenshot_" + suffix + dateAsStr + "_0.png"));

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("scroll(0, 1000);");
        FileUtils.copyFile(scrFile1, new File(filePath + "/screenshot_" + suffix + dateAsStr + "_1.png"));

    }

    public static void logInfo(String elementDescription, SeleniumProcessEnum process, String status) {
        ThreadContext.put("formId", String.valueOf(formId));
        ThreadContext.put("elementDescription", elementDescription);
        ThreadContext.put("seleniumProcess", process.name());
        ThreadContext.put("seleniumStatus", status);
        logger.info(String.format("Element: %s, Process:%s, Status:%s", elementDescription, process, status));
        //ThreadContext.clearAll();
    }

    public static void logInfo(String elementDescription, SeleniumProcessEnum process, String status, String msg) {
        ThreadContext.put("formId", String.valueOf(formId));
        ThreadContext.put("elementDescription", elementDescription);
        ThreadContext.put("seleniumProcess", process.name());
        ThreadContext.put("seleniumStatus", status);
        logger.info(String.format("Element: %s, Process:%s, Status:%s, Msg:%s", elementDescription, process, status, msg));
        ThreadContext.clearAll();
    }

    public static void logWarn(String elementDescription, String process, String status, Throwable throwable) {
        ThreadContext.put("formId", String.valueOf(formId));
        ThreadContext.put("elementDescription", elementDescription);
        ThreadContext.put("seleniumProcess", process);
        ThreadContext.put("seleniumStatus", status);
        logger.warn(String.format("Element: %s, Process:%s, Status:%s ", elementDescription, process, status), throwable);
        ThreadContext.clearAll();
    }

    public static void logWarn(String elementDescription, String process, String status, String msg) {
        ThreadContext.put("formId", String.valueOf(formId));
        ThreadContext.put("elementDescription", elementDescription);
        ThreadContext.put("seleniumProcess", process);
        ThreadContext.put("seleniumStatus", status);
        logger.warn(String.format("Element: %s, Process:%s, Status:%s Msg:%s", elementDescription, process, status, msg));
        ThreadContext.clearAll();
    }

    public static void logWarn(String elementDescription, String process, String status, String msg, Throwable e) {
        ThreadContext.put("formId", String.valueOf(formId));
        ThreadContext.put("elementDescription", elementDescription);
        ThreadContext.put("seleniumProcess", process);
        ThreadContext.put("seleniumStatus", status);
        logger.warn(String.format("Element: %s, Process:%s, Status:%s Msg:%s", elementDescription, process, status, msg), e);
        ThreadContext.clearAll();
    }
}
