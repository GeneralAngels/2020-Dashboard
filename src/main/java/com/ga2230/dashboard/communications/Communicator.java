package com.ga2230.dashboard.communications;

import com.ga2230.networking.Dialog;
import com.ga2230.networking.OnConnect;
import com.ga2230.networking.OnReceive;
import org.json.JSONObject;

/**
 * Copyright (c) 2019 General Angels
 * https://github.com/GeneralAngels/RIO20
 */

public class Communicator {
    public static Broadcaster<JSONObject> pullListener;
    public static Broadcaster<JSONObject> pushListener;

    static {
        pullListener = new Broadcaster<>();
        pushListener = new Broadcaster<>();
        new Thread(() -> {
            connectToPull();
            connectToPush();
        }).start();
    }

    private static void connectToPull() {
        Dialog.connect("10.22.30.2", (s, dialog) -> {
            if (s.length() > 0)
                pullListener.send(new JSONObject(s));
        }, new OnConnect() {
            @Override
            public void onConnect(Dialog dialog) {

            }

            @Override
            public void onDisonnect(Dialog dialog) {
            }
        });
    }

    private static void connectToPush() {
        Dialog.connect("10.22.30.3", (s, dialog) -> {
            if (s.length() > 0)
                pushListener.send(new JSONObject(s));
        }, new OnConnect() {
            @Override
            public void onConnect(Dialog dialog) {

            }

            @Override
            public void onDisonnect(Dialog dialog) {
            }
        });
    }
}