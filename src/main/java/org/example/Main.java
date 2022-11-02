package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class Main {

    public static void main(String[] args) throws IOException {
        URL url = new URL("https://otv.verwalt-berlin.de/api/remote2/TerminBuchen/0a943678-fe4c-4d20-874e-227220d240fc/proceed?dswid=8502&suppressRenderOnChange=true");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Host", "otv.verwalt-berlin.de");
        httpConn.setRequestProperty("Connection", "keep-alive");
        httpConn.setRequestProperty("sec-ch-ua", "\"Google Chrome\";v=\"107\", \"Chromium\";v=\"107\", \"Not=A?Brand\";v=\"24\"");
        httpConn.setRequestProperty("Accept", "*/*");
        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundaryxYnOYUFCpb4ViT0Y");
        httpConn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        httpConn.setRequestProperty("sec-ch-ua-mobile", "?0");
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36");
        httpConn.setRequestProperty("sec-ch-ua-platform", "\"macOS\"");
        httpConn.setRequestProperty("Origin", "https://otv.verwalt-berlin.de");
        httpConn.setRequestProperty("Sec-Fetch-Site", "same-origin");
        httpConn.setRequestProperty("Sec-Fetch-Mode", "cors");
        httpConn.setRequestProperty("Sec-Fetch-Dest", "empty");
        httpConn.setRequestProperty("Referer", "https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng/9890b240-2ce6-4b5b-9ca6-5efbb962c88c?v=1667318660115");
        httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        httpConn.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
        httpConn.setRequestProperty("Cookie", "JSESSIONID=8hu7CQ-45MZ4J3r-bIy1C0kVP2XOiYpj5dmicNoj.frontend-1; SERVERID=frontend-1; otv_neu=d0f9ce66047a3e3090d61c1db32e6579; TS018ca6c6=01d33437f9990c606221e82381112ef6d9472f5c5d59e7c2734d1818a96e189e120a7f0ac3ddbf63c5b61cdbb389246d6cabefc544");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write("------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\"applicationForm:managedForm\"\n\napplicationForm:managedForm\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\"frid\"\n\n9d6c83fe-5f9a-472c-90ee-92f719fb12bb\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\"lang\"\n\nde\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\"gelesen\"\n\non\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\"xima-9875-required\"\n\n\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\"xfc-pp-elementslist\"\n\ngelesen\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\"error_code\"\n\ne6:76gg4d0360hd73he0g303i<d;6if6gf<6\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\"clientWizardStep\"\n\nMV9JbmZvcm1hdGlvbg==\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\"applicationForm:managedForm:j_idt314\"\n\n\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\"javax.faces.ViewState\"\n\n-7202582509904912962:-3739826161992955906\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y\nContent-Disposition: form-data; name=\"javax.faces.ClientWindow\"\n\n8502\n------WebKitFormBoundaryxYnOYUFCpb4ViT0Y--\n");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        if ("gzip".equals(httpConn.getContentEncoding())) {
            responseStream = new GZIPInputStream(responseStream);
        }
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        System.out.println(response);
    }


//String hostUrl = "https://otv.verwalt-berlin.de/ams/TerminBuchen/wizardng";
//String requestId = "0eca6037-0ec1-4db0-8258-fb0678dcefbd";
//       activateId(requestId);
//String targetUrl = hostUrl + "/" + requestId +"?dswid=5224&dsrid=387&st=2&v="+getRandomNumber();
//SessionCatcher.getServiceSelectionTab(targetUrl);


public static int getRandomNumber(){
        int min=100;
        int max=999999999;
        return(int)((Math.random()*(max-min))+min);
        }

private static void activateId(String requestId)throws IOException{
        URL url=new URL("https://otv.verwalt-berlin.de/api/remote2/TerminBuchen/"+requestId+"/proceed?dswid=8502&suppressRenderOnChange=true");
        System.out.println("URL:"+url.toString());
        HttpURLConnection http=(HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Host","otv.verwalt-berlin.de");
        http.setRequestProperty("Connection","keep-alive");
        //http.setRequestProperty("sec-ch-ua", ""Google Chrome";v="107", "Chromium";v="107", "Not=A?Brand";v="24"");
        http.setRequestProperty("Accept","*/*");
        http.setRequestProperty("Content-Type","multipart/form-data; boundary=----WebKitFormBoundaryxYnOYUFCpb4ViT0Y");
        http.setRequestProperty("X-Requested-With","XMLHttpRequest");
        http.setRequestProperty("sec-ch-ua-mobile","?0");
        http.setRequestProperty("User-Agent","Mozilla/5.0 (Macint)");
        }

}