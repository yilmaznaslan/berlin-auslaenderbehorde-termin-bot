package com.yilmaznaslan.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yilmaznaslan.forms.VisaFormTO;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IoUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IoUtils.class);

    private IoUtils() {
    }

    /*
    public static PersonalInfoFormTO readPersonalInfoFromFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = PersonalInfoFormTO.class.getResourceAsStream("/DEFAULT_PERSONAL_INFO_FORM.json");
        return mapper.readValue(is, PersonalInfoFormTO.class);
    }

     */

    public static VisaFormTO readVisaInfoFromFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = VisaFormTO.class.getResourceAsStream("/DEFAULT_VISA_APPLICATION_FORM.json");
        return mapper.readValue(is, VisaFormTO.class);
    }

    public static void savePage(WebDriver driver, String pageDescriber) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String dateAsStr = dtf.format(now);
            String fileName = pageDescriber + "_" + dateAsStr;
            String pageSourceFileName = fileName + ".html";
            String screenshotFileName = fileName + ".png";
            LOGGER.info("File name :{}, {}", pageSourceFileName, screenshotFileName);

            LOGGER.info("Getting the page content");
            String content = driver.getPageSource();

            saveSourceCodeToFile(content, pageSourceFileName);
            saveScreenshot(driver, screenshotFileName);


        } catch (Exception e) {
            LOGGER.error("Saving has failed. Reason: ", e);
        }
    }

    private static void saveSourceCodeToFile(String content, String fileName) {
        LOGGER.info("Saving source code to file");
        File file = new File(fileName);

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(content);
        } catch (IOException e) {
            LOGGER.error("Failed to save source code to file", e);
        }

    }

    protected static void saveScreenshot(WebDriver driver, String fileName) throws IOException {
        LOGGER.info("Saving screenshot");
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
        ImageIO.write(screenshot.getImage(), "PNG", new File(fileName));
    }

}
