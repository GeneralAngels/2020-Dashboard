package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Broadcast;
import com.ga2230.dashboard.communications.Communicator;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class Path extends Panel {

    private JSONArray array;

    private static final int WORLD_TO_SCREEN = 150; // Pixels per meter

    private int x, y;

    private double cubeX, cubeY, cubeTheta;

    private double cubeSize = 0.1;

    public Path() {
        Communicator.Topic path = new Communicator.Topic();
        path.setCommand("pathman get_da_yeet");
        path.getBroadcast().listen(new Broadcast.Listener<String>() {
            @Override
            public void update(String thing) {
                array = new JSONArray(thing);
                repaint();
            }
        });
        path.begin(5);
        Communicator.Topic odom = new Communicator.Topic();
        odom.setCommand("odometry json");
        odom.getBroadcast().listen(new Broadcast.Listener<String>() {
            @Override
            public void update(String thing) {
                JSONObject object = new JSONObject(thing);
                cubeX = object.getDouble("x");
                cubeY = object.getDouble("y");
                cubeTheta = object.getDouble("theta");
                repaint();
            }
        });
        odom.begin(5);
    }

    @Override
    public void setSize(int width, int height) {
        x = width;
        y = height;
        super.setSize(width, height);
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.clearRect(0, 0, this.x, this.y);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.x, this.y);
        g.setColor(Color.RED);
        if (array != null) {
            for (Object object : array) {
                if (object instanceof JSONObject) {
                    JSONObject myObject = (JSONObject) object;
                    double x, y;
                    x = myObject.getDouble("x");
                    y = myObject.getDouble("y");
                    x *= WORLD_TO_SCREEN;
                    y *= WORLD_TO_SCREEN;
                    x += this.x / 4; // Fucking offset
                    y += this.y / 4;
                    g.fillRect((int) x, this.y - (int) y, 4, 4);
                }
            }
        }
        double x, y;
        x = cubeX * WORLD_TO_SCREEN;
        y = cubeY * WORLD_TO_SCREEN;
        x += this.x / 4; // Fucking offset
        y += this.y / 4;
        g.setColor(Color.BLUE);
        Rectangle rectangle = new Rectangle((int) x, (int) (this.y - y), (int) (cubeSize * WORLD_TO_SCREEN), (int) (cubeSize * WORLD_TO_SCREEN));
        g.rotate(Math.toRadians(cubeTheta), rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
        g.draw(rectangle);
        g.fill(rectangle);
//        g.drawRect(((int) cubeX) * WORLD_TO_SCREEN, this.y - ((int) (cubeY * WORLD_TO_SCREEN)), (int) (cubeSize * WORLD_TO_SCREEN), (int) (cubeSize * WORLD_TO_SCREEN));
    }
}
