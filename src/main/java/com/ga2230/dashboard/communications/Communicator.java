package com.ga2230.dashboard.communications;

import com.ga2230.networking.Dialog;
import com.ga2230.networking.OnConnect;
import com.ga2230.networking.OnReceive;
import com.ga2230.networking.Server;
import org.json.JSONObject;

/**
 * Copyright (c) 2019 General Angels
 * https://github.com/GeneralAngels/RIO20
 */

public class Communicator {
    public static Broadcaster<JSONObject> pullListener;
    public static Broadcaster<JSONObject> pushListener;
    private static Dialog dialog = null;
    private static OnConnect onConnect = null;
    private static OnReceive onReceive = null;

    static {
        pullListener = new Broadcaster<>();
        pushListener = new Broadcaster<>();
        Server.begin(new OnReceive() {
            @Override
            public void receive(String s, Dialog dialog) {
                pushListener.send(new JSONObject(s));
            }
        });
        onReceive = new OnReceive() {
            @Override
            public void receive(String s, Dialog dialog) {
                if (s.length() > 0)
                    pullListener.send(new JSONObject(s));
            }
        };
        onConnect = new OnConnect() {
            @Override
            public void onConnect(Dialog dialog) {

            }

            @Override
            public void onDisonnect(Dialog dialog) {
                new Thread(() -> {
                    try {
                        Communicator.dialog = Dialog.connect("10.22.30.2", onReceive, Communicator.onConnect);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        };
        onConnect.onDisonnect(null);

    }

    private static void connectToPull() {
        Dialog.connect("10.22.30.2", (s, dialog) -> {

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