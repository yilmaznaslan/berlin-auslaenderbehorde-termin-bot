package com.yilmaznaslan;

import com.yilmaznaslan.enums.MdcVariableEnum;
import com.yilmaznaslan.formhandlers.Section1MainPageHandler;
import com.yilmaznaslan.formhandlers.Section2ServiceSelectionHandler;
import com.yilmaznaslan.formhandlers.Section3DateSelectionHandler;
import com.yilmaznaslan.forms.PersonalInfoFormTO;
import com.yilmaznaslan.forms.VisaFormTO;
import com.yilmaznaslan.notification.NotificationAdapter;
import com.yilmaznaslan.utils.DriverUtils;
import com.yilmaznaslan.utils.IoUtils;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AppointmentFinder {

    public static final long FORM_REFRESH_PERIOD_IN_SECONDS = 5;
    public static final long TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS = 60;

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentFinder.class);
    private final NotificationAdapter notificationAdapter;
    private final VisaFormTO visaFormTO;
    private final PersonalInfoFormTO personalInfoFormTO;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private RemoteWebDriver driver;
    private String sessionUrl;

    public AppointmentFinder(final NotificationAdapter notificationAdapter,
                             final PersonalInfoFormTO personalInfoFormTO,
                             final VisaFormTO visaFormTO,
                             final RemoteWebDriver driver) {
        this.notificationAdapter = notificationAdapter;
        this.visaFormTO = visaFormTO;
        this.personalInfoFormTO = personalInfoFormTO;
        this.driver = driver;
    }

    public CompletableFuture<Boolean> startScanning() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        executor.scheduleWithFixedDelay(() -> {
            try {
                run();
                if (executor.isShutdown()) {
                    future.complete(true);
                }
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, 0, FORM_REFRESH_PERIOD_IN_SECONDS, TimeUnit.SECONDS);

        LOGGER.info("Scheduled the task with delay: {} ", FORM_REFRESH_PERIOD_IN_SECONDS);
        return future;
    }

    private void run() {

        try {
            getHomePage();
            DriverUtils.waitUntilFinished(driver);
          
            Section1MainPageHandler section1MainPageHandler = new Section1MainPageHandler(driver);
            sessionUrl = section1MainPageHandler.fillAndSendForm();
            LOGGER.info("SessionUrl: {}", sessionUrl);
            if (sessionUrl == null) {
                LOGGER.warn("Couldn't capture sessionId, quitting.");
                return;
            }
            MDC.put(MdcVariableEnum.sessionUrl.name(), sessionUrl);


            Section2ServiceSelectionHandler section2ServiceSelectionHandler = new Section2ServiceSelectionHandler(visaFormTO, personalInfoFormTO, driver);
            boolean result = section2ServiceSelectionHandler.fillAndSendForm();
            if (result) {
                Section3DateSelectionHandler section3DateSelectionHandler = new Section3DateSelectionHandler(driver);
                if (section3DateSelectionHandler.isDateAndTimeVerified()) {
                    LOGGER.info("End of process");
                    notificationAdapter.triggerNotification("");
                    IoUtils.savePage(driver, "date_selection_success");
                    executor.shutdown();
                }
            }

        } catch (Exception e) {
            LOGGER.error("Failed to finish task", e);
            driver = DriverUtils.initDriver();
        }

    }

    /**
     * DO NOT CHANGE THIS METHOD. TAB SWITCHING IS DONE HERE !!
     * OTHERWISE, IT STUCKS IN THE FIRST TAB SOMETIMES
     */
    private void getHomePage() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info("Starting to {}", methodName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(webDriver -> {
            try {
                LOGGER.info("Switching to a new tab");
                // create a new tab
                webDriver.switchTo().newWindow(WindowType.TAB);
                ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());

                // Closing the first tab
                webDriver.switchTo().window(tabs.get(0)).close();

                // Switching to the second tab
                webDriver.switchTo().window(tabs.get(1));

                // Getting the home page
                String url = "https://otv.verwalt-berlin.de/ams/TerminBuchen?lang=en";
                LOGGER.info(String.format("Getting the URL: %s", url));
                webDriver.get(url);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public String getSessionUrl() {
        return sessionUrl;
    }
}
