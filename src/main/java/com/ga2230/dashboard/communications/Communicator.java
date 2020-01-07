package com.ga2230.dashboard.communications;

import java.io.*;
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

    public static void reconnect() {
        for (Topic topic : topics) {
            try {
                topic.reconnect();
            } catch (IOException e) {
                e.printStackTrace();
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
                reader.close();
                writer.close();
                socket.close();
            }
            socket = new Socket("10.22.30.2", 2230);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connected = true;
        }

        private void loop() throws IOException {
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
            } catch (SocketException e) {
                reconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void begin(int refreshRate){
            if (!topics.contains(this))
                topics.add(this);
            try {
                reconnect();
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            loop();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 1000 / refreshRate);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void stop() {
            if (topics.contains(this))
                topics.remove(this);
        }
    }

}