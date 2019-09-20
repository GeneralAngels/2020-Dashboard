package com.ga2230.dashboard.communications;

import edu.wpi.first.networktables.*;
import org.json.JSONObject;

/**
 * Copyright (c) 2019 General Angels
 * https://github.com/GeneralAngels/RIO20
 */

public class Communicator {
    public static Broadcaster<JSONObject> pullListener;
    public static Broadcaster<JSONObject> pushListener;
    private static NetworkTableInstance instance;
    private static NetworkTable database;
    private static NetworkTableEntry push, pull;

    static {
        pullListener = new Broadcaster<>();
        pushListener = new Broadcaster<>();
        instance = NetworkTableInstance.getDefault();
        database = instance.getTable("database");
        instance.startClientTeam(2230);
        instance.startDSClient();
        push = database.getEntry("push");// Push to robot
        pull = database.getEntry("pull");// Pull from robot
        pull.addListener(entryNotification -> {
            pullListener.send(new JSONObject(entryNotification.value.getString()));
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        push.addListener(entryNotification -> {
            pushListener.send(new JSONObject(entryNotification.value.getString()));
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }
}