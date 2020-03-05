package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Connection;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class CSVPanel extends Panel {

    boolean record = false;

    private JButton toggle, save, saveAndOpen, clear, bookmark;

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<ArrayList<String>> log = new ArrayList<>();

    private JSONObject full = new JSONObject();

    public CSVPanel() {
        toggle = new JButton("Record");
        save = new JButton("Save");
        saveAndOpen = new JButton("Save and Open");
        clear = new JButton("Clear");
        bookmark = new JButton("Add Marker");
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
        bookmark.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.add(new ArrayList<>(Collections.singletonList(JOptionPane.showInputDialog("Bookmark Name"))));
            }
        });
        clear.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                titles.clear();
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
        add(bookmark);
        add(clear);
        setLayout(new GridLayout(1, 4));

        Connection telemetryConnection = Connection.openConnection(20, Connection.ConnectionType.PeriodicExecution);
        telemetryConnection.send(new Connection.Command("robot telemetry", new Connection.Callback() {
            @Override
            public void callback(boolean finished, String result) {
                try {
                    full = new JSONObject(result);
                    CSVPanel.this.update();
                } catch (Exception ignored) {
                }
            }
        }));
    }

    private void update() {
        if (record) {
            titles = flatten(full, true);
            log.add(flatten(full, false));
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
                    ArrayList<ArrayList<String>> logData = new ArrayList<>();
                    logData.add(titles);
                    logData.addAll(log);
                    for (ArrayList<String> line : logData) {
                        String lineString = "";
                        for (String cell : line) {
                            if (lineString.length() > 0) {
                                lineString += ",";
                            }
                            lineString += cell;
                        }
                        outputStreamWriter.append(lineString).append("\n");
                    }
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
