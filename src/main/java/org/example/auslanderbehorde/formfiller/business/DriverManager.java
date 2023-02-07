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
        String remoteUrl = "http://"+ seleniumDriverHost+":4444/wd/hub";
        try {
            RemoteWebDriver driver = new RemoteWebDriver(new URL(remoteUrl), options);
            logger.info("Driver is initialized.");
            return driver;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

}
