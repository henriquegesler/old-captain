package oldcaptain.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import oldcaptain.graphics.AgentRenderer;
import oldcaptain.itens.Weapon;

import static com.badlogic.gdx.math.MathUtils.random;
import static oldcaptain.OldCaptain.batch;
import static oldcaptain.OldCaptain.camera;

public class Captain extends Soldier {
    public static int MAX_ACTION=2;
    public String name;

    public Captain(String name, Weapon meelee, Weapon ranged, Vector2 position, Color color) {
        super(meelee, ranged, position, color);
        this.name = name;
        this.totalHP = random(30,46);
        this.chanceToDie = random(.5f,1.01f);
        this.partialHP = this.totalHP;
        this.salary = random(4.50f,6.01f);
        this.level = 4;
        this.proficiency = random(8.00f,10.01f);
        this.actualAction = MAX_ACTION;
        soldierRenderer = new AgentRenderer(batch, camera, new Texture("captains.png"));
    }

    public void levelUP(){
        this.level++;
        this.proficiency += random(0.65f,1.26f);
        this.salary += .75;
        this.totalHP += random(3,10);
        this.partialHP = this.totalHP;
    }

    public void isDead(){
        if(partialHP <= 0){
            active=false;
            float chance=random(10000);
            if(chance<=6750){
                chance*=this.chanceToDie;
                if(chance <= 8200){
                    alive = false;
                }
                else{
                    alive = true;
                }
            }
            else{
                this.chanceToDie += random(0.07f);
                alive = true;
            }
        }
        else{
            active=true;
            alive = true;
        }
    }

    public float contractCost(){
        float cost = 20;
        return (cost+(proficiency/2.00f)+(totalHP/5.00f)+(salary/2));
    }
}
