package org.example.auslanderbehorde.formfiller.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.sessionfinder.business.SessionFinder;
import org.example.auslanderbehorde.sessionfinder.model.SessionInfo;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class DriverManager {
    private final static Logger logger = LogManager.getLogger(TerminFinder.class);
    private static RemoteWebDriver driver;
    private String currentWindowHandle;

    public static RemoteWebDriver initDriverHeadless() {
        String seleniumDriverHost = System.getenv("SELENIUM_GRID_HOST");
        logger.info("Connecting to selenium host: {}", seleniumDriverHost);
        logger.info("Initializing driver");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NONE);
        options.addArguments("--no-proxy-server");
        options.addArguments("--no-sandbox");
        options.addArguments("--enable-automation");
        options.addArguments("--enableVNC");
        //options.addArguments("--headless");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--ignore-certificate-errors");
        //options.addArguments("--disable-gpu");
        //chromeOptions.addArguments("--disable-logging");
        //chromeOptions.addArguments("--disable-popup-blocking");
        //chromeOptions.addArguments("--headless");
        //int freePort = findFreePort();
        //chromeOptions.addArguments("--remote-debugging-port=" + freePort);
        String remoteUrl = "http://"+ seleniumDriverHost+":4444/wd/hub";
        try {
            driver = new RemoteWebDriver(new URL(remoteUrl), options);
            logger.info("Driver is initialized.");
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    private void getFormPage(String requestId, String dswid, String dsrid) {
        String hostUrl = "https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng";
        String targetUrl = hostUrl + "/" + requestId + "?dswid=" + dswid + "&dsrid=" + dsrid;
        logger.info(String.format("Getting the URL: %s", targetUrl));
        currentWindowHandle = driver.getWindowHandle();
        driver.get(targetUrl);
    }

}
