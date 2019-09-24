package com.ga2230.dashboard.util;

import org.json.JSONObject;

public class ModuleHelper {
    public static Object get(String coordinates, JSONObject current, Object fallback) {
        try {
            String[] split = coordinates.split("->");
            JSONObject temporary = current;
            for (int i = 0; i < split.length - 1; i++) {
                temporary = temporary.getJSONObject(split[i]);
            }
            return temporary.get(split[split.length - 1]);
        } catch (Exception e) {
            return fallback;
        }
    }

    public static Boolean getBoolean(String coordinates, JSONObject current) {
        return (Boolean) get(coordinates, current, false);
    }

    public static String getString(String coordinates, JSONObject current) {
        return (String) get(coordinates, current, "");
    }

    public static Integer getInt(String coordinates, JSONObject current) {
        return (Integer) get(coordinates, current, 0);
    }

    public static Double getDouble(String coordinates, JSONObject current) {
        return (Double) get(coordinates, current, 0.0);
    }
}
