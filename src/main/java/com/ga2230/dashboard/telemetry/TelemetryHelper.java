package com.ga2230.dashboard.telemetry;

import com.ga2230.dashboard.communications.Connection;
import org.json.JSONObject;

public class TelemetryHelper {

    private Connection connection;

    public TelemetryHelper(double refreshRate) {
        connection = Connection.openConnection(refreshRate, Connection.ConnectionType.PeriodicExecution);
    }

    public void configure(String module, String value, Callback callback) {
        connection.clear();
        connection.send(new Connection.Command(module + " telemetry", new Connection.Callback() {
            @Override
            public void callback(boolean finished, String result) {
                if (finished) {
                    JSONObject object = new JSONObject(result);
                    if (object.has(value)) {
                        callback.callback(object.getString(value));
                        return;
                    }
                }
                callback.callback("");
            }
        }));
    }

    public interface Callback {
        void callback(String value);
    }

}
