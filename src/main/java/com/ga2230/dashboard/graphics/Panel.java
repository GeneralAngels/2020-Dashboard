package com.ga2230.dashboard.graphics;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    public void setSize(int width, int height){
        Dimension dimension = new Dimension(width, height);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
    }
}
