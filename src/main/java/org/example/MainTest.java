package org.example;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

class MainTest {

    public static void main(String[] args) throws IOException {


        URL url = new URL("https://0.dat");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("Host", "otv.verwalt-berlin.de");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestProperty("sec-ch-ua", "\"Google Chrome\";v=\"107\", \"Chromium\";v=\"107\", \"Not=A?Brand\";v=\"24\"");
        con.setRequestProperty("Accept", "*/*");
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundaryxYnOYUFCpb4ViT0Y");
        con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        con.setRequestProperty("sec-ch-ua-mobile", "?0");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
        con.setRequestProperty("sec-ch-ua-platform", "\"macOS\"");
        con.setRequestProperty("Origin", "https://otv.verwalt-berlin.de");
        con.setRequestProperty("Sec-Fetch-Site", "same-origin");
        con.setRequestProperty("Sec-Fetch-Mode", "cors");
        con.setRequestProperty("Sec-Fetch-Dest", "empty");
        con.setRequestProperty("Referer", "https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng/9890b240-2ce6-4b5b-9ca6-5efbb962c88c?v=1667318660115");
        con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        con.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");

        con.setRequestProperty("Cookie", "JSESSIONID=8hu7CQ-45MZ4J3r-bIy1C0kVP2XOiYpj5dmicNoj.frontend-1; SERVERID=frontend-1; otv_neu=d0f9ce66047a3e3090d61c1db32e6579; TS018ca6c6=01d33437f9990c606221e82381112ef6d9472f5c5d59e7c2734d1818a96e189e120a7f0ac3ddbf63c5b61cdbb389246d6cabefc544");

        con.setRequestMethod("POST");

        con.setDoOutput(true);

        //String jsonInputString = "$------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\\"applicationForm:managedForm\\"\n\napplicationForm:managedForm\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\\"frid\\"\n\n9d6c83fe-5f9a-472c-90ee-92f719fb12bb\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\\"lang\\"\n\nde\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\\"gelesen\\"\n\non\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\\"xima-9875-required\\"\n\n\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\\"xfc-pp-elementslist\\"\n\ngelesen\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\\"error_code\\"\n\ne6:76gg4d0360hd73he0g303i<d;6if6gf<6\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\\"clientWizardStep\\"\n\nMV9JbmZvcm1hdGlvbg==\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\\"applicationForm:managedForm:j_idt314\\"\n\n\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\\"javax.faces.ViewState\\"\n\n-7202582509904912962:-3739826161992955906\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\\"javax.faces.ClientWindow\\"\n\n8502\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y--\n";
        String jsonInputString = "$------WebKitFormBoundary";
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        System.out.println("Response code: " + responseCode);

        InputStreamReader inputStreamReader = null;
        if (responseCode >= 200 && responseCode < 400) {
            inputStreamReader = new InputStreamReader(con.getInputStream());
        } else {
            inputStreamReader = new InputStreamReader(con.getErrorStream());
        }
        BufferedReader in = new BufferedReader(inputStreamReader);
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());

// You have specified -k or --insecure in the input request
// Please follow the steps to enable it
// https://stackoverflow.com/questions/1201048/allowing-java-to-use-an-untrusted-certificate-for-ssl-https-connection
    }
}
