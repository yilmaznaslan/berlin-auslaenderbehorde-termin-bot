package org.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.SeleniumProcessEnum;
import org.example.enums.SeleniumProcessResultEnum;
import org.example.exceptions.ElementNotFoundTimeoutException;
import org.example.exceptions.InteractionFailedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import static org.example.utils.LogUtils.logInfo;
import static org.example.utils.LogUtils.logWarn;

public class DriverUtils {
    private static final Logger logger = LogManager.getLogger(DriverUtils.class);
    public static final int TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS = 25;
    public static final int TIMEOUT_FOR_GETTING_CALENDER_ELEMENT_IN_SECONDS = 5;

    static final int TIMEOUT_FOR_INTERACTING_IN_SECONDS = 25;
    public static final int SLEEP_DURATION_IN_MILLISECONDS = 1500;
    public static long formId;

    public static RemoteWebDriver initDriverHeadless() {
        String seleniumDriverHost = System.getenv().getOrDefault("SELENIUM_GRID_HOST", "localhost");
        ChromeOptions options = new ChromeOptions();

        // Add options to make Selenium-driven browser look more like a regular user's browser
        options.addArguments("--disable-blink-features=AutomationControlled"); // Remove "navigator.webdriver" flag
        options.addArguments("--disable-infobars"); // Disable infobars
        options.addArguments("--start-maximized"); // Start the browser maximized
        options.addArguments("--disable-extensions"); // Disable extensions

        String remoteUrl = "http://" + seleniumDriverHost + ":4444/wd/hub";
        try {
            RemoteWebDriver driver = new RemoteWebDriver(new URL(remoteUrl), options);
            logger.info("Driver is initialized.");
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    public static WebElement getElementById(String id, String elementDescription, WebDriver driver) throws InterruptedException, ElementNotFoundTimeoutException {
        WebElement element = null;
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                element = driver.findElement(By.id(id));
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_BY_ID, SeleniumProcessResultEnum.SUCCESSFUL.name());
                Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
                break;
            } catch (Exception e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.firstName(), SeleniumProcessResultEnum.FAILED.firstName(), "");
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new ElementNotFoundTimeoutException(elementDescription);
        }
        return element;
    }

    public static WebElement getElementByTagName(String tagName, String elementDescription, WebDriver driver) throws InterruptedException, ElementNotFoundTimeoutException {
        WebElement element = null;
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                element = driver.findElements(By.tagName("input")).stream().filter(element1 -> element1.getAttribute("type").equals("checkbox")).collect(Collectors.toList()).get(0);
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_BY_ID, SeleniumProcessResultEnum.SUCCESSFUL.name());
                Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
                break;
            } catch (Exception e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.firstName(), SeleniumProcessResultEnum.FAILED.firstName(), "");
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new ElementNotFoundTimeoutException(elementDescription);
        }
        return element;
    }

    public static WebElement getElementByLabelValue(String labelValue, String elementDescription, WebDriver driver) throws InterruptedException, ElementNotFoundTimeoutException {
        WebElement element = null;
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                element = driver.findElements(By.tagName("label")).stream().filter(webElement -> webElement.getText().equals(labelValue)).collect(Collectors.toList()).get(0);
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_BY_ID, SeleniumProcessResultEnum.SUCCESSFUL.name());
                Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
                break;
            } catch (Exception e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.firstName(), SeleniumProcessResultEnum.FAILED.firstName(), "");
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_TEXT.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new ElementNotFoundTimeoutException(elementDescription);
        }
        return element;
    }

    public static WebElement getElementByTextValue(String textValue, String elementDescription, WebDriver driver) throws InterruptedException, ElementNotFoundTimeoutException {
        WebElement element = null;
        int i = 1;
        while (i <= TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            try {
                element = driver.findElement(By.linkText(textValue));
                logInfo(elementDescription, SeleniumProcessEnum.GETTING_BY_ID, SeleniumProcessResultEnum.SUCCESSFUL.name());
                Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
                break;
            } catch (Exception e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_ID.firstName(), SeleniumProcessResultEnum.FAILED.firstName(), "");
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_TEXT.name(), SeleniumProcessResultEnum.FAILED.name(), "");
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
                Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
                break;
            } catch (Exception e) {

            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
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
                Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
                break;
            } catch (Exception e) {
                //logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_CSS_SELECTOR.firstName(), SeleniumProcessResultEnum.FAILED.firstName(), "");
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_GETTING_ELEMENT_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.GETTING_BY_CSS_SELECTOR.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new ElementNotFoundTimeoutException(cssSelector);

        }
        return element;
    }

    public static void clickToElement(WebElement element, String elementDescription) throws InteractionFailedException, InterruptedException {
        if (element == null) {
            logger.warn("Element:{} is null, Process: Click can not be continued", elementDescription);
            return;
        }
        int i = 0;
        while (i <= TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            try {
                element.click();
                logInfo(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT, "Successful");
                break;
            } catch (Exception e) {
                //logger.warn("Element: {}. Process: Click, Result: Failed. Exception: ", elementDescription, e);
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.CLICKING_TO_ELEMENT.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new InteractionFailedException("");
        }
    }

    public static void selectOptionByValue(WebElement element, String elementDescription, String optionValue) throws InteractionFailedException, InterruptedException {
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
                break;
            } catch (Exception e) {
                //logger.warn("Element: {}. Process: Select, Result: Failed Reason:{}", elementDescription, e.getMessage());
            }
            Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
            i++;
        }
        if (i > TIMEOUT_FOR_INTERACTING_IN_SECONDS) {
            logWarn(elementDescription, SeleniumProcessEnum.SELECTING_OPTION.name(), SeleniumProcessResultEnum.FAILED.name(), "");
            throw new InteractionFailedException("");
        }
    }

}
