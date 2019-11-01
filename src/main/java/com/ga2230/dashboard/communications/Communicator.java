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
    private static OnReceive onReceive = new OnReceive() {
        @Override
        public void receive(String s, Dialog dialog) {
            if (s.length() > 0)
                pullListener.send(new JSONObject(s));
        }
    };

    static {
        pullListener = new Broadcaster<>();
        pushListener = new Broadcaster<>();
        Server.begin(new OnReceive() {
            @Override
            public void receive(String s, Dialog dialog) {
                pushListener.send(new JSONObject(s));
            }
        });
        reconnect();
    }

    public static void reconnect() {
        if (dialog == null || !dialog.isRunning()) {
            if (dialog != null)
                dialog.kill();
            dialog = Dialog.connect("10.22.30.2", onReceive, null);
        }
    }
}