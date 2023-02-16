package org.example.auslanderbehorde.sessionfinder.business;

import okhttp3.*;
import okio.ByteString;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;

import static org.openqa.selenium.net.PortProber.findFreePort;

class SessionFinderTest {

    @Test
    void ASSERT_THAT__() {
        ChromeOptions chromeOptions = new ChromeOptions();
        int freePort = findFreePort();
        chromeOptions.addArguments("--remote-debugging-port=" + freePort);

        ChromeDriver driver = new ChromeDriver(chromeOptions);
        String response = makeGetCall("http://127.0.0.1:" + freePort + "/json");
        String webSocketUrl = response.substring(response.indexOf("ws://127.0.0.1"), response.length() - 4);
        WebSocket socket = makeSocketConnection(webSocketUrl);
        socket.send("{\"id\":1,\"method\":\"Network.enable\"}");
    }

    private String makeGetCall(String s) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder().url(s).build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private WebSocket makeSocketConnection(String websocketUrl) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder().url(websocketUrl).build();

        EchoWebSocketListener listener = new EchoWebSocketListener();

        WebSocket ws = client.newWebSocket(request, listener);
        return ws;
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("Knock, knock!");
            webSocket.send("Hello!");
            webSocket.send(ByteString.decodeHex("deadbeef"));
            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye!");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            System.out.println("Receiving: " + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            System.out.println("Receiving: " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            System.out.println("Closing: " + code + " " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            t.printStackTrace();
        }

    }

}