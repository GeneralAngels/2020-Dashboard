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

    public static ArrayList<Topic> topics = new ArrayList<>();

    private static boolean locked = false;

    public static void reconnect() {
        new Thread(() -> {
            for (Topic topic : topics) {
                new Thread(() -> {
                    try {
                        topic.reconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
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
            locked = true;
            int result = JOptionPane.showConfirmDialog(new JFrame(), "Disconnected - Reconnect?", "Oopsi", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                Communicator.reconnect();
            } else if (result == JOptionPane.NO_OPTION || result == JOptionPane.CANCEL_OPTION) {
                locked = true;
            }
        }
    }

    public static class Topic {

        private String command = "master json";
        private Broadcast<String> broadcast = new Broadcast<>();

        private BufferedWriter writer;
        private BufferedReader reader;
        private Socket socket;

        private boolean connected = false;
        private boolean loop = true;

        public Broadcast<String> getBroadcast() {
            return broadcast;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public void reconnect() throws IOException {
            connected = false;
            if (reader != null && writer != null && socket != null) {
                try {
                    reader.close();
                    writer.close();
                    socket.close();
                } catch (Exception ignore) {
                }
            }
            socket = new Socket();
            for (int i = 0; i < 10 && !socket.isConnected(); i++)
                socket.connect(new InetSocketAddress("10.22.30.2", 2230), 500);
            System.out.println("Here");
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connected = true;
        }

        private void loop() {
            try {
                if (connected) {
                    if (loop) {
                        writer.write(command);
                        writer.newLine();
                        writer.flush();
                    } else {
                        String result = reader.readLine();
                        if (result != null)
                            broadcast.send(result);
                    }
                    loop = !loop;
                }
            } catch (IOException e) {
                connected = false;
                Communicator.disconnected();
            }
        }

        public void begin(double refreshRate) {
            if (!topics.contains(this))
                topics.add(this);
            try {
                this.reconnect();
            } catch (IOException e) {
                Communicator.disconnected();
            }
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    loop();
                }
            }, 0, (long) (1000.0 / refreshRate));
        }

        public void stop() {
            if (topics.contains(this))
                topics.remove(this);
        }

        public boolean isConnected() {
            return connected;
        }
    }

}