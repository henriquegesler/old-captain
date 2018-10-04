package oldcaptain.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import oldcaptain.LevelManager;
import oldcaptain.OldCaptain;
import oldcaptain.graphics.AgentRenderer;
import oldcaptain.movement.behavior.BattalionTatic;

import java.util.ArrayList;

public class Group {
    public String name;
    public ArrayList<Soldier> soldiers;
    public ArrayList<AgentRenderer> renders;
    public ArrayList<BattalionTatic> availableTatics;
    public int meelee, ranged;
    public BattalionTatic activeTatic;
    public Captain captain;

    public  Group(){
        this.soldiers = new ArrayList<>();
        this.renders = new ArrayList<>();
    }

    public Group(String name, Captain captain, ArrayList<BattalionTatic> availableTatics){
        this.name = name;
        this.captain = captain;
        this.soldiers = new ArrayList<>();
        this.renders = new ArrayList<>();
        this.availableTatics = availableTatics;
        this.activeTatic = new BattalionTatic();
    }

    public void discoverNumbers(){
        for(int i=0; i<soldiers.size(); i++){
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
