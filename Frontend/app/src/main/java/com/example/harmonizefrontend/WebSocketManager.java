package com.example.harmonizefrontend;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


public class WebSocketManager {
    private static WebSocketManager instance;
    private WebSocket webSocket;
    private final OkHttpClient client = new OkHttpClient();

    private messageListener messageListener;

    private WebSocketManager() {
    }

    // Ensures that only one instance of WebSocketManager is created
    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }

        return instance;
    }

    // Opens webSocket connection with serverURL and user jwtToken
    protected void connect(String serverURL, String jwtToken) {
        if (webSocket != null) {
            Log.e("msg", "WebSocket already started");
            return;
        }

        Request request = new Request.Builder()
                .url(serverURL)
                .addHeader("Authorization", jwtToken)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                super.onOpen(webSocket, response);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);

                JSONObject messageObj = null;
                try {
                    messageObj = new JSONObject(text);
                    String type = messageObj.getString("type");

                    switch (type) {
                        case "harmonize.DTOs.MessageDTO":

                            break;

                        case "harmonize.DTOs.ConversationDTO":
                            // Handle conversation
                            break;
                    }
                } catch (JSONException e) {
                    Log.e("msg", "Error parsing recieved message");
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                super.onFailure(webSocket, t, response);
            }
        });
    }

    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "Closing connection");
            webSocket = null;
        }
    }
}

