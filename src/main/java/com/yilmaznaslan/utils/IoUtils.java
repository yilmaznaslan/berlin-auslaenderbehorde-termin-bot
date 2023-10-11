package com.yilmaznaslan.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yilmaznaslan.forms.PersonalInfoFormTO;
import com.yilmaznaslan.forms.VisaFormTO;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IoUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(IoUtils.class);

    private IoUtils() {
    }

    public static PersonalInfoFormTO readPersonalInfoFromFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = PersonalInfoFormTO.class.getResourceAsStream("/DEFAULT_PERSONAL_INFO_FORM.json");
        return mapper.readValue(is, PersonalInfoFormTO.class);
    }

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

            String content;
            try {
                LOGGER.info("Getting the page content");
                content = driver.getPageSource();

            } catch (Exception exception) {
                LOGGER.error("Error occurred during getting the page source. Reason: ", exception);
                return;
            }

            saveSourceCodeToFile(content, pageSourceFileName);
            saveScreenshot(driver, screenshotFileName);


        } catch (Exception e) {
            LOGGER.error("Saving has failed. Reason: ", e);
        }
    }

    private static void saveSourceCodeToFile(String content, String fileName) throws IOException {
        LOGGER.info("Saving source code to file");
        File file = new File(fileName);
        FileWriter fw;
        fw = new FileWriter(file);
        fw.write(content);
        fw.close();
    }

    private static void saveScreenshot(WebDriver driver, String fileName) throws IOException {
        LOGGER.info("Saving screenshot");
        File scrFile1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File(fileName);

        try (FileInputStream srcStream = new FileInputStream(scrFile1);
             FileOutputStream destStream = new FileOutputStream(destFile);
             FileChannel srcChannel = srcStream.getChannel();
             FileChannel destChannel = destStream.getChannel()) {

            destChannel.transferFrom(srcChannel, 0, srcChannel.size());
        }
    }

}
