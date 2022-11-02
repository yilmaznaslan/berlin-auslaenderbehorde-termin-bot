package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class makes calls to the auslender behorde in interval to catch certain information that is required to fill the form
 */
public class SessionCatcher {
    private static String dswid;
    private static String dsrid;
    private static String requestId;
    private static String usefulUrl;
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);


    public static void getServiceSelectionTab(String url){
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless");

        //load default profile
        WebDriver driver = new ChromeDriver(options);
        LOGGER.info("Getting the URL: {}", url);

        driver.get(url);
        driver.navigate().to(url);
    }

    public static String  getFormPage() {
        System.setProperty("webdriver.chrome.driver","chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);


        String initialUrl = "https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng";
        LOGGER.info("Getting the URL: {}", initialUrl);

        driver.get(initialUrl);
        while (true) {
            String urlAfterRedirect = driver.getCurrentUrl();
            LOGGER.info("CurrentURL: " + urlAfterRedirect);
            try {
                URL url = new URL(urlAfterRedirect);
                String queryStr = url.getQuery();
                if (dsrid == null && dswid == null) {
                    LOGGER.info("QueryString: {}", queryStr);
                    setDs(queryStr);
                }

                if (urlAfterRedirect.contains("?v")){
                    setRequestId(urlAfterRedirect);
                    driver.close();
                    break;
                }


            } catch (MalformedURLException e) {
                LOGGER.error("URL is malformed exception occurred", e);
            }
        }

        return SessionCatcher.requestId;
    }

    private static void setDs(String queryStr){
        List<String> queryStrings = List.of(queryStr.split("&"));
        SessionCatcher.dsrid = Arrays.stream(queryStrings.get(0).split("=")).toList().get(1);
        SessionCatcher.dswid = Arrays.stream(queryStrings.get(1).split("=")).toList().get(1);
        LOGGER.info("dswid: {}, dsrid: {}", dswid, dsrid);
    }

    private static void setRequestId(String url){
        List<String> urlAsList = List.of(url.split("/"));
         String requestIdAndV = urlAsList.get(urlAsList.size() - 1);

         String requestId = List.of(requestIdAndV.split("\\?")).get(0);

        //SessionCatcher.dsrid = Arrays.stream(queryStrings.get(0).split("=")).toList().get(1);
        //SessionCatcher.dswid = Arrays.stream(queryStrings.get(1).split("=")).toList().get(1);
        LOGGER.info("RequestID: {}",requestId);
        SessionCatcher.requestId = requestId;
        //LOGGER.info("RequestId: {}, v: {}", dswid, dsrid);
    }



}