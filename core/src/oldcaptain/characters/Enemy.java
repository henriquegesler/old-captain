package oldcaptain.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import oldcaptain.graphics.AgentRenderer;
import oldcaptain.itens.Weapon;

import static oldcaptain.OldCaptain.batch;
import static oldcaptain.OldCaptain.camera;

public class Enemy extends Soldier {
    public Enemy(Weapon meelee,Weapon ranged, Vector2 position, Color color) {
        super(meelee,ranged, position, color);
        this.soldierRenderer = new AgentRenderer(batch, camera, new Texture("warriors-enemy.png"));
    }
}
