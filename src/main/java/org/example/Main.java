package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auslanderbehorde.formfiller.business.FormFillerBAL;
import org.example.auslanderbehorde.formfiller.model.FormInputs;
import org.example.auslanderbehorde.formfiller.enums.EconomicActivityVisaDeEnum;
import org.example.auslanderbehorde.sessionfinder.business.SessionFinder;
import org.example.auslanderbehorde.sessionfinder.model.SessionInfo;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {


        List<FormInputs> formInputs = generateForms();

/*
        for (FormInputs formInput: formInputs) {
            SessionFinder sessionFinder = new SessionFinder();
            sessionFinder.findRequestId();
            String requestId = sessionFinder.getRequestId();
            String dswid = sessionFinder.getDswid();
            String dsrid = sessionFinder.getDsrid();
            FormFiller formFiller = new FormFiller(requestId, dswid, dsrid, formInput, new ChromeDriver(options));
            formFiller.startScanning();
        }
*/

        SessionFinder sessionFinder = new SessionFinder();
        SessionInfo sessionInfo = sessionFinder.findAndGetSession();
        Thread.sleep(5000);
        RemoteWebDriver remoteWebDriver = initDriverHeadless();
        FormFillerBAL formFillerBAL = new FormFillerBAL(new FormInputs("163", "1", "2", EconomicActivityVisaDeEnum.BLUECARD), sessionInfo , remoteWebDriver);
        formFillerBAL.startScanning();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutdown hook ran!");
            formFillerBAL.cancel();
            formFillerBAL.getDriver().quit();
            logger.info("quitted the driver");
            remoteWebDriver.quit();
        }
        ));

        ThreadMonitor threadMonitor = new ThreadMonitor();
        threadMonitor.startMonitoring();

        while(true){
            Thread.sleep(1000);
        }
    }

    public static List<FormInputs> generateForms(){
        List<FormInputs> result = new ArrayList<>();
        for (EconomicActivityVisaDeEnum ecoVisa: EconomicActivityVisaDeEnum.values()) {
                result.add(new FormInputs("163", "1", "2", ecoVisa));
        }
        return result;
    }

    private static RemoteWebDriver initDriverHeadless() {
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

    private static void  cleanDrivers(){

    }
}