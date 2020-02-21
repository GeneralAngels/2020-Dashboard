package com.ga2230.dashboard.graphics;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import jdk.nashorn.internal.objects.Global;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.mrl.RtspMrl;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;

public class Stream extends Panel {
    private static final int PORT = 554;
    //    private static final String ADDRESS = "10.22.30.21";
    private static final String ADDRESS = "127.0.0.1";

    private EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public Stream() {

        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
                add(mediaPlayerComponent);
            }
        });

    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        if (mediaPlayerComponent != null) {
            Dimension dimension = new Dimension(width, height);
            mediaPlayerComponent.setSize(dimension);
            mediaPlayerComponent.setMinimumSize(dimension);
            mediaPlayerComponent.setMaximumSize(dimension);
        }
    }

    public void play(){
        mediaPlayerComponent.getMediaPlayer().playMedia(new RtspMrl().host(ADDRESS).port(PORT).path("/").value());
    }
}
