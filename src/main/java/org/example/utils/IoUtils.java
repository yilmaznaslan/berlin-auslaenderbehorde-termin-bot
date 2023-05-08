package org.example.utils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
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
    public static boolean isS3Enabled = true;
    public static String CLOUDWATCH_METRIC_NAMESPACE = "termin-bot";
    public static boolean isLocalSaveEnabled = true;
    public static boolean isCloudwatchEnabled = true;
    private static S3Client s3Client;
    private static String AWS_ACCESS_KEY_ID;
    private static String AWS_SECRET_ACCESS_KEY;
    private static AwsCredentials awsCredentials;

    private IoUtils() {
    }

    public static void sendEventToAWS(EventDetail eventDetail) {
        try {
        setAWSCredentials();
        EventBridgeClient eventBrClient = EventBridgeClient.builder()
                .region(Region.of(AWS_REGION))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(eventDetail);
        } catch (JsonProcessingException e) {
            return;
        }
        PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
                .eventBusName("termin-event-bus")
                .source("ExampleSource")
                .detail(json)
                .detailType("ExampleType")
                .build();

        PutEventsRequest eventsRequest = PutEventsRequest.builder()
                .entries(entry)
                .build();

        PutEventsResponse response = eventBrClient.putEvents(eventsRequest);

        response.entries().stream()
                .forEach(resultEntry -> {
                            String eventId = resultEntry.eventId();
                            if (eventId != null) {
                                logger.info("EventId: {}", eventId);
                            } else {
                                logger.warn("Failed sending event. ErrorCode: {}", resultEntry.errorMessage());
                            }
                        }
                );
        }
        catch (Exception e){
            logger.info("Error occurred during sending event to AWS.Reason ", e);
        }
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
                awsCredentials = AwsBasicCredentials.create(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);

                logger.info("Successfully retrieved the secrets");
            } catch (Exception e) {
                logger.info("error occurred during getting the secret");
            }
        }
    }

    public static void increaseCalenderOpenedMetric() {
        EventDetail eventDetail = new EventDetail("calender", "completed");
        sendEventToAWS(eventDetail);
    }

    public static void increaseVerifiedTimeslotMetric() {
        EventDetail eventDetail = new EventDetail("timeslot", "completed");
        sendEventToAWS(eventDetail);
    }

    public static void increaseReservationDoneMetric() {
        EventDetail eventDetail = new EventDetail("reservation", "completed");
        sendEventToAWS(eventDetail);
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
        try {


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
                s3Client = S3Client.builder()
                        .region(Region.of(AWS_REGION))
                        .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                        .build();

                try {
                    s3Client.putObject(PutObjectRequest.builder().bucket(S3_BUCKET_NAME).key(pagesourceFileName).build(), RequestBody.fromFile(sourceFile));
                    s3Client.putObject(PutObjectRequest.builder().bucket(S3_BUCKET_NAME).key(screenshotFileName).build(), RequestBody.fromFile(screenShotFile));
                } catch (Exception e) {
                    logger.error("Error occurred during s3 operation. Exception: ", e);
                }
            } else {
                logger.info("S3 is not  enabled");
            }

        } catch (Exception e) {
            logger.info("Saving has failed. Reason: ", e);
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
