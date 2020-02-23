package com.ga2230.dashboard.graphics;

import net.sf.jipcam.axis.MjpegFrame;
import net.sf.jipcam.axis.MjpegInputStream;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class Stream extends Panel {

    private static final String CONTENT_LENGTH = "Content-length: ";
    private static final String CONTENT_TYPE = "Content-type: image/jpeg";
    private static final int PORT = 5810;
    //    private static final String ADDRESS = "192.168.0.106";
    private static final String ADDRESS = "10.22.30.20";

    private static final String URL = "http://" + ADDRESS + ":" + PORT + "/";

    private MjpegInputStream mjpegInputStream;
    private BufferedImage bufferedImage;
    private Timer timer;

    public Stream() {
        addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                play();
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {

            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }

    public void play() {
        if (timer != null)
            timer.cancel();
        try {
            URLConnection urlConnection = new URL(URL).openConnection();
            urlConnection.setReadTimeout(500);
            urlConnection.connect();
            mjpegInputStream = new MjpegInputStream(urlConnection.getInputStream());
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        bufferedImage = (BufferedImage) mjpegInputStream.readMjpegFrame().getImage();
                        repaint();
                    } catch (SocketException | IIOException e) {
                        noCamera();
                        timer.cancel();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 1000 / 20);
        } catch (IOException e) {
            noCamera();
            e.printStackTrace();
        }
    }

    private void noCamera() {
        try {
            bufferedImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("no-camera.jpeg"));
            repaint();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

//    private byte[] retrieveNextImage() throws IOException {
//        byte[] imageBytes = new byte[stream.available() + 1];
//        if (imageBytes.length > 1) {
//            while ((stream.read()) != 255) {
//                // Skip
//            }
//            int offset = 1;
//            while (offset < imageBytes.length) {
//                // Read
//                offset += stream.read(imageBytes, offset, imageBytes.length - offset);
//            }
//        }
//        imageBytes[0] = (byte) 255;
//        return imageBytes;
//
//    }
}
