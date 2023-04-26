package org.example.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.example.model.PersonalInfoFormTO;
import org.example.model.VisaFormTO;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IoUtils {

    private final static Logger logger = LoggerFactory.getLogger(IoUtils.class);
    private final static String S3_BUCKET_NAME = "auslander-termin-files";
    private final static String secretName = "iam-credentials-for-termin-bot";
    private static final String AWS_REGION = "eu-central-1";
    private static final AmazonCloudWatchClientBuilder amazonCloudWatchClientBuilder = AmazonCloudWatchClient.builder()
            .withRegion(Regions.fromName(AWS_REGION));
    public static String CLOUDWATCH_METRIC_FOR_CALENDER_OPENED = "calender opened";
    public static String CLOUDWATCH_METRIC_FOR_VERIFIED_TIMESLOT = "verified timeslot";
    public static String CLOUDWATCH_METRIC_FOR_RESERVATION_COMPLETED = "reservation completed";
    public static boolean isS3Enabled = false;
    public static String CLOUDWATCH_METRIC_NAMESPACE = "termin-bot";
    public static boolean isLocalSaveEnabled = true;
    private static AmazonS3 client;
    public static boolean isCloudwatchEnabled = true;
    private static String AWS_ACCESS_KEY_ID;
    private static String AWS_SECRET_ACCESS_KEY;
    private static BasicAWSCredentials awsCreds;

    private IoUtils() {
    }

    public static void setAWSCredentials() {
        if (AWS_ACCESS_KEY_ID == null || AWS_SECRET_ACCESS_KEY == null) {
            SecretsManagerClient client = SecretsManagerClient.builder()
                    .region(Region.of(AWS_REGION))
                    .build();
            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();
            GetSecretValueResponse getSecretValueResponse;
            try {
                getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
                String secret = getSecretValueResponse.secretString();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode actualObj = mapper.readTree(secret);
                AWS_ACCESS_KEY_ID = actualObj.findValue("AWS_ACCESS_KEY_ID").textValue();
                AWS_SECRET_ACCESS_KEY = actualObj.findValue("AWS_SECRET_ACCESS_KEY").textValue();
                awsCreds = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
                amazonCloudWatchClientBuilder.withCredentials(new AWSStaticCredentialsProvider(awsCreds));

                logger.info("Successfully retrieved the secrets");
            } catch (Exception e) {
                logger.info("error occurred during getting the secret");
            }
        }
    }

    public static void increaseCalenderOpenedMetric() {
        try {
            setAWSCredentials();

            MetricDatum datum = new MetricDatum()
                    .withMetricName(CLOUDWATCH_METRIC_FOR_CALENDER_OPENED)
                    .withUnit(StandardUnit.Count)
                    .withValue(1.0);

            PutMetricDataRequest request = new PutMetricDataRequest()
                    .withNamespace(CLOUDWATCH_METRIC_NAMESPACE)
                    .withMetricData(datum);

            amazonCloudWatchClientBuilder.build().putMetricData(request);
            logger.info("Successfully increased the metric:{}", CLOUDWATCH_METRIC_FOR_CALENDER_OPENED);
        } catch (Exception e) {
            logger.info("Failed to send metric to cloud watch, ignoring.", e);
        }
    }

    public static void increaseReservationDoneMetric() {
        try {
            setAWSCredentials();

            MetricDatum datum = new MetricDatum()
                    .withMetricName(CLOUDWATCH_METRIC_FOR_RESERVATION_COMPLETED)
                    .withUnit(StandardUnit.Count)
                    .withValue(1.0);

            PutMetricDataRequest request = new PutMetricDataRequest()
                    .withNamespace(CLOUDWATCH_METRIC_NAMESPACE)
                    .withMetricData(datum);
            amazonCloudWatchClientBuilder.build().putMetricData(request);
            logger.info("Successfully increased the metric:{}", CLOUDWATCH_METRIC_FOR_RESERVATION_COMPLETED);
        } catch (Exception e) {
            logger.info("Failed to send metric to cloud watch, ignoring.");
        }
    }

    public static void increaseVerifiedTimeslotMetric() {
        try {
            setAWSCredentials();

            MetricDatum datum = new MetricDatum()
                    .withMetricName(CLOUDWATCH_METRIC_FOR_VERIFIED_TIMESLOT)
                    .withUnit(StandardUnit.Count)
                    .withValue(1.0);

            PutMetricDataRequest request = new PutMetricDataRequest()
                    .withNamespace(CLOUDWATCH_METRIC_NAMESPACE)
                    .withMetricData(datum);

            amazonCloudWatchClientBuilder.build().putMetricData(request);
            logger.info("Successfully increased the metric:{}", CLOUDWATCH_METRIC_FOR_VERIFIED_TIMESLOT);

        } catch (Exception e) {
            logger.info("Failed to send metric to cloud watch, ignoring.");
        }
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
