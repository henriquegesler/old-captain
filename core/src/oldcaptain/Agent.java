package oldcaptain;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import oldcaptain.characters.Group;
import oldcaptain.characters.Soldier;
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
import static oldcaptain.LevelManager.graph;

import static oldcaptain.LevelManager.totalPixelHeight;
import static oldcaptain.LevelManager.totalPixelWidth;
import static oldcaptain.OldCaptain.counterAgents;
import static oldcaptain.OldCaptain.march;
import static oldcaptain.OldCaptain.stage2Level;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class Agent{

    public Position position;
    protected final Algorithm seek;
    private IndexedAStarPathFinder pathFinder;
    private final DefaultGraphPath<TileConnection> path;
    private Iterator<TileConnection> pathIterator;
    private final Target steeringTarget;
    private final float fullSpeed = 75;
    private static final float MIN_DISTANCE_CONSIDERED_ZERO_SQUARED = (float) Math.pow(2.0f, 2);
    private Facing facing;
    protected TileNode nextNode;
    protected TileNode currentNode;
    public LevelManager levelManager;
    public Soldier engagedEnemy;

    public Color color;
    public boolean shouldMove, captain, inRange;
    public int id, waiting=0;

    public Agent(Vector2 position, Color color) {
        counterAgents++;
        this.id = counterAgents;
        this.position = new Position(position);
        LevelManager.graph.getNodeAtCoordinates((int) Math.floor(position.x),(int) Math.floor(position.y)).setIsOcuppied(this.id);
        this.color = color;
        this.steeringTarget = new Target(position);
        this.seek = new Seek(fullSpeed);
        this.seek.target = steeringTarget;
        this.path = new DefaultGraphPath<>();
        this.pathIterator = this.path.iterator();
        this.facing = Facing.SOUTH;
        this.shouldMove = false;
        this.captain = false;
        this.currentNode = LevelManager.graph.getNodeAtCoordinates((int) Math.floor(position.x),(int) Math.floor(position.y));

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
    public void update(Array<Group> all, float delta, Array<Group> enemies) {
        // verifica se tem um proximo lugar pra ir
        if(nextNode != null){
            if(nextNode.isOcuppied() && nextNode.ocuppiedBy() != this.id){
                shouldMove = false;
                //Essa interação serve para não atravessar personagens em combate
                if(all != null) {
                    for (int i = 0; i < all.size; i++) {
                        for (int j = 0; j < all.get(i).soldiers.size; j++) {
                            if (all.get(i).soldiers.get(j).id == nextNode.ocuppiedBy()) {
                                Soldier soldier = all.get(i).soldiers.get(j);
                                if (!soldier.inRange) {
                                    waiting++;
                                }
                            }
                        }
                    }
                }
                else{
                    waiting++;
                }
            }
            else{
                shouldMove = true;
            }
        }
        //verifica se é capitao e se seu time esta perto ou longe
        if(captain) {
            this.verificaFormacao();
        }
        this.escolheAlvo(enemies);
        // verifica se atingiu o objetivo imediato
        if (position.coords.dst2(steeringTarget.coords) < MIN_DISTANCE_CONSIDERED_ZERO_SQUARED) {
            // procurar se temos outra conexão na nossa rota
            // e, caso afirmativo, definir o nó de chegada como novo target
            if (shouldMove = pathIterator.hasNext()) {
                if(waiting==0){
                    TileConnection nextConnection = pathIterator.next();
                    nextNode = nextConnection.getToNode();
                    steeringTarget.coords = nextNode.getPosition().coords;
                    //verficamos se o nó para onde vamos está ocupado
                    //caso esteja, ele ficará parado
                    if(nextNode.isOcuppied()){
                        shouldMove = false;
                        //Essa interação serve para não atravessar personagens em combate
                        if(all != null) {
                            for (int i = 0; i < all.size; i++) {
                                for (int j = 0; j < all.get(i).soldiers.size; j++) {
                                    if (all.get(i).soldiers.get(j).id == nextNode.ocuppiedBy()) {
                                        Soldier soldier = all.get(i).soldiers.get(j);
                                        if (!soldier.inRange) {
                                            waiting++;
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            waiting++;
                        }
                    }
                    //Caso não esteja, liberamos o espaço original
                    //e habilitamos para que ele se mova para o próximo nó
                    else{
                        if(captain){
                            this.verificaFormacao();
                            if(shouldMove){
                                nextNode.setIsOcuppied(this.id);
                                waiting = 0;
                            }
                        }
                        else{
                            nextNode.setIsOcuppied(this.id);
                            waiting = 0;
                        }
                    }
                    // atualiza a velocidade do "seek" de acordo com o terreno (a conexão)
                    this.seek.maxSpeed = fullSpeed - (fullSpeed / 2.0f) * (nextConnection.getCost() - 1) / (this.levelManager.maxCost - 1);
                }
                else{
                    if(nextNode.isOcuppied()){
                        shouldMove = false;
                        waiting++;
                    }
                    else{
                        nextNode.setIsOcuppied(this.id);
                        waiting = 0;
                    }
                }
            }
        } else if (position.coords.dst2(steeringTarget.coords) < MIN_DISTANCE_CONSIDERED_ZERO_SQUARED * 6) {
            if(currentNode != null){
                currentNode.isNotOcuppied();
            }
            currentNode = nextNode;
            currentNode.setIsOcuppied(this.id);
        }

        if(waiting>35){
            if(shouldMove = pathIterator.hasNext()){
                TileConnection nextConnection = pathIterator.next();
                nextNode = nextConnection.getToNode();
                steeringTarget.coords = nextNode.getPosition().coords;
                nextNode.setIsOcuppied(this.id);
                waiting = 0;
                this.seek.maxSpeed = fullSpeed - (fullSpeed / 2.0f) * (nextConnection.getCost() - 1) / (this.levelManager.maxCost - 1);
            }
        }

        // integra
        if (shouldMove) {

            Steering steering = seek.steer(this.position);
            //march.play(0.1f);
            position.integrate(steering,delta);

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
                .getNodeAtCoordinates((int) Math.floor(x), (int) Math.floor(y));
        path.clear();
        pathFinder.metrics.reset();
        // Finding a new nearest target from target for an obstacle target
        try {
            if((targetNode.isOcuppied() || targetNode.isObstacle())){
                //targetNode = newPosition2(targetNode,startNode);
            }
            // AQUI ESTAMOS CHAMANDO O ALGORITMO A* (instância pathFinder) 
            if (heuristic == 1) {
                pathFinder.searchConnectionPath(startNode, targetNode,
                        new Heuristic<TileNode>() {
                    public float estimate(TileNode n, TileNode n1) {
                        return 0;
                    }
                }, path);
            }
            if (heuristic == 2) {
                pathFinder.searchConnectionPath(startNode, targetNode,
                        new Heuristic<TileNode>() {
                    public float estimate(TileNode n, TileNode n1) {
                        float x, y;
                        x = n1.getPosition().coords.x - n.getPosition().coords.x;
                        y = n1.getPosition().coords.y - n.getPosition().coords.y;
                        return (float) Math.sqrt(Math.pow((double) y, 2) + Math.pow((double) x, 2)) / 32;
                    }
                }, path);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
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
        float totalDistance2 = currentNode.getPosition().coords
                .dst2(nextNode.getPosition().coords);
        float remainingDistance2 = position.coords.dst2(nextNode.getPosition().coords);
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

    public TileNode newPosition(TileNode target, TileNode startNode){
        TileNode t;
        Vector2 v = new Vector2();
        int k = 0;
        if(!target.equals(startNode)){
            while ((target.isOcuppied() && target.ocuppiedBy()!=this.id) || target.isObstacle()) {
                int exp;
                float xInicial, yInicial;
                exp = 3 + (k * 2);
                xInicial = startNode.position.coords.x - 32 * (1 + k);
                yInicial = startNode.position.coords.y - 32 * (1 + k);
                /**
                 * Iteração que verifica as linhas acima e abaixo do alvo
                 * A cada novo k, sobe um posição e desce uma posiçao, de forma que não verifique os mesmos
                 * quadrados já verificados num k anterior.
                 * */
                for (int j = 0; j < 2; j++) {
                    v.y = yInicial + 32 * (j * 2 * (1 + k));
                    if (v.y >= 0 && v.y <= totalPixelHeight) {
                        for (int i = 0; i < exp; i++) {
                            v.x = xInicial + 32 * i;
                            if (v.x >= 0 && v.x <= totalPixelWidth) {
                                t = this.levelManager.graph.getNodeAtCoordinates((int) Math.floor(v.x), (int) Math.floor(v.y));
                                if (!t.isObstacle() & (!t.isOcuppied() || t.ocuppiedBy()==this.id)) {
                                    target = t;
                                    i = exp;
                                    j = exp;
                                }
                            }
                        }
                    }
                }
                /**
                 * Iteração que verifica as colunas a esquerda e a direita do alvo
                 * A cada novo k, esquerda uma posição e vai para a direita, de forma que não verifique os mesmos
                 * quadrados já verificados num k anterior. X estático, lê as colunas
                 * */
                yInicial = startNode.position.coords.y - 32 * (k);
                for (int j = 0; j < 2; j++) {
                    v.x = xInicial + 32 * (j * 2 * (1 + k));
                    if (v.x >= 0 && v.x <= totalPixelWidth) {
                        for (int i = 0; i < (exp-2); i++) {
                            v.y = yInicial + 32 * i;
                            if (v.y >= 0 && v.y <= totalPixelHeight) {
                                t = this.levelManager.graph.getNodeAtCoordinates((int) Math.floor(v.x), (int) Math.floor(v.y));
                                if (!t.isObstacle() & (!t.isOcuppied() || t.ocuppiedBy()==this.id)) {
                                    target = t;
                                    i = exp;
                                    j = exp;
                                }
                            }
                        }
                    }
                }
                k++;
            }
            return target;
        }
        else return startNode;
    }

    public TileNode newPosition2(TileNode target, TileNode startNode){
        TileNode t;
        Vector2 v = new Vector2();
        int k = 0;
        float distancia=0;
       // if(target != startNode){
            //while ((target.isOcuppied() && target.ocuppiedBy()!=this.id) || target.isObstacle()) {
                int exp;
                boolean primeiro=false;
                float xInicial, yInicial;
                exp = 3 + (k * 2);
                xInicial = startNode.position.coords.x - 32 * (1 + k);
                yInicial = startNode.position.coords.y - 32 * (1 + k);
                /**
                 * Iteração que verifica as linhas acima e abaixo do alvo
                 * A cada novo k, sobe um posição e desce uma posiçao, de forma que não verifique os mesmos
                 * quadrados já verificados num k anterior.
                 * */
                for (int j = 0; j < 2; j++) {
                    v.y = yInicial + 32 * (j * 2 * (1 + k));
                    if (v.y >= 0 && v.y <= totalPixelHeight) {
                        for (int i = 0; i < exp; i++) {
                            v.x = xInicial + 32 * i;
                            if (v.x >= 0 && v.x <= totalPixelWidth) {
                                t = this.levelManager.graph.getNodeAtCoordinates((int) Math.floor(v.x), (int) Math.floor(v.y));
                                if (!t.isObstacle() && (!t.isOcuppied() || t.ocuppiedBy()==this.id)) {
                                    if(!primeiro){
                                        distancia = t.position.coords.dst(this.engagedEnemy.position.coords);
                                        primeiro = true;
                                    }
                                    if(t.position.coords.dst(this.engagedEnemy.position.coords) <= distancia){
                                        target = t;
                                        distancia = t.position.coords.dst(this.engagedEnemy.position.coords);
                                        i = exp;
                                        j = exp;
                                    }
                                }
                            }
                        }
                    }
                }
                /**
                 * Iteração que verifica as colunas a esquerda e a direita do alvo
                 * A cada novo k, esquerda uma posição e vai para a direita, de forma que não verifique os mesmos
                 * quadrados já verificados num k anterior. X estático, lê as colunas
                 * */
                yInicial = startNode.position.coords.y - 32 * (k);
                for (int j = 0; j < 2; j++) {
                    v.x = xInicial + 32 * (j * 2 * (1 + k));
                    if (v.x >= 0 && v.x <= totalPixelWidth) {
                        for (int i = 0; i < (exp-2); i++) {
                            v.y = yInicial + 32 * i;
                            if (v.y >= 0 && v.y <= totalPixelHeight) {
                                t = this.levelManager.graph.getNodeAtCoordinates((int) Math.floor(v.x), (int) Math.floor(v.y));
                                if (!t.isObstacle() && (!t.isOcuppied() || t.ocuppiedBy()==this.id)) {
                                    if(!primeiro){
                                        distancia = t.position.coords.dst(this.engagedEnemy.position.coords);
                                        primeiro = true;
                                    }
                                    if(t.position.coords.dst(this.engagedEnemy.position.coords) <= distancia){
                                        target = t;
                                        distancia = t.position.coords.dst(this.engagedEnemy.position.coords);
                                        i = exp;
                                        j = exp;
                                    }
                                }
                            }
                        }
                    }
                }
                //k++;
            //}
            return target;
       // }
        //else return startNode;
    }

    public void verificaFormacao(){}

    public void escolheAlvo(Array<Group> enemies){}
}
