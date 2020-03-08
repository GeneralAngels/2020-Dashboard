package com.ga2230.dashboard.communications;

import com.ga2230.dashboard.configuration.Configuration;
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

    public static final BroadcastConnection TelemetryConnection = Communicator.openBroadcastConnection(20);
    public static final Connection ActionConnection = Communicator.openConnection(10, Connection.ConnectionType.QueuedExecution);

    static {
        Communicator.TelemetryConnection.send(new Connection.Command("robot telemetry", null));
        Communicator.TelemetryConnection.register((finished, result) -> TelemetryParser.update(new JSONObject(result)));
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

    public static Connection openConnection(double refreshRate, Connection.ConnectionType connectionType) {
        Connection connection = new Connection(Configuration.load().getTeam(), refreshRate, connectionType);
        connection.open();
        return connection;
    }

    public static BroadcastConnection openBroadcastConnection(double refreshRate) {
        BroadcastConnection broadcastConnection = new BroadcastConnection(Configuration.load().getTeam(), refreshRate);
        broadcastConnection.open();
        return broadcastConnection;
    }

    public static void register(Connection connection) {
        connections.add(connection);
    }
}