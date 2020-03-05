package com.ga2230.dashboard.communications;

import com.ga2230.dashboard.graphics.Frame;
import com.ga2230.dashboard.telemetry.TelemetryParser;
import org.json.JSONObject;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Copyright (c) 2019 General Angels
 * https://github.com/GeneralAngels/RIO20
 */

public abstract class Communicator {

    private static final ArrayList<Connection> connections = new ArrayList<>();

    public static final BroadcastConnection TelemetryConnection = new BroadcastConnection("robot telemetry", 20);
    public static final Connection ActionConnection = Connection.openConnection(10, Connection.ConnectionType.QueuedExecution);

    static {
        Communicator.TelemetryConnection.register(new Connection.Callback() {
            @Override
            public void callback(boolean finished, String result) {
                TelemetryParser.update(new JSONObject(result));
            }
        });
    }


    private static boolean popupLock = false;

    public static void reconnect() {
        new Thread(() -> {
            for (Connection topic : connections) {
                if (!topic.isConnected()) {
                    topic.open();
                }
            }
            unlock();
        }).start();
    }

    public static void disconnected() {
        if (!popupLock) {
            popupLock = true;
            if (Frame.getFrame() != null) {
                int result = JOptionPane.showConfirmDialog(Frame.getFrame(), "Disconnected - Reconnect?", "Connection error", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    Communicator.reconnect();
                } else {
                    unlock();
                }
            }
        }
    }

    public static void unlock() {
        popupLock = false;
    }

    public static void register(Connection connection) {
        connections.add(connection);
    }
}