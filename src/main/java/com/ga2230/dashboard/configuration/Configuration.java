package com.ga2230.dashboard.configuration;

import com.ga2230.dashboard.graphics.StatusPanel;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

public class Configuration {

    private static final URL CONFIGURATION_URL = Configuration.class.getClassLoader().getResource("configuration.json");

    private JSONObject object;

    private int team;
    private ArrayList<StatusPanel.StatusButton.Configuration> configurations;

    private Configuration() throws IOException {
        this.object = new JSONObject(IOUtils.toString(CONFIGURATION_URL.openStream(), Charset.defaultCharset()));
        this.team = object.getInt("team");
        this.configurations = new ArrayList<>();
        for (int index = 0; index < this.object.getJSONArray("buttons").length(); index++) {
            JSONObject object = this.object.getJSONArray("buttons").getJSONObject(index);
            this.configurations.add(new StatusPanel.StatusButton.Configuration(object.getString("text"), object.getString("state"), object.getString("click")));
        }
    }

    public static Configuration load() {
        try {
            return new Configuration();
        } catch (IOException e) {
            return null;
        }
    }

    public int getTeam() {
        return team;
    }

    public ArrayList<StatusPanel.StatusButton.Configuration> getConfigurations() {
        return configurations;
    }
}
