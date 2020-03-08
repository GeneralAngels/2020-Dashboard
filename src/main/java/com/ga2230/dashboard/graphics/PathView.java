package com.ga2230.dashboard.graphics;

import com.ga2230.dashboard.communications.Communicator;
import com.ga2230.dashboard.communications.Connection;
import com.ga2230.dashboard.communications.Global;
import com.ga2230.dashboard.telemetry.TelemetryParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;

public class PathView extends Panel {

    private JSONArray array;

    private static final int WORLD_TO_SCREEN = 50; // Pixels per meter

    private static final int PATH_POINT_SIZE_PX = 4;

    private static final double CUBE_SIZE_M = 0.5;
    private static final double OFFSET_DIVIDER = 2;

    private int x, y;

    private double cubeX, cubeY, cubeAngle;


    public PathView() {
        Connection pathConnection = Communicator.openConnection(2, Connection.ConnectionType.PeriodicExecution);
        pathConnection.send(new Connection.Command("path fetch", new Connection.Callback() {
            @Override
            public void callback(boolean finished, String result) {
                array = new JSONArray(result);
                repaint();
            }
        }));

        Global.TelemetryConnection.register(new Connection.Callback() {
            @Override
            public void callback(boolean finished, String result) {
                cubeX = TelemetryParser.findDouble("odometry", "x");
                cubeY = TelemetryParser.findDouble("odometry", "y");
                cubeAngle = TelemetryParser.findDouble("odometry", "theta");

                repaint();
            }
        });
    }

    @Override
    public void setSize(int width, int height) {
        this.x = width;
        this.y = height;
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
                    x += this.x / OFFSET_DIVIDER; // Fucking offset
                    y += this.y / OFFSET_DIVIDER;
                    g.fillRect((int) x - PATH_POINT_SIZE_PX / 2, this.y - (int) y - PATH_POINT_SIZE_PX / 2, PATH_POINT_SIZE_PX, PATH_POINT_SIZE_PX);
                }
            }
        }
        double x, y;
        x = cubeX * WORLD_TO_SCREEN;
        y = cubeY * WORLD_TO_SCREEN;
        x += this.x / OFFSET_DIVIDER; // Fucking offset
        y += this.y / OFFSET_DIVIDER;
        g.setColor(Color.BLUE);
        Rectangle rectangle = new Rectangle((int) x - ((int) (CUBE_SIZE_M * WORLD_TO_SCREEN) / 2), (int) (this.y - y) - ((int) (CUBE_SIZE_M * WORLD_TO_SCREEN) / 2), (int) (CUBE_SIZE_M * WORLD_TO_SCREEN), (int) (CUBE_SIZE_M * WORLD_TO_SCREEN));
        g.rotate(Math.toRadians(-cubeAngle), rectangle.x, rectangle.y);
        g.draw(rectangle);
        g.fill(rectangle);
//        g.drawRect(((int) cubeX) * WORLD_TO_SCREEN, this.y - ((int) (cubeY * WORLD_TO_SCREEN)), (int) (cubeSize * WORLD_TO_SCREEN), (int) (cubeSize * WORLD_TO_SCREEN));
    }
}
