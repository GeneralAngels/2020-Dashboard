package com.ga2230.dashboard.graphics;

//import com.teamdev.jxbrowser.browser.Browser;
//import com.teamdev.jxbrowser.engine.Engine;
//import com.teamdev.jxbrowser.engine.EngineOptions;
//import com.teamdev.jxbrowser.engine.RenderingMode;
//import com.teamdev.jxbrowser.view.swing.BrowserView;

import javax.swing.*;
import java.awt.*;

public class Camera extends Panel {

//    private Engine engine;
//    private Browser browser;
//    private BrowserView browserView;
//    private JButton previous, next;
//    private JPanel navigation;
//
//    public Camera(String source) {
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        // Browser init
//        engine = Engine.newInstance(EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED).build());
//        browser = engine.newBrowser();
//        browserView = BrowserView.newInstance(browser);
//        // Navigation init
//        navigation = new JPanel();
//        previous = new JButton("<");
//        next = new JButton(">");
//        previous.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Frame.FONT_SIZE));
//        next.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Frame.FONT_SIZE));
//        navigation.setLayout(new GridLayout(1, 2));
//        navigation.add(previous);
//        navigation.add(next);
//        add(browserView);
//        add(navigation);
//    }
//
//    public void load(String cameraUrl) {
//        String html = "<html><body><img height=\"100%\" width=\"100%\" src=\"" + cameraUrl + "\"></img></body></html>";
//        browser.mainFrame().ifPresent(frame ->
//                frame.loadHtml(html)
//        );
//    }
//
//    @Override
//    public void setSize(int width, int height) {
//        Dimension dimension = new Dimension(width, height / 15);
//        navigation.setPreferredSize(dimension);
//        navigation.setMinimumSize(dimension);
//        navigation.setMaximumSize(dimension);
//        super.setSize(width, height);
//    }
}
