package com.ga2230.dashboard.communications;

import com.ga2230.dashboard.graphics.Frame;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Copyright (c) 2019 General Angels
 * https://github.com/GeneralAngels/RIO20
 */

public class Communicator {

    public static ArrayList<Connection> connections = new ArrayList<>();

    private static boolean locked = false;

    public static void register(Connection connection) {
        connections.add(connection);
    }

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

    public static void unlock() {
        locked = false;
    }

    public static void disconnected() {
        if (!locked) {
            locked = true;
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
}