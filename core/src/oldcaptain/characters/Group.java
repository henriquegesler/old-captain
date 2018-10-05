package oldcaptain.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import oldcaptain.LevelManager;
import oldcaptain.OldCaptain;
import oldcaptain.graphics.AgentRenderer;
import oldcaptain.movement.behavior.BattalionTatic;


public class Group {
    public String name;
    public Array<Soldier> soldiers;
    public Array<AgentRenderer> renders;
    public Array<BattalionTatic> availableTatics;
    public int meelee, ranged;
    public BattalionTatic activeTatic;
    public Captain captain;

    public  Group(){
        this.soldiers = new Array<>();
        this.renders = new Array<>();
    }

    public Group(String name, Captain captain, Array<BattalionTatic> availableTatics){
        this.name = name;
        this.captain = captain;
        this.soldiers = new Array<>();
        this.renders = new Array<>();
        this.availableTatics = availableTatics;
        this.activeTatic = new BattalionTatic();
    }

    public void discoverNumbers(){
        for(int i=0; i<soldiers.size; i++){
            if(soldiers.get(i).activeW.meelee){
                meelee++;
            }
            else{
                ranged++;
            }
        }
    }

    public void selectTatic(int tatic){
        this.activeTatic = availableTatics.get(tatic);
    }
}
