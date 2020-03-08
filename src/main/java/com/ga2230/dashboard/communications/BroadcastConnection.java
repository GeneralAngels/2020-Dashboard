package com.ga2230.dashboard.communications;

import java.util.ArrayList;
import java.util.Iterator;

public class BroadcastConnection extends Connection {

    private ArrayList<Connection.Callback> callbacks;

    public BroadcastConnection(int teamNumber, double refreshRate) {
        // Set the connection up
        super(teamNumber, refreshRate, ConnectionType.PeriodicExecution);
        // Set the properties up
        this.callbacks = new ArrayList<>();
    }

    @Override
    public void send(Command command) {
        super.send(new Command(command.getFunction(), new Callback() {
            @Override
            public void callback(boolean finished, String result) {
                Iterator<Connection.Callback> callbackIterator = callbacks.iterator();
                while (callbackIterator.hasNext()) {
                    Callback callback = callbackIterator.next();
                    if (callback != null) {
                        callback.callback(finished, result);
                    }
                }
            }
        }));
    }

    public void register(Connection.Callback callback) {
        callbacks.add(callback);
    }

}
