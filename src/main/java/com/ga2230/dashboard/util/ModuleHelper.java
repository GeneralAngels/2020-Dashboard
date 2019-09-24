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

    public static boolean getBoolean(String coordinates, JSONObject current) {
        return (boolean) get(coordinates, current, false);
    }

    public static String getString(String coordinates, JSONObject current) {
        return (String) get(coordinates, current, "");
    }

    public static int getInt(String coordinates, JSONObject current) {
        return (int) get(coordinates, current, 0);
    }

    public static double getDouble(String coordinates, JSONObject current) {
        Object object = get(coordinates, current, 0.0);
        if (object instanceof Double) {
            return (double) object;
        } else {
            return (double) (Integer) object;
        }
    }
}
