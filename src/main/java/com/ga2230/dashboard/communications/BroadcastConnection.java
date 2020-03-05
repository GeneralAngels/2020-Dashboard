package com.ga2230.dashboard.communications;

import java.util.ArrayList;
import java.util.Iterator;

public class BroadcastConnection {

    private Connection connection;

    private ArrayList<Connection.Callback> callbacks;

    public BroadcastConnection(String command, double refreshRate) {
        callbacks = new ArrayList<>();
        connection = Connection.openConnection(refreshRate, Connection.ConnectionType.PeriodicExecution);
        connection.send(new Connection.Command(command, new Connection.Callback() {
            @Override
            public void callback(boolean finished, String result) {
                Iterator<Connection.Callback> callbackIterator = callbacks.iterator();
                while (callbackIterator.hasNext())
                    callbackIterator.next().callback(finished, result);
            }
        }));
    }

    public void register(Connection.Callback callback) {
        callbacks.add(callback);
    }

}
