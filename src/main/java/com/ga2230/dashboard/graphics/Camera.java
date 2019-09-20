package com.ga2230.dashboard.graphics;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.view.swing.BrowserView;

import javax.swing.*;

public class Camera extends Panel {

    private Engine engine;
    private Browser browser;
    private BrowserView browserView;
    private JButton previous, next;
    private JPanel navigation;

    public Camera(String source) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Browser init
        engine = Engine.newInstance(EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED).build());
        browser = engine.newBrowser();
        browserView = BrowserView.newInstance(browser);
        // Navigation init
        navigation = new JPanel();
        previous = new JButton("◀");
        next = new JButton("▶");
        navigation.setLayout(new BoxLayout(navigation, BoxLayout.X_AXIS));
        navigation.add(previous);
        navigation.add(next);
        add(browserView);
        add(navigation);
    }

    public void load(String cameraUrl) {
        String html = "<html><body><img height=\"100%\" width=\"100%\" src=\"" + cameraUrl + "\"></img></body></html>";
        browser.mainFrame().ifPresent(frame ->
                frame.loadHtml(html)
        );
    }
}