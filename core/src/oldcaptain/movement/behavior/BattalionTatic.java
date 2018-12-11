package oldcaptain.movement.behavior;

import com.badlogic.gdx.utils.Array;
import oldcaptain.characters.Enemy;
import oldcaptain.characters.Group;
import oldcaptain.characters.Soldier;


import static oldcaptain.OldCaptain.group2;

public class BattalionTatic {
    public int id;
    String name, description;
    Array<Soldier> roles;

    public BattalionTatic() {

    }

    public BattalionTatic(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.description = desc;
    }

    public void takeAction(Group formation) {
        if (formation.captain.active) {
            if (this.id == 0) {
                if (formation.soldiers.size > 0 && formation.soldiers.get(0).inRange == false) {
                    formation.soldiers.get(0).setGoal(formation.captain.desiredPosition.x + 32, formation.captain.position.coords.y - 32, 2);
                }
                if (formation.soldiers.size > 1 && formation.soldiers.get(1).inRange == false) {
                    formation.soldiers.get(1).setGoal(formation.captain.desiredPosition.x + 32, formation.captain.desiredPosition.y, 2);
                }
                if (formation.soldiers.size > 2 && formation.soldiers.get(2).inRange == false) {
                    formation.soldiers.get(2).setGoal(formation.captain.desiredPosition.x + 32, formation.captain.desiredPosition.y + 32, 2);
                }
                if (formation.soldiers.size > 3 && formation.soldiers.get(3).inRange == false) {
                    formation.soldiers.get(3).setGoal(formation.captain.desiredPosition.x, formation.captain.desiredPosition.y + 32, 2);
                }
                if (formation.soldiers.size > 4 && formation.soldiers.get(4).inRange == false) {
                    formation.soldiers.get(4).setGoal(formation.captain.desiredPosition.x, formation.captain.desiredPosition.y - 32, 2);
                }
                if (formation.soldiers.size > 5 && formation.soldiers.get(5).inRange == false) {
                    formation.soldiers.get(5).setGoal(formation.captain.desiredPosition.x - 32, formation.captain.desiredPosition.y - 32, 2);
                }
                if (formation.soldiers.size > 6 && formation.soldiers.get(6).inRange == false) {
                    formation.soldiers.get(6).setGoal(formation.captain.desiredPosition.x - 32, formation.captain.desiredPosition.y, 2);
                }
                if (formation.soldiers.size > 7 && formation.soldiers.get(7).inRange == false) {
                    formation.soldiers.get(7).setGoal(formation.captain.desiredPosition.x - 32, formation.captain.desiredPosition.y + 32, 2);
                }

            } else if (this.id == 1) {
                if (formation.soldiers.size > 0 && !formation.soldiers.get(0).inRange) {
                    formation.soldiers.get(0).setGoal(formation.captain.position.coords.x, formation.captain.position.coords.y + 64, 2);
                }
                if (formation.soldiers.size > 1 && !formation.soldiers.get(1).inRange) {
                    formation.soldiers.get(1).setGoal(formation.captain.position.coords.x, formation.captain.position.coords.y + 32, 2);
                }
                if (formation.soldiers.size > 2 && !formation.soldiers.get(2).inRange) {
                    formation.soldiers.get(2).setGoal(formation.captain.position.coords.x, formation.captain.position.coords.y - 32, 2);
                }
                if (formation.soldiers.size > 3 && !formation.soldiers.get(3).inRange) {
                    formation.soldiers.get(3).setGoal(formation.captain.position.coords.x, formation.captain.position.coords.y - 64, 2);
                }
                if (formation.soldiers.size > 4 && !formation.soldiers.get(4).inRange) {
                    formation.soldiers.get(4).setGoal(formation.captain.position.coords.x - 32, formation.captain.position.coords.y + 64, 2);
                }
                if (formation.soldiers.size > 5 && !formation.soldiers.get(5).inRange) {
                    formation.soldiers.get(5).setGoal(formation.captain.position.coords.x - 32, formation.captain.position.coords.y + 32, 2);
                }
                if (formation.soldiers.size > 6 && !formation.soldiers.get(6).inRange) {
                    formation.soldiers.get(6).setGoal(formation.captain.position.coords.x - 32, formation.captain.position.coords.y, 2);
                }
                if (formation.soldiers.size > 7 && !formation.soldiers.get(7).inRange) {
                    formation.soldiers.get(7).setGoal(formation.captain.position.coords.x - 32, formation.captain.position.coords.y - 32, 2);
                }
            } else if (this.id == 3) {
                if (formation.soldiers.size > 0 && !formation.soldiers.get(0).inRange) {
                    formation.soldiers.get(0).setGoal(formation.captain.position.coords.x + 32, formation.captain.position.coords.y - 32, 2);
                }
                if (formation.soldiers.size > 1 && !formation.soldiers.get(1).inRange) {
                    formation.soldiers.get(1).setGoal(formation.captain.position.coords.x + 32, formation.captain.position.coords.y + 0, 2);
                }
                if (formation.soldiers.size > 2 && !formation.soldiers.get(2).inRange) {
                    formation.soldiers.get(2).setGoal(formation.captain.position.coords.x + 32, formation.captain.position.coords.y + 32, 2);
                }
                if (formation.soldiers.size > 3 && !formation.soldiers.get(3).inRange) {
                    formation.soldiers.get(3).setGoal(formation.captain.position.coords.x + 32, formation.captain.position.coords.y + 64, 2);
                }
                if (formation.soldiers.size > 4 && !formation.soldiers.get(4).inRange) {
                    formation.soldiers.get(4).setGoal(formation.captain.position.coords.x, formation.captain.position.coords.y - 64, 2);
                }
                if (formation.soldiers.size > 5 && !formation.soldiers.get(5).inRange) {
                    formation.soldiers.get(5).setGoal(formation.captain.position.coords.x, formation.captain.position.coords.y - 32, 2);
                }
                if (formation.soldiers.size > 6 && !formation.soldiers.get(6).inRange) {
                    formation.soldiers.get(6).setGoal(formation.captain.position.coords.x, formation.captain.position.coords.y + 32, 2);
                }
                if (formation.soldiers.size > 7 && !formation.soldiers.get(7).inRange) {
                    formation.soldiers.get(7).setGoal(formation.captain.position.coords.x, formation.captain.position.coords.y + 64, 2);
                }
            }
        }
    }
}
