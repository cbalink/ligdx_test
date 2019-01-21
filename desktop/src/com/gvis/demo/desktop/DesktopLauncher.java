package com.gvis.demo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gvis.demo.Core;

public class DesktopLauncher {
    public static void main(String[] arg) {


        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = (int) Core.V_WIDTH;
        config.height = (int) Core.V_HEIGHT;
        config.samples = 6;
        config.resizable = false;
        new LwjglApplication(new Core(), config);
    }
}
