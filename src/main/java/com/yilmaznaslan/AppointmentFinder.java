package com.yilmaznaslan;

import com.yilmaznaslan.formhandlers.Section1MainPageHandler;
import com.yilmaznaslan.formhandlers.Section2ServiceSelectionHandler;
import com.yilmaznaslan.formhandlers.Section3DateSelectionHandler;
import com.yilmaznaslan.forms.PersonalInfoFormTO;
import com.yilmaznaslan.forms.VisaFormTO;
import com.yilmaznaslan.notification.NotificationAdapter;
import com.yilmaznaslan.utils.DriverUtils;
import com.yilmaznaslan.utils.IoUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AppointmentFinder {

    public static final String FOREIGNERS_OFFICE_WEBSITE_HOMEPAGE = "https://otv.verwalt-berlin.de/ams/TerminBuchen?lang=en";
    public static final long FORM_REFRESH_PERIOD_IN_SECONDS = 5;
    public static final long TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS = 60;
    public static final long TIMEOUT_FOR_GETTING_HOME_PAGE_IN_SECONDS = 60;

    private static final Logger MyLogger = LoggerFactory.getLogger(AppointmentFinder.class);
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
                run(driver, MyLogger);
                if (executor.isShutdown()) {
                    future.complete(true);
                }
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, 0, FORM_REFRESH_PERIOD_IN_SECONDS, TimeUnit.SECONDS);

        MyLogger.info("Scheduled the task with delay: {} ", FORM_REFRESH_PERIOD_IN_SECONDS);
        return future;
    }

    private void run(RemoteWebDriver driver, Logger logger) {

        try {
            loadHomePage(driver, FOREIGNERS_OFFICE_WEBSITE_HOMEPAGE, logger);
            DriverUtils.waitUntilFinished(driver);
        } catch (TimeoutException e) {
            logger.error("TimeoutException occurred during getting the home page. Will try again");
            return;
        }

        try {
            Section1MainPageHandler section1MainPageHandler = new Section1MainPageHandler(driver);
            sessionUrl = section1MainPageHandler.fillAndSendForm();
            logger.info("SessionUrl: {}", sessionUrl);
            if (sessionUrl == null) {
                logger.warn("Couldn't capture sessionId, quitting.");
            }


            Section2ServiceSelectionHandler section2ServiceSelectionHandler = new Section2ServiceSelectionHandler(visaFormTO, personalInfoFormTO, driver);
            boolean result = section2ServiceSelectionHandler.fillAndSendForm();
            if (result) {
                Section3DateSelectionHandler section3DateSelectionHandler = new Section3DateSelectionHandler(driver);
                if (section3DateSelectionHandler.isDateAndTimeVerified()) {
                    logger.info("End of process");
                    notificationAdapter.triggerNotification("");
                    IoUtils.savePage(driver, "date_selection_success");
                    executor.shutdown();
                }
            }


        } catch (UnhandledAlertException e) {
            logger.error("UnhandledAlertException occurred during getting the home page.Clicking", e);
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (NoSuchSessionException e) {
            logger.error("NoSuchSessionException occurred during getting the home page. Creating a new Driver instance", e);
            driver = DriverUtils.initDriver();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted: ", e);
        } catch (Exception e) {
            logger.error("General Exception: ", e);
            IoUtils.savePage(driver, "exception_section2");
        }

    }

    private void loadHomePage(RemoteWebDriver driver, String url, Logger logger) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to {}", methodName);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_GETTING_HOME_PAGE_IN_SECONDS));
        wait.until(currentWebDriver -> {
            try {
                logger.info(String.format("Loading the URL: %s", url));
                currentWebDriver.get(url);
                return true;
            } catch (Exception e) {
                logger.error("Loading the home page failed. Reason: ", e);
                return false;
            }
        });

    }

    public String getSessionUrl() {
        return sessionUrl;
    }
}
