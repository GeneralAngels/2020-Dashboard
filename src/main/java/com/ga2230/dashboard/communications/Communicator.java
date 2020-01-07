package com.ga2230.dashboard.communications;

import com.ga2230.networking.Server;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Copyright (c) 2019 General Angels
 * https://github.com/GeneralAngels/RIO20
 */

public class Communicator {

    private static BufferedWriter writer;
    private static BufferedReader reader;
    private static Socket socket;

    public static Topic topicA = new Topic(), topicB = new Topic(), topicC = new Topic(), topicD = new Topic();

    static {
        try {
            socket = new Socket("10.22.30.2", 2230);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                }
            }, 0, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Topic {

        private String command = "master json";
        private Broadcast<String> broadcast = new Broadcast<>();

        public Broadcast<String> getBroadcast() {
            return broadcast;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public void begin(int refreshRate) {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        writer.write(command + "\n");
                        writer.flush();
                        Thread.sleep(10);
                        broadcast.send(reader.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 1000 / refreshRate);
        }
    }

}