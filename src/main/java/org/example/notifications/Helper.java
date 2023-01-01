package org.example.notifications;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Main;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class Helper {
    public static final Logger logger = LogManager.getLogger(Helper.class);

    public static RemoteWebDriver initDriverHeadless() {
        logger.info("Initializing driver");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NONE);
        options.addArguments("--no-proxy-server");
        options.addArguments("--no-sandbox");
        options.addArguments("--enable-automation");
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

        String remoteUrl = "http://localhost:4444/wd/hub";
        RemoteWebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(remoteUrl), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        //String response = makeGetCall("http://127.0.0.1:" + freePort + "/json");
        //String webSocketUrl = response.substring(response.indexOf("ws://127.0.0.1"), response.length() - 4);
        //WebSocket socket = makeSocketConnection(webSocketUrl);
        //socket.send( "{\"id\":1,\"method\":\"Network.enable\"}" );
        logger.info("Driver is initialized for");
        return driver;
    }
}
