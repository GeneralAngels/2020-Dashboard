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

    private static JFrame frame;

    public static void setFrame(JFrame frame) {
        Communicator.frame = frame;
    }

    public static void reconnect() {
        new Thread(() -> {
            for (Topic topic : topics) {
                System.out.println("Here");
                new Thread(() -> {
                    System.out.println("Here2");
                    for (int i = 0; i < 10; i++) {
                        if (!topic.isConnected()) {
                            try {
                                topic.reconnect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
            if (frame != null) {
                locked = true;
                int result = JOptionPane.showConfirmDialog(frame, "Disconnected - Reconnect?", "Oopsi", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    Communicator.reconnect();
                } else if (result == JOptionPane.NO_OPTION || result == JOptionPane.CANCEL_OPTION) {
                    locked = true;
                }
            }
        }
    }

    public static class Topic {

//        private static final String ADDRESS = "roboRIO-2230-FRC";
        private static final String ADDRESS = "10.22.30.2";
        private static final int PORT = 5800;

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
//            System.out.println("Dis");
            if (reader != null && writer != null && socket != null) {
                try {
                    reader.close();
                    writer.close();
                    socket.close();
                } catch (Exception ignore) {
                }
            }
            socket = new Socket();
            socket.connect(new InetSocketAddress(ADDRESS, PORT), 1000);
//            System.out.println("Con");
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            System.out.println("OK");
            connected = true;
            loop = true; // Must have this fucking line
        }

        private void loop() {
            try {
                if (connected) {
                    if (loop) {
                        if (command != null) {
                            writer.write(command);
                            writer.newLine();
                            writer.flush();
                        }
                    } else {
                        String result = reader.readLine();
                        try {
                            if (result != null)
                                broadcast.send(result);
                        } catch (Exception ignored) {
                        }
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
            if (refreshRate > 0) {
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        loop();
                    }
                }, 0, (long) (1000.0 / refreshRate));
            }
        }

        public void single(double rate) {
            try {
                this.reconnect();
            } catch (IOException e) {
                Communicator.disconnected();
            }
            if (rate > 0) {
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        loop();
                        setCommand(null);
                    }
                }, 0, (long) (1000.0 / rate));
            }
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