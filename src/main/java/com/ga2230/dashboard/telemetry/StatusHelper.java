package com.ga2230.dashboard.telemetry;

import com.ga2230.dashboard.communications.Connection;
import org.json.JSONObject;

public class StatusHelper {

    private Connection connection;

    public StatusHelper(double refreshRate) {
        connection = Connection.openConnection(refreshRate, false);
    }

    public void configure(String function, StatusHelper.Callback callback) {
        connection.clear();
        connection.send(function, new Connection.Callback() {
            @Override
            public void callback(boolean finished, String result) {
                callback.callback(finished);
            }
        });
    }

    public interface Callback {
        void callback(boolean value);
    }
}
