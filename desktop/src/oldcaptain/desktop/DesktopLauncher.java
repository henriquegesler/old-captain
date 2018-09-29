package oldcaptain.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import oldcaptain.OldCaptain;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new OldCaptain(), config);
        config.width = 1856;
        config.height = 884;
        config.fullscreen = false;
        config.resizable = false;
    }
}
