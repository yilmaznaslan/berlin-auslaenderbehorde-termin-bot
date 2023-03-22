package org.example.utils;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IoUtils {

    private final static Logger logger = LogManager.getLogger(IoUtils.class);

    public static PersonalInfoFormTO readPersonalInfoFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = PersonalInfoFormTO.class.getResourceAsStream("/DEFAULT_PERSONAL_INFO_FORM.json");
        try {
            return mapper.readValue(is, PersonalInfoFormTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static VisaFormTO readVisaInfoFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = VisaFormTO.class.getResourceAsStream("/DEFAULT_VISA_APPLICATION_FORM.json");
        try {
            return mapper.readValue(is, VisaFormTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void savePage(WebDriver driver, String pageDescriber, String suffix) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateAsStr = dtf.format(now);
        String fileName = pageDescriber + "_" + dateAsStr + "_" + suffix + ".html";
        String s3FileName = pageDescriber + "_" + dateAsStr + "_" + suffix + ".png";

        logger.info("File name :{}, {}", fileName, s3FileName);

        String content = driver.getPageSource();
        saveSourceCodeToFile(content, fileName);
        saveScreenshot(driver, s3FileName);
    }

    private static void saveSourceCodeToFile(String content, String fileName) {
        String bucketName = "auslander-termin-files";

        File file = new File(fileName);
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            logger.error("Failed to write to file", e);
        }
        try {
            AmazonS3 client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new EnvironmentVariableCredentialsProvider())
                    .withRegion(Regions.US_EAST_1)
                    .build();
            client.putObject(new PutObjectRequest(bucketName, fileName, file));
        } catch (Exception e) {
            logger.error("Error occurred during s3 operation");
        }
    }

    private static void saveScreenshot(WebDriver driver, String fileName) {
        File scrFile1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File file = null;
        try {
            //File file = new File(filePAth +"/screenshot_" + suffix + "_" + dateAsStr + ".png");
            file = new File(fileName);
            FileUtils.copyFile(scrFile1, file);
        } catch (IOException e) {
            logger.error("Failed to write to file", e);
        }

        String bucketName = "auslander-termin-files";
        try {
            AmazonS3 client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new EnvironmentVariableCredentialsProvider())
                    .withRegion(Regions.US_EAST_1)
                    .build();
            client.putObject(new PutObjectRequest(bucketName, fileName, file));
        } catch (Exception e) {
            logger.error("Error occurred during s3 operation");
        }


    }

}
