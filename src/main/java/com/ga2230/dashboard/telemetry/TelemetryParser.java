package com.ga2230.dashboard.telemetry;

import com.ga2230.dashboard.communications.Communicator;
import com.ga2230.dashboard.communications.Connection;
import org.json.JSONObject;

public abstract class TelemetryParser {

    private static JSONObject object;

    static {
        Communicator.TelemetryConnection.register(new Connection.Callback() {
            @Override
            public void callback(boolean finished, String result) {
                TelemetryParser.update(new JSONObject(result));
            }
        });
    }

    public static JSONObject get() {
        return object;
    }

    public static void update(JSONObject object) {
        TelemetryParser.object = object;
    }

    public static Double find(String module, String value) {
        return find(module, value, TelemetryParser.object, module.equals("robot"));
    }

    private static Double find(String module, String value, JSONObject object, boolean search) {
        if (search) {
            if (object.has(value)) {
                return Double.parseDouble(object.getString(value));
            }
        }
        for (String key : object.keySet()) {
            Object child = object.get(key);
            if (child instanceof JSONObject) {
                Double foundValue = find(module, value, (JSONObject) child, key.equals(module));
                if (foundValue != null)
                    return foundValue;
            }
        }
        return null;
    }

}
