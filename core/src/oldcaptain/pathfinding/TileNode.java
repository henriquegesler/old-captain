package oldcaptain.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import oldcaptain.movement.Position;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class TileNode {

    private final Array<Connection<TileNode>> connections = new Array<>();
    private final int index;
    private int ocuppiedByWhom;
    private boolean isObstacle;
    private boolean isOcuppied;
    private boolean isWater;
    public Position position;
    
    public TileNode() {
        index = Incrementer.nextIndex();
        position = new Position(Vector2.Zero);
        isOcuppied = false;
    }
    
    public int getIndex() {
        return index;
    }

    public boolean isOcuppied(){ return isOcuppied; }

    public int ocuppiedBy(){ return ocuppiedByWhom; }

    public void setIsOcuppied(int whom){
        this.isOcuppied = true;
        this.ocuppiedByWhom = whom;
    }

    public void isNotOcuppied(){
        this.isOcuppied = false;
        this.ocuppiedByWhom = -1;
    }

    /**
     * @return the isObstacle
     */
    public boolean isObstacle() {
        return isObstacle;
    }

    /**
     * @param isObstacle the isObstacle to set
     */
    public void setIsObstacle(boolean isObstacle) {
        this.isObstacle = isObstacle;
    }

    /**
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    public Array<Connection<TileNode>> getConnections() {
        return connections;
    }

    public boolean isWater() {
        return isWater;
    }
    
    public void setIsWater(boolean water) {
        isWater = water;
    }

    public static void resetIncrementer() {
        Incrementer.id = 0;
    }
    
    private static class Incrementer {
        private static int id = 0;
        public static int nextIndex() {
            return id++;
        }
    }
    
    public void createConnection(TileNode to, float cost) {
        connections.add(new TileConnection(this, to, cost));
    }
}
