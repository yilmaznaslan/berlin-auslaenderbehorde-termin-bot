package org.example;

import org.example.formhandlers.Section1MainPageHandler;
import org.example.formhandlers.Section2ServiceSelectionHandler;
import org.example.forms.PersonalInfoFormTO;
import org.example.forms.VisaFormTO;
import org.example.notification.NotificationAdapter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.example.utils.IoUtils.savePage;
import static org.example.utils.IoUtils.setAWSCredentials;

public class TerminFinder {

    public static final long FORM_REFRESH_PERIOD_IN_SECONDS = 5;
    public static final long TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS = 60;
    private static final Logger LOGGER = LoggerFactory.getLogger(TerminFinder.class);
    private final NotificationAdapter notificationAdapter;
    private final VisaFormTO visaFormTO;
    private final PersonalInfoFormTO personalInfoFormTO;
    private final RemoteWebDriver driver;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private String sessionUrl;

    public TerminFinder(final NotificationAdapter notificationAdapter,
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

        setAWSCredentials();

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
            Section1MainPageHandler section1MainPageHandler = new Section1MainPageHandler(driver);
            sessionUrl = section1MainPageHandler.fillAndSendForm();
            LOGGER.info("SessionUrl: {}", sessionUrl);
            if (sessionUrl == null) {
                LOGGER.warn("Couldn't capture sessionId, quitting.");
                return;
            }

        } catch (Exception e) {
            LOGGER.error("Exception occurred during getting the home page", e);
            savePage(driver, "exception_home_page");
            return;
        }

        try {
            Section2ServiceSelectionHandler section2ServiceSelectionHandler = new Section2ServiceSelectionHandler(visaFormTO, personalInfoFormTO, driver);
            boolean result = section2ServiceSelectionHandler.fillAndSendForm();
            if (result) {
                LOGGER.info("End of process");
                notificationAdapter.triggerNotification("");
                savePage(driver, "date_selection_success");
                executor.shutdown();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Reset the interrupted status
            LOGGER.error("Interrupted: ", e);
        } catch (Exception e) {
            LOGGER.error("General Exception: ", e);
            savePage(driver, "exception_section2");
        }

    }

    public String getSessionUrl() {
        return sessionUrl;
    }
}
