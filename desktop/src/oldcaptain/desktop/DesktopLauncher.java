package oldcaptain.desktop;

/**
 * @author fegemo <coutinho@decom.cefetmg.br>
 *     @edited by henriquegesler <henriquegesler@gmail.com> on 29/09/2018
 */

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import oldcaptain.OldCaptain;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new OldCaptain(), config);
        config.width = 1856;
        config.height = 896;
        config.fullscreen = false;
//        OldCaptain.tamanho(1856, 896);
        config.resizable = true;
    }
}
