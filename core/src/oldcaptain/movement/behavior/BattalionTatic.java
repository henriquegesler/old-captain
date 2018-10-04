package oldcaptain.movement.behavior;

import oldcaptain.characters.Enemy;
import oldcaptain.characters.Group;
import oldcaptain.characters.Soldier;

import java.util.ArrayList;

import static oldcaptain.OldCaptain.group2;

public class BattalionTatic {
    public int id;
    String name, description;
    ArrayList<Soldier> roles;
    public  BattalionTatic(){

    }
    public BattalionTatic(int id, String name, String desc){
        this.id = id;
        this.name = name;
        this.description = desc;
    }

    public void takeAction(Group formation){
        if(this.id == 0){
            formation.soldiers.get(0).setGoal(formation.captain.position.coords.x+32,formation.captain.position.coords.y,2);
            formation.soldiers.get(1).setGoal(formation.captain.position.coords.x+32,formation.captain.position.coords.y+32,2);
            formation.soldiers.get(2).setGoal(formation.captain.position.coords.x+32,formation.captain.position.coords.y-32,2);
            formation.soldiers.get(3).setGoal(formation.captain.position.coords.x,formation.captain.position.coords.y+32,2);
            formation.soldiers.get(4).setGoal(formation.captain.position.coords.x,formation.captain.position.coords.y-32,2);
            formation.soldiers.get(5).setGoal(formation.captain.position.coords.x-32,formation.captain.position.coords.y+32,2);
            formation.soldiers.get(6).setGoal(formation.captain.position.coords.x-32,formation.captain.position.coords.y-32,2);
            formation.soldiers.get(7).setGoal(formation.captain.position.coords.x-32,formation.captain.position.coords.y,2);

        }
        else if(this.id == 1){
            formation.soldiers.get(0).setGoal(formation.captain.position.coords.x,formation.captain.position.coords.y+64,2);
            formation.soldiers.get(1).setGoal(formation.captain.position.coords.x,formation.captain.position.coords.y+32,2);
            formation.soldiers.get(2).setGoal(formation.captain.position.coords.x,formation.captain.position.coords.y-32,2);
            formation.soldiers.get(3).setGoal(formation.captain.position.coords.x,formation.captain.position.coords.y-64,2);
            formation.soldiers.get(4).setGoal(formation.captain.position.coords.x-32,formation.captain.position.coords.y+64,2);
            formation.soldiers.get(5).setGoal(formation.captain.position.coords.x-32,formation.captain.position.coords.y+32,2);
            formation.soldiers.get(6).setGoal(formation.captain.position.coords.x-32,formation.captain.position.coords.y,2);
            formation.soldiers.get(7).setGoal(formation.captain.position.coords.x-32,formation.captain.position.coords.y-32,2);
        }

    }
}
