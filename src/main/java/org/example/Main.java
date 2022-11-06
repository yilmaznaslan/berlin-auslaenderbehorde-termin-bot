package org.example;

import okhttp3.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static String dswid;
    public static String dsrid;
    public static String requestId;
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        requestId = getRequestId();
        activateRequestId(requestId);
        FormFiller formFiller = new FormFiller(requestId, dswid, dsrid);
        formFiller.fillTheForm();
    }

    public static void activateRequestId(String requestId) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("hello", "myfriend")
                .build();
        Request request = new Request.Builder()
                .url("https://otv.verwalt-berlin.de/api/remote2/TerminBuchen/" + requestId + "/proceed?dswid=8502&suppressRenderOnChange=true")
                .method("POST", body)
                .addHeader("Host", "otv.verwalt-berlin.de")
                .addHeader("Connection", "keep-alive")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"107\", \"Chromium\";v=\"107\", \"Not=A?Brand\";v=\"24\"")
                .addHeader("Accept", "*/*")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .addHeader("Origin", "https://otv.verwalt-berlin.de")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Sec-Fetch-Dest", "empty")
                .addHeader("Referer", "https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng/9890b240-2ce6-4b5b-9ca6-5efbb962c88c?v=1667318660115")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8")
                .addHeader("Cookie", "JSESSIONID=8hu7CQ-45MZ4J3r-bIy1C0kVP2XOiYpj5dmicNoj.frontend-1; SERVERID=frontend-1; otv_neu=d0f9ce66047a3e3090d61c1db32e6579; TS018ca6c6=01d33437f9990c606221e82381112ef6d9472f5c5d59e7c2734d1818a96e189e120a7f0ac3ddbf63c5b61cdbb389246d6cabefc544; JSESSIONID=pEgF7e2XT7kaIpl7yHrZAndVWYt1dIzIwpgFdw62.frontend-1; SERVERID=frontend-2; TS018ca6c6=01d33437f9832280903ece3a5244806bacae79dae315b0c3012bf2c5c185ecce30ff0fd9c7a44fc5ba29bf6e8b2e41c30f468e49ef; otv_neu=d0f9ce66047a3e3090d61c1db32e6579; JSESSIONID=yT4lc5LxUWUzmqMwiYUXJqv7G6sypQaWyLnrke6d.frontend-1; TS018ca6c6=01d33437f99f6cf591632be481b881df35db07d620fe6cdf8ad9f23c8cb9224c2de7c285120642136301818243edb836a1f6b3bf0b")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            logger.error("Executign secuting request failed");
            throw new RuntimeException(e);
        }
        System.out.println("RresponseCode; " + response.code());
    }

    public static String getRequestId() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);

        String requestId;
        String initialUrl = "https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng";
        logger.info("Getting the URL: {}", initialUrl);

        driver.get(initialUrl);
        while (true) {
            String urlAfterRedirect = driver.getCurrentUrl();
            logger.debug("CurrentURL: " + urlAfterRedirect);
            try {
                URL url = new URL(urlAfterRedirect);
                String queryStr = url.getQuery();
                if (dsrid == null && dswid == null) {
                    logger.info("QueryString: {}", queryStr);
                    extractDswidAndDsrid(queryStr);
                }

                if (urlAfterRedirect.contains("?v")) {
                    requestId = extractRequestId(urlAfterRedirect);
                    driver.close();
                    break;
                }

            } catch (MalformedURLException e) {
                logger.error("URL is malformed exception occurred", e);
            }
        }

        return requestId;
    }

    public static void extractDswidAndDsrid(String queryStr) {
        List<String> queryStrings = List.of(queryStr.split("&"));
        dsrid = Arrays.stream(queryStrings.get(0).split("=")).toList().get(1);
        dswid = Arrays.stream(queryStrings.get(1).split("=")).toList().get(1);
        logger.info("dswid: {}, dsrid: {}", dswid, dsrid);
    }

    public static String extractRequestId(String url) {
        List<String> urlAsList = List.of(url.split("/"));
        String requestIdAndV = urlAsList.get(urlAsList.size() - 1);

        String requestId = List.of(requestIdAndV.split("\\?")).get(0);

        //dsrid = Arrays.stream(queryStrings.get(0).split("=")).toList().get(1);
        //dswid = Arrays.stream(queryStrings.get(1).split("=")).toList().get(1);
        logger.info("RequestID: {}", requestId);
        //LOGGER.info("RequestId: {}, v: {}", dswid, dsrid);
        return requestId;

    }


}