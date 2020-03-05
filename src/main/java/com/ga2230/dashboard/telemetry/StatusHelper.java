package com.ga2230.dashboard.telemetry;

import com.ga2230.dashboard.communications.Connection;
import org.json.JSONObject;

public class StatusHelper {

    private Connection connection;

    public StatusHelper(double refreshRate) {
        connection = Connection.openConnection(refreshRate, Connection.ConnectionType.PeriodicExecution);
    }

    public void configure(String function, StatusHelper.Callback callback) {
        connection.clear();
        connection.send(new Connection.Command(function, new Connection.Callback() {
            @Override
            public void callback(boolean finished, String result) {
                callback.callback(finished);
            }
        }));
    }

    public interface Callback {
        void callback(boolean value);
    }
}
