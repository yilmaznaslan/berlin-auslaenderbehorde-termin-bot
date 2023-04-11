package org.example.utils;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IoUtils {

    private final static Logger logger = LoggerFactory.getLogger(IoUtils.class);
    private final static String S3_BUCKET_NAME = "auslander-termin-files";
    public static boolean isS3Enabled = false;
    public static boolean isLocalSaveEnabled = true;
    private static AmazonS3 client;

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

    public static void savePage(WebDriver driver, String pageDescriber, String suffix) {
        if (!isLocalSaveEnabled) {
            logger.info("Saving is disabled");
            return;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateAsStr = dtf.format(now);
        String fileName = pageDescriber + "_" + dateAsStr + "_" + suffix;
        String pagesourceFileName = fileName + ".html";
        String screenshotFileName = fileName + ".png";
        logger.info("File name :{}, {}", pagesourceFileName, screenshotFileName);

        String content;
        try {
            logger.info("Getting the page content");
            content = driver.getPageSource();

        } catch (Exception exception) {
            logger.error("Error occurred during getting the page source. Reason: ", exception);
            return;
        }


        File sourceFile;
        try {
            sourceFile = saveSourceCodeToFile(content, pagesourceFileName);
        } catch (IOException e) {
            logger.error("Error occurred during IO operation. Exception: ", e);
            return;
        }
        File screenShotFile;
        try {
            screenShotFile = saveScreenshot(driver, screenshotFileName);
        } catch (IOException e) {
            logger.error("Error occurred during IO operation. Exception: ", e);
            return;
        }

        if (isS3Enabled) {
            logger.info("Storing files to s3");
            client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new EnvironmentVariableCredentialsProvider())
                    .withRegion(Regions.US_EAST_1)
                    .build();

            try {
                client.putObject(new PutObjectRequest(S3_BUCKET_NAME, pagesourceFileName, sourceFile));
                client.putObject(new PutObjectRequest(S3_BUCKET_NAME, screenshotFileName, screenShotFile));
            } catch (Exception e) {
                logger.error("Error occurred during s3 operation. Exception: ", e);
            }
        } else {
            logger.info("S3 is not  enabled");
        }
    }

    private static File saveSourceCodeToFile(String content, String fileName) throws IOException {
        logger.info("Saving source code to file");
        File file = new File(fileName);
        FileWriter fw;
        fw = new FileWriter(file);
        fw.write(content);
        fw.close();
        return file;
    }

    private static File saveScreenshot(WebDriver driver, String fileName) throws IOException {
        logger.info("Saving screenshot");
        File scrFile1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File file = new File(fileName);
        FileUtils.copyFile(scrFile1, file);
        return file;
    }

}
