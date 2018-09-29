package oldcaptain.movement.behavior;

import oldcaptain.movement.Position;
import oldcaptain.movement.Target;
import oldcaptain.movement.Steering;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public abstract class Algorithm {

    public float maxSpeed;
    public Target target;
    public char name;

    public Algorithm(char name) {
        this(name, false);
    }

    public Algorithm(char nome, boolean protege) {
        this.name = nome;
    }

    public abstract Steering steer(Position agent);
}
