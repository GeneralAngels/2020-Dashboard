package com.ga2230.dashboard.communications;

import com.ga2230.dashboard.graphics.Frame;

import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Copyright (c) 2019 General Angels
 * https://github.com/GeneralAngels/RIO20
 */

public class Communicator {

    public static ArrayList<Connection> topics = new ArrayList<>();

    private static boolean locked = false;

    public static void register(Connection connection) {
        topics.add(connection);
    }

    public static void reconnect() {
        new Thread(() -> {
            for (Connection topic : topics) {
                new Thread(() -> {
                    for (int i = 0; i < 10; i++) {
                        if (!topic.isConnected()) {
                            topic.open();
                        } else
                            break;
                    }
                }).start();
            }
            locked = false;
        }).start();
    }

    public static void unlock() {
        locked = false;
    }

    public static void disconnected() {
        if (!locked) {
            if (Frame.getFrame() != null) {
                locked = true;
                int result = JOptionPane.showConfirmDialog(Frame.getFrame(), "Disconnected - Reconnect?", "Connection error", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    Communicator.reconnect();
                } else if (result == JOptionPane.NO_OPTION || result == JOptionPane.CANCEL_OPTION) {
                    locked = true;
                }
            }
        }
    }
}