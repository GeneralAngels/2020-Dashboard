package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Broadcast;
import com.ga2230.dashboard.communications.Communicator;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class Path extends Panel {

    private JSONArray array;

    private int x, y;

    public Path() {
        Communicator.Topic topic = new Communicator.Topic();
        topic.setCommand("pathman get_da_yeet");
        topic.getBroadcast().listen(new Broadcast.Listener<String>() {
            @Override
            public void update(String thing) {
                array = new JSONArray(thing);
                repaint();
            }
        });
        topic.begin(5);
    }

    @Override
    public void setSize(int width, int height) {
        x = width;
        y = height;
        super.setSize(width, height);
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, this.x, this.y);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, this.x, this.y);
        g.setColor(Color.RED);
        if (array != null) {
            for (Object object : array) {
                if (object instanceof JSONObject) {
                    JSONObject myObject = (JSONObject) object;
                    double x, y;
                    x = myObject.getDouble("x");
                    y = myObject.getDouble("y");
                    x *= 100;
                    y *= 100;
                    x += this.x / 4; // Fucking offset
                    y += this.y / 4;
                    g.fillRect((int) x, this.y - (int) y, 4, 4);
                }
            }
        }
    }
}
