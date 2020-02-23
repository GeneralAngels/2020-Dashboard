package com.ga2230.dashboard.graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class Stream extends Panel {

    private static int QUEUE_SIZE = 1000;
    private static final int PORT = 554;
    //    private static final String ADDRESS = "10.22.30.21";
    private static final String ADDRESS = "127.0.0.1";

    private static final String URL = "http://" + ADDRESS + ":" + PORT + "/video.mjpeg";

    private BufferedImage bufferedImage;

    @Override
    public void paint(Graphics g) {
        g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }

    public void play() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    bufferedImage = ImageIO.read(new URL(URL));
                    repaint();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);
    }
}
