package com.ga2230.dashboard.configuration;

import com.ga2230.dashboard.graphics.StatusPanel;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public abstract class Configuration {

    private static final URL CONFIGURATION_URL = Configuration.class.getClassLoader().getResource("configuration.json");

    private static JSONObject object;

    private static int teamNumber;
    private static ArrayList<StatusPanel.StatusButton.Configuration> configurations;

    public static void initialize() {
        try {
            Configuration.object = new JSONObject(IOUtils.toString(CONFIGURATION_URL.openStream(), Charset.defaultCharset()));
            Configuration.teamNumber = object.getInt("team");
            Configuration.configurations = new ArrayList<>();
            for (int index = 0; index < Configuration.object.getJSONArray("buttons").length(); index++) {
                JSONObject object = Configuration.object.getJSONArray("buttons").getJSONObject(index);
                Configuration.configurations.add(new StatusPanel.StatusButton.Configuration(object.getString("text"), object.getString("state"), object.getString("click")));
            }
        } catch (IOException e) {
        }
    }

    public static int getTeamNumber() {
        return teamNumber;
    }

    public static ArrayList<StatusPanel.StatusButton.Configuration> getConfigurations() {
        return configurations;
    }
}
