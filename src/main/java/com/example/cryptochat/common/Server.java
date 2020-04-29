package com.example.cryptochat.common;

import android.util.Log;

import com.example.cryptochat.UserEvents;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.core.util.Consumer;
import androidx.core.util.Pair;

public class Server {

    private UserEvents onUserEventsListener;

    public void setonUserEventsListener (UserEvents onUserEventsListener) {
        this.onUserEventsListener = onUserEventsListener;
    }

    private static final String TAG = "myLOG";

    private WebSocketClient client;
    private Map<Long, String> names = new ConcurrentHashMap<>();
    private Consumer<Pair<String, String>> onMessageReceived;


    public Server(Consumer<Pair<String, String>> onMessageReceived) {
        this.onMessageReceived = onMessageReceived;
    }

    public void connect() {
        URI addr;
        try {
            addr = new URI("ws://35.214.1.221:8881");
            client = new WebSocketClient(addr) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.i(TAG, "Connected to server");
                }

                @Override
                public void onMessage(String json) {
                    int type = Protocol.getType(json);
                    if (type == Protocol.MESSAGE) {
                        displayIncoming(Protocol.unpackMessage(json));
                    }
                    if (type == Protocol.USER_STATUS) {
                        updateStatus(Protocol.unpackStatus(json));
                    }

                    Log.i(TAG, "Got message: " + json);

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.i(TAG, "Connection closed");
                }

                @Override
                public void onError(Exception ex) {
                    Log.e(TAG, "onError", ex);
                }
            };

            client.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
            //return;
        }
    }

    public void disconnect() {
        client.close();
    }

    public void sendMessage(String text) {
        Protocol.Message mess = new Protocol.Message(text);
        if (client != null && client.isOpen()) {
            client.send(Protocol.packMessage(mess));
        }
    }

    public void sendName(String name) {
        Protocol.UserName userName = new Protocol.UserName(name);
        if (client != null && client.isOpen()) {
            client.send(Protocol.packName(userName));
        }
    }

    private void updateStatus(Protocol.UserStatus status) {
        Protocol.User u = status.getUser();
        String userName = u.getName();
        if (status.isConnected()) {
            if (onUserEventsListener != null)
                onUserEventsListener.UserCONNECED(u.getId(), userName == null ? "???" : userName, names.size());

            names.put(u.getId(), u.getName());


        } else {
            if (onUserEventsListener != null)
                onUserEventsListener.UserDISCONNECED(names.size());
            names.remove(u.getId());

        }
    }

    private void displayIncoming(Protocol.Message message) {
        String name = names.get(message.getSender());
        if (name == null) {
            name = "Unnamed";
        }

        onMessageReceived.accept(
                new Pair<>(name, message.getEncodedText())
        );
    }
}
