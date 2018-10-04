package oldcaptain;

import com.badlogic.gdx.maps.tiled.TiledMap;
import oldcaptain.graphics.Facing;
import oldcaptain.movement.Position;
import oldcaptain.movement.Steering;
import oldcaptain.movement.Target;
import oldcaptain.movement.behavior.Algorithm;
import oldcaptain.movement.behavior.Seek;
import oldcaptain.pathfinding.TileConnection;
import oldcaptain.pathfinding.TileNode;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder.Metrics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.Iterator;

import static oldcaptain.OldCaptain.counterAgents;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class Agent {

    public static float MAX_MOVE = 9.0f;
    public static int MAX_ACTION = 1;
    public int actualAction;
    public float actualMove;
    public Position position;
    protected final Algorithm seek;
    private IndexedAStarPathFinder pathFinder;
    private final DefaultGraphPath<TileConnection> path;
    private Iterator<TileConnection> pathIterator;
    private final Target steeringTarget;
    private final float fullSpeed = 75;
    private static final float MIN_DISTANCE_CONSIDERED_ZERO_SQUARED = (float) Math.pow(2.0f, 2);
    private Facing facing;
    private TileNode nextNode, currentNode;
    public LevelManager levelManager;

    public Color color;
    private boolean shouldMove;

    public Agent(Vector2 position, Color color) {
        counterAgents++;
        this.position = new Position(position);
        this.color = color;
        this.steeringTarget = new Target(position);
        this.seek = new Seek(fullSpeed);
        this.seek.target = steeringTarget;
        this.path = new DefaultGraphPath<>();
        this.pathIterator = this.path.iterator();
        this.facing = Facing.SOUTH;
        this.shouldMove = false;
        this.actualMove = MAX_MOVE;
        this.actualAction = MAX_ACTION;

    }

    public Position getPosition(){
        return this.position;
    }

    public void setLevelManager(LevelManager levelManager){
        this.levelManager = levelManager;
        this.pathFinder = new IndexedAStarPathFinder(this.levelManager.graph, true);
    }

    /**
     * Atualiza a posição do personagem de acordo com seu objetivo de alto nível
     * (pathfinding).
     *
     * @param delta tempo desde a última atualização.
     */
    public void update(float delta) {
        shouldMove = true;

        // verifica se atingimos nosso objetivo imediato
        if (position.coords.dst2(steeringTarget.coords) < MIN_DISTANCE_CONSIDERED_ZERO_SQUARED) {
            // procurar se temos outra conexão na nossa rota
            // e, caso afirmativo, definir o nó de chegada como novo target
            if (shouldMove = pathIterator.hasNext()) {
                TileConnection nextConnection = pathIterator.next();
                nextNode = nextConnection.getToNode();
                steeringTarget.coords = nextNode.getPosition();


                // atualiza a velocidade do "seek" de acordo com o terreno (a conexão)
                this.seek.maxSpeed = fullSpeed - (fullSpeed / 2.0f) * (nextConnection.getCost() - 1) / (this.levelManager.maxCost - 1);
            }
        } else if (position.coords.dst2(steeringTarget.coords) < MIN_DISTANCE_CONSIDERED_ZERO_SQUARED * 6) {
            currentNode = nextNode;
        }

        // integra
        if (shouldMove && actualMove > 0) {
            /**
             * Mudança será feita aqui
             * by: Henrique Gesler 10/2018
             */
            Steering steering = seek.steer(this.position);
            if(pathIterator.hasNext()){
                TileConnection nextConnection = pathIterator.next();
                actualMove -= nextConnection.getCost();
            }
            position.integrate(steering, delta);

            // verifica o vetor velocidade para determinar a orientação
            float angle = steering.velocity.angle();
            int quadrant = (int) (((int) angle + (360 - 67.5f)) / 45) % 8;
            facing = Facing.values()[(8 - quadrant) % 8];
        }
    }

    /**
     * Este método é chamado quando um clique no mapa é realizado.
     *  @param x coordenada x do ponteiro do mouse.
     * @param y coordenada y do ponteiro do mouse.
     */
    public void setGoal(float x, float y, int heuristic) {
        TileNode startNode = this.levelManager.graph
                .getNodeAtCoordinates(
                        (int) this.position.coords.x,
                        (int) this.position.coords.y);
        TileNode targetNode = this.levelManager.graph
                .getNodeAtCoordinates((int) x, (int) y);

        path.clear();
        pathFinder.metrics.reset();
        // Finding a new nearest target from target for an obstacle target
        TileNode target = targetNode;
        int k=0;
        while (target.isObstacle()){
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(target.isObstacle()){
                        target = this.levelManager.graph.getNodeAtCoordinates(
                                (int )targetNode.getPosition().x +32-(32*i),(int) targetNode.getPosition().y-(32*(1+k))+(32*(j+k)));
                    }
                }
            }
            k++;
        }
        targetNode = target;
        // AQUI ESTAMOS CHAMANDO O ALGORITMO A* (instância pathFinder) 
        if(heuristic == 1){
            pathFinder.searchConnectionPath(startNode, targetNode,
                    new Heuristic<TileNode>() {
                public float estimate(TileNode n, TileNode n1){
                    return 0;
                }
            }, path);
        }
        if(heuristic == 2){
            pathFinder.searchConnectionPath(startNode, targetNode,
                    new Heuristic<TileNode>() {
                public float estimate(TileNode n, TileNode n1){
                    float x, y;
                    x = n1.getPosition().x - n.getPosition().x;
                    y = n1.getPosition().y - n.getPosition().y;
                    return (float) Math.sqrt(Math.pow((double)y,2)+Math.pow((double)x,2))/32;
                }
            }, path);
        }
        if(heuristic == 3){
            pathFinder.searchConnectionPath(startNode, targetNode,
                    new Heuristic<TileNode>() {
                public float estimate(TileNode n, TileNode n1){
                    float x, y;
                    x = (float) ((float) Math.ceil(n1.getPosition().x/32) - Math.ceil(n.getPosition().x/32));
                    y = (float) ((float) Math.ceil(n1.getPosition().y/32) - Math.ceil(n.getPosition().y/32));
                    return Math.max(x, y);
                }
            }, path);
        }
        pathIterator = path.iterator();
    }

    /**
     * Retorna em que direção (das 8) o personagem está olhando.
     *
     * @return a direção de orientação.
     */
    public Facing getFacing() {
        return facing;
    }

    /**
     * Retorna se o personagem está se movimentando ou se está parado.
     *
     * @return
     */
    public boolean isMoving() {
        return shouldMove;
    }

    /**
     * Retorna se o personagem está em um tile de água.
     *
     * @return
     */
    public boolean isUnderWater() {
        return currentNode != null && nextNode != null && (currentNode.isWater() || nextNode.isWater());
    }

    private float getPercentageOfNodeTraversalConcluded() {
        float totalDistance2 = currentNode.getPosition()
                .dst2(nextNode.getPosition());
        float remainingDistance2 = position.coords.dst2(nextNode.getPosition());
        return (totalDistance2 - remainingDistance2) / totalDistance2;
    }

    public float getUnderWaterLevel() {
        if (currentNode != null && nextNode != null) {
            if (currentNode.isWater() && nextNode.isWater()) {
                // vai continuar na água
                return 1;
            } else if (currentNode.isWater() && !nextNode.isWater()) {
                // está saindo da água
                return 1 - getPercentageOfNodeTraversalConcluded();
            } else if (nextNode.isWater() && !currentNode.isWater()) {
                // está entrando na água
                return getPercentageOfNodeTraversalConcluded();
            }
        }
        return 0;
    }

    /**
     * Retorna as métricas da última execução do algoritmo de planejamento de
     * trajetórias.
     *
     * @return as métricas.
     */
    public Metrics getPathFindingMetrics() {
        return pathFinder.metrics;
    }

    /**
     * Acrescentando a função: resetaMove e resetaAction
     * by: Henrique Gesler 10/2018
     */
    public void resetaMove(){
        actualMove = MAX_MOVE;
    }
    public void resetaAction(){
        actualAction = MAX_ACTION;
    }
}
