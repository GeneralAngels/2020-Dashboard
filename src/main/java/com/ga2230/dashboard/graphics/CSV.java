package com.ga2230.dashboard.graphics;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CSV extends Panel {

    private JButton save, saveAndOpen, clear, marker;

    private ArrayList<ArrayList<String>> log = new ArrayList<>();

    public CSV() {
        save = new JButton("Save");
        saveAndOpen = new JButton("Save and Open");
        clear = new JButton("Clear");
        marker = new JButton("Add Marker");
        add(save);
        add(saveAndOpen);
        add(clear);
        add(marker);
        setLayout(new GridLayout(1, 4));
    }
}
