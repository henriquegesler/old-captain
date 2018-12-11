package oldcaptain.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import oldcaptain.graphics.AgentRenderer;
import oldcaptain.itens.Weapon;

import static com.badlogic.gdx.math.MathUtils.random;
import static oldcaptain.OldCaptain.batch;
import static oldcaptain.OldCaptain.camera;

public class Captain extends Soldier {
    public String name;
    public Vector2 desiredPosition;
    public Group myGroup;

    public Captain(String name, Weapon meelee, Weapon ranged, Vector2 position, Color color) {
        super(meelee, ranged, position, color);
        this.name = name;
        this.totalHP = random(30,46);
        this.chanceToDie = random(.5f,1.01f);
        this.partialHP = this.totalHP;
        this.salary = random(4.50f,6.01f);
        this.level = 4;
        this.proficiency = random(8.00f,10.01f);
        soldierRenderer = new AgentRenderer(batch, camera, new Texture("captains.png"));
        desiredPosition = this.position.coords;
        this.captain = true;
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

    @Override
    public void update(Array<Group> all, float delta, Array<Group> enemies) {
        super.update(all, delta, enemies);
        desiredPosition = nextNode.position.coords;
    }

    public float contractCost(){
        float cost = 20;
        return (cost+(proficiency/2.00f)+(totalHP/5.00f)+(salary/2));
    }

    public void setMyGroup(Group g){this.myGroup = g;}

    @Override
    public void verificaFormacao(){
        int nearMe=myGroup.soldiers.size;
        for (int i = 0; i < myGroup.soldiers.size; i++) {
            if(myGroup.soldiers.get(i).position.coords.dst(this.position.coords) > 96){
                nearMe--;
            }
        }
        if(myGroup.soldiers.size*2/3 > nearMe){
            this.shouldMove = false;
        }
    }
}
