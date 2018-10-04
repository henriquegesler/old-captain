package oldcaptain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import oldcaptain.characters.Captain;
import oldcaptain.characters.Enemy;
import oldcaptain.characters.Group;
import oldcaptain.characters.Soldier;
import oldcaptain.graphics.GraphRenderer;

import static com.badlogic.gdx.math.MathUtils.random;
import static oldcaptain.OldCaptain.*;

public class MyInputAdapter extends InputAdapter {
    @Override
    public boolean keyUp(int keycode) {
        if(stage == 2){
            if(keycode == Input.Keys.SPACE){
                turno = !turno;
            }
            if (keycode == Input.Keys.LEFT) {
                camera.translate(-32, 0);
                tx-=32;
            }
            if (keycode == Input.Keys.RIGHT) {
                camera.translate(32, 0);
                tx+=32;
            }
            if (keycode == Input.Keys.DOWN) {
                camera.translate(0, -32);
                ty-=32;
            }
            if (keycode == Input.Keys.UP) {
                camera.translate(0, 32);
                ty+=32;

            }
            if (keycode == Input.Keys.ESCAPE) {
                Gdx.app.exit();
            }
            if (keycode == Input.Keys.M) {
                showingMetrics = !showingMetrics;
            }
            if (keycode == Input.Keys.D) {
                debugMode = !debugMode;
            }
        }
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        Vector2 clique = new Vector2(x, y);
        viewport.unproject(clique);
        if (button == Input.Buttons.LEFT){
            if(stage == 0){
                if (y>496 && y<596 && x>55 && x<570){
                    camera.translate(-288,-416);
                    //Cria o mapa
                    stage1Level = new LevelManager();
                    selecMap = stage1Level.LoadLevel("mapa-selecao.tmx");
                    tiledMapRenderer = new OrthogonalTiledMapRenderer(selecMap, batch);
                    graphRenderer = new GraphRenderer(batch, shapeRenderer);
                    graphRenderer.renderGraphToTexture(stage1Level.graph);
                    //Cria os grupos
                    creatorOfCaptains();
                    int rand = random(0,3);
                    group1 = new Group("Grupo 1",avaibleCaptains.get(rand),tatics);
                    avaibleCaptains.remove(rand);
                    rand = random(0,2);
                    group2 = new Group("Grupo 2",avaibleCaptains.get(rand),tatics);
                    avaibleCaptains.remove(rand);

                    for (int i=0;i<8;i++){
                        Soldier soldier = new Soldier(weapons.get(random(0,4)) ,new Vector2(
                                (stage1Level.tileWidth / 2)+16,
                                stage1Level.totalPixelHeight-96),
                                Color.FIREBRICK
                        );
                        soldier.setLevelManager(stage1Level);
                        group1.soldiers.add(soldier);
                        group1.renders.add(group1.soldiers.get(i).soldierRenderer);
                    }
                    group1.soldiers.add(8, group1.captain);
                    group1.renders.add(8, group1.captain.soldierRenderer);

                    for (int i=0;i<8;i++){
                        Soldier soldier = new Soldier(weapons.get(random(0,4)) ,new Vector2(
                                (stage1Level.tileWidth / 2)+16,
                                stage1Level.totalPixelHeight-96),
                                Color.FIREBRICK
                        );

                        soldier.setLevelManager(stage1Level);
                        group2.soldiers.add(soldier);
                        group2.renders.add(group2.soldiers.get(i).soldierRenderer);
                    }
                    group2.soldiers.add(8,group2.captain);
                    group2.renders.add(8, group2.captain.soldierRenderer);
                    activeGroup = null;
                    //Efeito Sonoro Do click
                    mouseclick.play(1.0f);
                    //Chama o stage novo
                    stage=1;
                }
                else if (y>596 && y<696 && x>55 && x<580){
                    mouseclick.play(1.0f);
                }
                else if (y>696 && y<796 && x>55 && x<395){
                    mouseclick.play(1.0f);
                }
                else if (y>796 && y<896 && x>55 && x<195){
                    mouseclick.play(1.0f);
                    Gdx.app.exit();
                }
            }
            else if(stage == 1){
                //Verificação do clique nos quadrados dos personagens
                if(y>775 && y<865 && x>212 && x<302){
                    mouseclick.play(1.0f);
                    q4Sel = 8;
                }
                for(int j=0;j<2;j++){
                    for (int i=0; i<4; i++){
                        if(y>(595+(95*j)) && y<(665+(95*j)) && x>(78+(i*95)) && x<(148+(i*90))){
                            mouseclick.play(1.0f);
                            q4Sel = (j*4)+i;
                        }
                    }
                }
                //Botão para procurar inimigos
                if (y>802 && y<884 && x>1326 && x<1836){
                    activeGroup=null;
                    camera.translate(288,416);
                    stage2Level = new LevelManager();
                    battleMap = stage2Level.LoadLevel("mapaPadrao1.tmx");
                    tiledMapRenderer = new OrthogonalTiledMapRenderer(battleMap, batch);
                    graphRenderer = new GraphRenderer(batch, shapeRenderer);
                    graphRenderer.renderGraphToTexture(stage2Level.graph);
                    mouseclick.play(1.0f);
                    enemy1 = geradorEnemy();
                    enemy2 = geradorEnemy();
                    enemy3 = geradorEnemy();
                    team2.add(enemy1);
                    team2.add(enemy2);
                    team2.add(enemy3);
                    for(int i=0; i<group1.soldiers.size();i++){
                        group1.soldiers.get(i).setLevelManager(stage2Level);
                        group1.soldiers.get(i).setInitialPosition();
                    }
                    group1.captain.setLevelManager(stage2Level);
                    group1.captain.setInitialPosition();
                    for(int i=0; i<group2.soldiers.size();i++){
                        group2.soldiers.get(i).setLevelManager(stage2Level);
                        group2.soldiers.get(i).setInitialPosition2();

                    }
                    group2.captain.setLevelManager(stage2Level);
                    group2.captain.setInitialPosition2();
                    group2.captain.setGoal(128+(stage2Level.tileWidth / 2),
                            ((float) stage2Level.totalPixelHeight/2)+64,2)
                    ;
                    group1.captain.setGoal((stage2Level.tileWidth / 2),
                            ((float) stage2Level.totalPixelHeight/2)+64,2)
                    ;
                    team1.add(group1);
                    team1.add(group2);
                    stage=2;
                }
                //Grupo 1
                else if (y>57 && y<137 && x>540 && x<815){
                    activeGroup = group1;
                    mouseclick.play(1.0f);
                    int a = 2+activeGroup.activeTatic.id;
                    parametro.set(0,2);
                    parametro.set(1,0);
                    for(int i=2;i<12;i++){
                        parametro.set(i,0);
                    }
                    parametro.set(a,2);
                    activeGroup.captain.setGoal(220, 175, heuristic);
                }
                //Grupo 2
                else if (y>57 && y<137 && x>1127 && x<1417){
                    activeGroup = group2;
                    mouseclick.play(1.0f);
                    int a = 7+activeGroup.activeTatic.id;
                    parametro.set(0,0);
                    parametro.set(1,2);
                    for(int i=2;i<12;i++){
                        parametro.set(i,0);
                    }
                    parametro.set(a,2);
                    activeGroup.captain.setGoal(220, 175, heuristic);
                }
                // Táticas batalhão 1
                else if (x>528 && x<876){
                    if(y>204 && y<247){
                        mouseclick.play(1.0f);
                        for(int i=2;i<12;i++){
                            parametro.set(i,0);
                        }
                        activeGroup.selectTatic(0);
                        parametro.set(2,2);
                    }
                    else if(y>251 && y<294){
                        mouseclick.play(1.0f);
                        for(int i=2;i<12;i++){
                            parametro.set(i,0);
                        }
                        activeGroup.selectTatic(1);
                        parametro.set(3,2);
                    }
                    else if(y>298 && y<341){
                        mouseclick.play(1.0f);
                        for(int i=2;i<12;i++){
                            parametro.set(i,0);
                        }
                        activeGroup.selectTatic(1);
                        parametro.set(4,2);
                    }
                    else if(y>346 && y<388){
                        mouseclick.play(1.0f);
                        for(int i=2;i<12;i++){
                            parametro.set(i,0);
                        }
                        activeGroup.selectTatic(1);
                        parametro.set(5,2);
                    }
                    else if(y>391 && y<436){
                        mouseclick.play(1.0f);
                        for(int i=2;i<12;i++){
                            parametro.set(i,0);
                        }
                        activeGroup.selectTatic(1);
                        parametro.set(6,2);
                    }
                }
                // Táticas batalhão 2
                else if (x>1127 && x<1475){
                    if(y>204 && y<247){
                        mouseclick.play(1.0f);
                        for(int i=2;i<12;i++){
                            parametro.set(i,0);
                        }
                        activeGroup.selectTatic(0);
                        parametro.set(7,2);
                    }
                    else if(y>251 && y<294){
                        mouseclick.play(1.0f);
                        for(int i=2;i<12;i++){
                            parametro.set(i,0);
                        }
                        activeGroup.selectTatic(1);
                        parametro.set(8,2);
                    }
                    else if(y>298 && y<341){
                        mouseclick.play(1.0f);
                        for(int i=2;i<12;i++){
                            parametro.set(i,0);
                        }
                        activeGroup.selectTatic(1);
                        parametro.set(9,2);
                    }
                    else if(y>346 && y<388){
                        mouseclick.play(1.0f);
                        for(int i=2;i<12;i++){
                            parametro.set(i,0);
                        }
                        activeGroup.selectTatic(1);
                        parametro.set(10,2);
                    }
                    else if(y>391 && y<436){
                        mouseclick.play(1.0f);
                        for(int i=2;i<12;i++){
                            parametro.set(i,0);
                        }
                        activeGroup.selectTatic(1);
                        parametro.set(11,2);
                    }
                }
            }
            else if(stage == 2) {

            }
        }
        // Botão DIREITO: posiciona objetivo do lider
        else if (button == Input.Buttons.RIGHT){
            if(stage == 0){

            }
            else if(stage == 1){
                //xx = (int) clique.x;
                //yy = (int) clique.y;
            }
            else if(stage == 2) {
                group1.captain.setGoal((int) clique.x, (int) clique.y, heuristic);
                group2.captain.setGoal((int) clique.x, (int) clique.y, heuristic);
            }
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        if(stage == 0){
            if (y>496 && y<596 && x>55 && x<570){
                if(!p1){
                    mouseover.play();
                    p1 = true;
                    p2= false;
                    p3= false;
                    p4= false;
                }
            }
            else if (y>596 && y<696 && x>55 && x<580){
                if(!p2){
                    mouseover.play();
                    p2 = true;
                    p1= false;
                    p3= false;
                    p4= false;
                }
            }
            else if (y>696 && y<796 && x>55 && x<395){
                if(!p3){
                    mouseover.play();
                    p3 = true;
                    p2= false;
                    p1= false;
                    p4= false;
                }
            }
            else if (y>796 && y<896 && x>55 && x<195){
                if(!p4){
                    mouseover.play();
                    p4 = true;
                    p2= false;
                    p3= false;
                    p1= false;
                }
            }
        }
        else if(stage == 1){
            if (y>57 && y<137 && x>540 && x<815){
                if(parametro.get(0) != 2){
                    if(parametro.get(0) != 1){
                        mouseover.play();
                    }
                    parametro.set(0,1);
                }
            }
            else if (y>57 && y<137 && x>1127 && x<1417){
                if(parametro.get(1) != 2){
                    if(parametro.get(1) != 1){
                        mouseover.play();
                    }
                    parametro.set(1,1);
                }
            }
            else{
                for(int i=0;i<parametro.size();i++){
                    if(parametro.get(i) != 2){
                        parametro.set(i,0);
                    }
                }
            }
        }
        else if(stage == 2) {

        }
        return super.mouseMoved(x, y);
    }

    public void creatorOfCaptains(){
        Captain c1 = new Captain("Messias, The Savior",weapons.get(1) ,new Vector2(
                (stage1Level.tileWidth / 2)+16,
                stage1Level.totalPixelHeight-96),
                Color.FIREBRICK
        );
        c1.setLevelManager(stage1Level);
        avaibleCaptains.add(c1);
        Captain c2 = new Captain("Ginger, The Stable Boy",weapons.get(2) ,new Vector2(
                (stage1Level.tileWidth / 2)+16,
                stage1Level.totalPixelHeight-96),
                Color.FIREBRICK
        );
        c2.setLevelManager(stage1Level);
        avaibleCaptains.add(c2);
        Captain c3 = new Captain("Daciolo, Cabo",weapons.get(3) ,new Vector2(
                (stage1Level.tileWidth / 2)+16,
                stage1Level.totalPixelHeight-96),
                Color.FIREBRICK
        );
        c3.setLevelManager(stage1Level);
        avaibleCaptains.add(c3);
        Captain c4 = new Captain("Ciro, O Cangaceiro",weapons.get(4) ,new Vector2(
                (stage1Level.tileWidth / 2)+16,
                stage1Level.totalPixelHeight-96),
                Color.FIREBRICK
        );
        c4.setLevelManager(stage1Level);
        avaibleCaptains.add(c4);
    }

    public Group geradorEnemy(){
        Group group = new Group();
        for (int i=0;i<9;i++){
            Enemy soldier = new Enemy(weapons.get(random(0,4)) ,new Vector2(
                    (LevelManager.totalPixelWidth)-3*(LevelManager.tileWidth/2),
                    (float) counterAgents * 0.01f * LevelManager.totalPixelHeight),
                    Color.FIREBRICK
            );
            soldier.setLevelManager(stage2Level);
            group.soldiers.add(soldier);
            group.renders.add(group.soldiers.get(i).soldierRenderer);
        }
        return group;
    }
}

