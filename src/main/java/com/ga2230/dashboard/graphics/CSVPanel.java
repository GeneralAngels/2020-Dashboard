package com.ga2230.dashboard.graphics;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.ga2230.dashboard.communications.Communicator;
import com.ga2230.dashboard.communications.Connection;
import com.ga2230.dashboard.communications.Global;
import com.ga2230.dashboard.telemetry.TelemetryParser;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class CSVPanel extends Panel {

    boolean record = false;

    private JButton toggle, save, saveAndOpen, clear;

    private HashMap<String, ArrayList<String>> log = new HashMap<>();

    private long index = 0;

    public CSVPanel() {
        toggle = new JButton("Record");
        save = new JButton("Save");
        saveAndOpen = new JButton("Save and Open");
        clear = new JButton("Clear");
        save.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        saveAndOpen.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = save();
                    if (file != null)
                        Desktop.getDesktop().open(file);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        clear.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.clear();
            }
        });
        toggle.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (record) {
                    toggle.setText("Record");
                } else {
                    toggle.setText("Don't Record");
                }
                record = !record;
            }
        });
        add(toggle);
        add(save);
        add(saveAndOpen);
        add(clear);
        setLayout(new GridLayout(1, 4));

        Global.TelemetryConnection.register(new Connection.Callback() {
            @Override
            public void callback(boolean finished, String result) {
                if (record) {
                    update(TelemetryParser.get(), "robot");
                    index++;
                }
            }
        });
    }

    private void update(JSONObject object, String title) {
        for (String key : object.keySet()) {
            Object current = object.get(key);
            if (current instanceof JSONObject) {
                update((JSONObject) current, key);
            } else {
                String columnName = title + ">" + key;
                ArrayList<String> currentColumn = log.get(columnName);
                // Make sure its not null
                if (currentColumn == null) {
                    currentColumn = new ArrayList<>();
                }
                for (long i = currentColumn.size(); i < index; i++) {
                    currentColumn.add(null);
                }
                // Add another one
                currentColumn.add((String) current);
                // Set
                log.put(columnName, currentColumn);
            }
        }
    }

    private File save() {
        try {
            File directory = new File(System.getProperty("user.home") + "/DashboardLogs/" + new SimpleDateFormat("dd-MM-YYYY").format(new Date()));
            if (directory.mkdirs() || directory.exists()) {
                String date = new SimpleDateFormat("HH-mm-ss").format(new Date());
                File logFile = new File(directory, date + ".csv");
                if (logFile.createNewFile()) {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(logFile));
                    outputStreamWriter.append(String.join(",", log.keySet())).append("\n");
                    for (int i = 1; i < index; i++) {
                        int j = i;
                        log.forEach((s, strings) -> {
                            try {
                                if (strings.size() > j) {
                                    outputStreamWriter.append(strings.get(j - 1));
                                }
                                outputStreamWriter.append(",");
                            } catch (IOException ignored) {
                            }
                        });
                        outputStreamWriter.append("\n");
                    }
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    return logFile;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    private ArrayList<String> flatten(JSONObject jsonObject, boolean keys) {
        ArrayList<String> flattened = new ArrayList<>();
        for (String key : jsonObject.keySet()) {
            if (jsonObject.get(key) instanceof JSONObject) {
                for (String sub : flatten(jsonObject.getJSONObject(key), keys)) {
                    flattened.add(keys ? (key + ">" + sub) : sub);
                }
            } else {
                flattened.add(keys ? key : jsonObject.get(key).toString());
            }
        }
        return flattened;
    }
}
