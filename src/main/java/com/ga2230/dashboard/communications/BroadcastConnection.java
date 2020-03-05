package com.ga2230.dashboard.communications;

import java.util.ArrayList;

public class BroadcastConnection {

    private Connection connection;

    private ArrayList<Connection.Callback> callbacks;

    public BroadcastConnection(String command, double refreshRate) {
        callbacks = new ArrayList<>();
        connection = Connection.openConnection(refreshRate, Connection.ConnectionType.PeriodicExecution);
        connection.send(new Connection.Command(command, new Connection.Callback() {
            @Override
            public void callback(boolean finished, String result) {
                for (Connection.Callback callback : callbacks)
                    callback.callback(finished, result);
            }
        }));
    }

    public void register(Connection.Callback callback) {
        callbacks.add(callback);
    }

}
