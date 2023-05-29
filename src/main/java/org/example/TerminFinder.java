package org.example;

import com.google.common.annotations.VisibleForTesting;
import org.example.enums.MdcVariableEnum;
import org.example.exceptions.FormValidationFailed;
import org.example.formhandlers.*;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.example.Config.FORM_REFRESH_PERIOD_IN_SECONDS;
import static org.example.Config.TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS;
import static org.example.formhandlers.Section3DateSelectionHandler.handledDateCount;
import static org.example.utils.DriverUtils.initDriver;
import static org.example.utils.IoUtils.*;

public class TerminFinder {

    public static int searchCount = 0;
    private final UUID id;
    private final Logger logger = LoggerFactory.getLogger(TerminFinder.class);
    private final VisaFormTO visaFormTO;
    private final PersonalInfoFormTO personalInfoFormTO;
    private final Timer timer = new Timer(true);
    private RemoteWebDriver driver;

    public TerminFinder(UUID id, PersonalInfoFormTO personalInfoFormTO, VisaFormTO visaFormTO) {
        this.id = id;
        this.personalInfoFormTO = personalInfoFormTO;
        this.visaFormTO = visaFormTO;
    }

    public TerminFinder(UUID id, PersonalInfoFormTO personalInfoFormTO, VisaFormTO visaFormTO, RemoteWebDriver driver) {
        this.id = id;
        this.visaFormTO = visaFormTO;
        this.personalInfoFormTO = personalInfoFormTO;
        this.driver = driver;
    }

    public void startScanning() throws FormValidationFailed {
        setAWSCredentials();
        setMDCVariables();
        Config.getPropValues();

        // Section 0
        if (isResidenceTitleInfoVerified(visaFormTO)) {
            logger.info("Successfully validated form: {}", visaFormTO);
        } else {
            logger.error("Failed validate form: {}", visaFormTO);
            throw new FormValidationFailed("");
        }

        if (driver == null) {
            driver = initDriver();
        }

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(this::run, 0, FORM_REFRESH_PERIOD_IN_SECONDS, TimeUnit.SECONDS);
        logger.info("Scheduled the task at rate: {} ", FORM_REFRESH_PERIOD_IN_SECONDS);

    }

    protected void run() {

        try {
            setMDCVariables();
            getHomePage();

            Section1MainPageHandler section1MainPageHandler = new Section1MainPageHandler(driver);
            Section2ServiceSelectionHandler section2ServiceSelectionHandler = new Section2ServiceSelectionHandler(visaFormTO, personalInfoFormTO, driver);
            Section3DateSelectionHandler section3DateSelectionHandler = new Section3DateSelectionHandler(driver);
            Section4VisaFormHandler section4VisaFormHandler = new Section4VisaFormHandler(personalInfoFormTO, visaFormTO, driver);
            Section5ReservationHandler section5ReservationHandler = new Section5ReservationHandler(driver);

            if (!fillAndSendFormWithExceptionHandling(section1MainPageHandler)) return;
            if (!fillAndSendFormWithExceptionHandling(section2ServiceSelectionHandler)) return;
            if (!fillAndSendFormWithExceptionHandling(section3DateSelectionHandler)) return;
            if (!fillAndSendFormWithExceptionHandling(section4VisaFormHandler)) return;
            if (!fillAndSendFormWithExceptionHandling(section5ReservationHandler)) return;

            logger.info("End of process");
            driver.quit();
            timer.cancel();
        } catch (Exception e) {
            logger.error("Exception occurred during the process, quitting.", e);
            String fileName = getClass().getSimpleName();
            savePage(driver, fileName, "exception");
            logger.info("page is saved");
            try {
                driver = initDriver();
            } catch (Exception e2) {
                logger.error("Exception occurred during the process, driver initializing", e2);
            }
        } finally {
            MDC.remove(MdcVariableEnum.elementDescription.name());
        }

    }

    boolean fillAndSendFormWithExceptionHandling(IFormHandler formHandler) throws FormValidationFailed, InterruptedException {
        Boolean isSuccessful = formHandler.fillAndSendForm();
        logger.info(String.format("Form :%s, Form handling result: %s", formHandler.getClass().getSimpleName(), isSuccessful));
        driver = formHandler.getDriver();
        logger.info("Calender page is not opened. Search count: {}. " +
                        "SearchCountWithCalenderOpened: {}. " +
                        "Handled date: {} ",
                searchCount,
                searchCountWithCalenderOpened,
                handledDateCount);
        return isSuccessful;
    }


    @VisibleForTesting
    protected void getHomePage() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("Starting to {}", methodName);


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(webDriver -> {
            try {
                // Create a new tab
                logger.info("Switching to a new tab");
                webDriver.switchTo().newWindow(WindowType.TAB);
                String currentWindowHandle = webDriver.getWindowHandle();
                Set<String> handle = webDriver.getWindowHandles();
                Thread.sleep(1000);

                // Closing the first tab
                webDriver.switchTo().window(handle.stream().collect(Collectors.toList()).get(0)).close();
                Thread.sleep(1000);

                // Switching to the second tab
                logger.info(String.format("Switching to window handle: %s", currentWindowHandle));
                webDriver.switchTo().window(currentWindowHandle);

                // Getting the home page
                String url = "https://otv.verwalt-berlin.de/ams/TerminBuchen?lang=en";
                logger.info(String.format("Getting the URL: %s", url));
                driver.get(url);
                return true;
            } catch (Exception e) {
                return false;
            }

        });

        String url = "https://otv.verwalt-berlin.de/ams/TerminBuchen?lang=en";
        logger.info(String.format("Getting the URL: %s", url));
/*
        String urlBefore = driver.getCurrentUrl();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_FOR_INTERACTING_WITH_ELEMENT_IN_SECONDS));
        wait.until(__ -> {
            try {
                driver.get(url);
                String urlAfter = driver.getCurrentUrl();
                return !urlBefore.equals(urlAfter);
            } catch (Exception e) {
                return false;
            }
        });


 */

    }

    private void setMDCVariables() {
        MDC.put("visaForm", visaFormTO.toString());
        MDC.put("id", id.toString());
    }

    private boolean isResidenceTitleInfoVerified(VisaFormTO visaFormTO) {
        logger.info("Verifying form: {}", visaFormTO);
        String serviceType = visaFormTO.getServiceType();
        Boolean isResidencePermitPresent = visaFormTO.getResidencePermitPresent();
        String residencePermitId = visaFormTO.getResidencePermitId();

        if (serviceType.equals("Apply for a residence title")) {
            if (isResidencePermitPresent == null) {
                return false;
            }

            if (isResidencePermitPresent && residencePermitId == null) {
                return false;
            }

            if (!isResidencePermitPresent && residencePermitId != null) {
                return false;
            }
        }

        if (serviceType.equals("Extend a residence title")) {

            return residencePermitId != null;
        }

        return true;
    }

}
