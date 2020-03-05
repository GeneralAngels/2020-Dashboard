package com.ga2230.dashboard.telemetry;

import org.json.JSONObject;

public abstract class TelemetryParser {

    private static JSONObject object;

    public static JSONObject get() {
        return object;
    }

    public static void update(JSONObject object) {
        TelemetryParser.object = object;
    }


    public static Double findDouble(String module, String value) {
        if (TelemetryParser.object != null) {
            String result = find(module, value, TelemetryParser.object, module.equals("robot"));
            if (result != null)
                return Double.parseDouble(result);
        }
        return 0.0;
    }

    public static String findString(String module, String value) {
        if (TelemetryParser.object != null) {
            String result = find(module, value, TelemetryParser.object, module.equals("robot"));
            if (result != null)
                return result;
        }
        return "";
    }

    private static String find(String module, String value, JSONObject object, boolean search) {
        if (search) {
            if (object.has(value)) {
                return object.getString(value);
            }
        }
        for (String key : object.keySet()) {
            Object child = object.get(key);
            if (child instanceof JSONObject) {
                String foundValue = find(module, value, (JSONObject) child, key.equals(module));
                if (foundValue != null)
                    return foundValue;
            }
        }
        return null;
    }

}
