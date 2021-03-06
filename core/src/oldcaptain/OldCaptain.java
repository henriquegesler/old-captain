package oldcaptain;

/*
  @author fegemo <coutinho@decom.cefetmg.br>
 *     @edited by henriquegesler <henriquegesler@gmail.com> on 29/09/2018
 */

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import oldcaptain.characters.Captain;
import oldcaptain.characters.Group;
import oldcaptain.characters.Soldier;
import oldcaptain.graphics.GraphRenderer;
import oldcaptain.graphics.AgentRenderer;
import oldcaptain.graphics.MetricsRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import oldcaptain.itens.Weapon;
import oldcaptain.movement.behavior.BattalionTatic;

import java.io.IOException;
import java.sql.Time;
import java.util.Iterator;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.graphics.Color.WHITE;

import com.badlogic.gdx.utils.Array;


public class OldCaptain extends Game {
    public static Sound mouseclick, slash, hit, block, march, bow;
    static Music mouseover;

    public static SpriteBatch batch;
    static ShapeRenderer shapeRenderer;
    static LevelManager stage1Level, stage2Level;
    static TiledMap selecMap, battleMap;

    static Viewport viewport;
    public static OrthographicCamera camera;

    static TiledMapRenderer tiledMapRenderer;
    static GraphRenderer graphRenderer;

    static Array<Agent> aliados = new Array<>();
    static Array<Agent> inimigos = new Array<>();
    private Agent personagem, inimigo;
    public static AgentRenderer allyRenderer, enemyRenderer;

    private final String windowTitle;
    static boolean debugMode = false;
    private MetricsRenderer metricsRenderer;
    static boolean showingMetrics;
    
    static int heuristic=2;
    public static int stage=0;
    public static int gold=50, q4Sel=8;
    static Array<Integer> parametro = new Array<>();
    public static int tx, ty, xx, yy;

    static Texture fundo, whiteS, greenS, redS, squareW, squareG;
    static BitmapFont messagesFont;
    static AssetManager assets;
    public static Group group1, group2, activeGroup, enemy1, enemy2, enemy3;
    static Array<Captain> avaibleCaptains = new Array<>();
    static Array<Weapon> weapons = new Array<>();
    static Array<BattalionTatic> tatics = new Array<>();
    static Array<Group> team1, team2, all;
    static boolean p1 = false, p2 =false, p3 =false, p4=false;
    public static int counterAgents=1, fallenE=0, fallenA=0;
    public static int allied=0, enemies=0;

    public OldCaptain() {
        this.windowTitle = "Old Captain(%d)";
        tx=0;
        ty=0;
        showingMetrics = true;
    }

    @Override
    public void create() {
        creatorOfWeapons();
        creatorOfTatics();
        activeGroup = new Group();
        enemy1 = new Group();
        enemy2 = new Group();
        enemy3 = new Group();
        team1 = new Array<>();
        team2 = new Array<>();

        mouseover = Gdx.audio.newMusic(Gdx.files.internal("sounds/mouseover.mp3"));
        mouseclick = Gdx.audio.newSound(Gdx.files.internal("sounds/mouseclick.mp3"));
        slash = Gdx.audio.newSound(Gdx.files.internal("sounds/soldier-sword.mp3"));
        block = Gdx.audio.newSound(Gdx.files.internal("sounds/soldier-nothit.mp3"));
        hit = Gdx.audio.newSound(Gdx.files.internal("sounds/soldier-hit.mp3"));
        bow = Gdx.audio.newSound(Gdx.files.internal("sounds/soldier-bow.mp3"));
        march = Gdx.audio.newSound(Gdx.files.internal("sounds/soldier-march.mp3"));

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.translate(w / 2, h / 2);
        camera.update();
        viewport = new ScreenViewport(camera);

        messagesFont = new BitmapFont();
        assets = new AssetManager();
        assets.load("fonts/sawasdee-24.fnt", BitmapFont.class);
        assets.load("fonts/sawasdee-50.fnt", BitmapFont.class);
        assets.load("fonts/sawasdee-100.fnt", BitmapFont.class);
        assets.load("fonts/sawasdee-150.fnt", BitmapFont.class);
        fundo = new Texture("black.png");
        whiteS = new Texture("white.png");
        greenS = new Texture("green.png");
        redS = new Texture("red.png");
        squareW = new Texture("images/square.png");
        squareG = new Texture("images/squareG.png");
        for(int i =0; i<100;i++){
            parametro.add(0);
        }

        Gdx.input.setInputProcessor(new MyInputAdapter());
    }

    /**
     * Atualiza o mundo virtual para ter as mesmas proporções que a janela.
     *
     * @param w Largura da janela.
     * @param h Altura da janela.
     */
    @Override
    public void resize(int w, int h) {
        viewport.update(w, h);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        if(stage == 0){
            batch.begin();
            telaMenu();
            batch.end();
        }
        else if(stage == 1){
            telaSelecao();
        }
        else if(stage == 2){
            telaBatalha();
        }
        else if(stage==3){
            telaResultado();
        }
        else if(stage==4){
            //telaMissoes();
        }
        else if(stage == 5){
            //telaMundo();
        }
        Gdx.graphics.setTitle(
                String.format(windowTitle+" X:"+Gdx.input.getX()+" Y:"+Gdx.input.getY(),
                        Gdx.graphics.getFramesPerSecond()));
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        messagesFont.dispose();
    }

    public static void tamanho(int w, int h) {
        viewport.update(w, h);
    }

    private void telaMenu(){
        if (assets.update()) {
            messagesFont = assets.get("fonts/sawasdee-150.fnt");
        }
        drawText("New Game",1.0f,
                50, 400, 0);
        //desenha fundo preto
        batch.draw(fundo, 0, 0, 1856, 896);
        //Define tamanho da letra
        if (assets.update()) {
            messagesFont = assets.get("fonts/sawasdee-150.fnt");
        }
        drawText("Options",.65f,
                50, 200, 0);
        //Define tamanho da letra
        if (assets.update()) {
            messagesFont = assets.get("fonts/sawasdee-100.fnt");
        }
        drawText("New Game",1.0f,
                50, 400, 0);
        drawText("Load Game",1.0f,
                50, 300, 0);

        drawText("Exit",1.0f,
                50, 100, 0);
        //Define tamanho da letra
        if (assets.update()) {
            messagesFont = assets.get("fonts/sawasdee-50.fnt");
        }
        //Define tamanho da letra
        if (assets.update()) {
            messagesFont = assets.get("fonts/sawasdee-24.fnt");
        }
    }

    private void telaSelecao(){
        camera.zoom = 0.75f;

        //Desenha o fundo preto
        batch.begin();
        batch.draw(fundo, -280,-416, 1856, 896);
        batch.end();

        //Desenha o mapa
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        //Cria o render de cada personagem e adiciona na lista
        if(activeGroup != null){
            for (int i = 0; i < activeGroup.soldiers.size; i++) {
                activeGroup.soldiers.get(i).update(null, Gdx.graphics.getDeltaTime(),null);
            }
            for(int i = 0; i< activeGroup.renders.size; i++){
                activeGroup.renders.get(i).render(activeGroup.soldiers.get(i));
            }
            activeGroup.activeTatic.takeAction(activeGroup);
        }
        batch.begin();
        //Define tamanho da letra
        if (assets.update()) {
            messagesFont = assets.get("fonts/sawasdee-150.fnt");
        }
        //Define tamanho da letra
        if (assets.update()) {
            messagesFont = assets.get("fonts/sawasdee-100.fnt");
        }
        //Define tamanho da letra
        if (assets.update()) {
            messagesFont = assets.get("fonts/sawasdee-50.fnt");
        }
        drawButton("Search Enemy",
                950,-240,50,370,parametro.get(12));
        drawButton("Group 1",
                360,320,50,195, parametro.get(0));
        drawText("Known Formations:",1.0f,
                350, 260, 0);
        drawButton("Group 2",
                800,320,50,205, parametro.get(1));
        drawText("Known Formations:",1.0f,
                800, 260, 0);
        drawText("Members",1.0f,
                0, -20, 0);
        for(int j=0;j<2;j++){
            for (int i=0; i<4; i++){
                batch.draw(squareW,i*70,-135-(j*70), 60,60);
            }
        }
        batch.draw(squareW,100,-285, 75,75);
        if(activeGroup != null){
            if(q4Sel == 8) {
                batch.draw(squareG, 100, -285, 75, 75);
            }
            else{
                int y = q4Sel/4;
                batch.draw(squareG, (q4Sel%4)*70,-135-(y*70), 60,60);
            }
            if(q4Sel==8){
                drawText("Name: " + activeGroup.captain.name,.75f,
                        340, -20, 0);
            }
            else {
                drawText("Cap. Name: " + activeGroup.captain.name,.75f,
                        340, -20, 0);
            }
            drawText("Level: "+activeGroup.soldiers.get(q4Sel).level,.75f,
                    340, -65, 0);
            drawText("Hit Points: "+activeGroup.soldiers.get(q4Sel).totalHP,.75f,
                    340, -110, 0);
            drawText("Armour Class: "+activeGroup.soldiers.get(q4Sel).armorClass,.75f,
                    340, -155, 0);
            drawText("Weapon: "+activeGroup.soldiers.get(q4Sel).activeW.name,.75f,
                    340, -200, 0);
            drawText("Salary: "+activeGroup.soldiers.get(q4Sel).salary,.75f,
                    340, -245, 0);
        }
        //Define tamanho da letra
        if (assets.update()) {
            messagesFont = assets.get("fonts/sawasdee-24.fnt");
        }
        drawText("Your camp will show the selected formation\n"+xx +" "+ yy,1.0f,
                -40, 350, 0);
        drawButton("Block Formation",
                350,210,24,250, parametro.get(2));
        drawButton("Shock Lines",
                350,175,24,250, parametro.get(3));
        drawButton(/*"(Pyramid Head)"*/"Not yet",
                350,140,24,250, parametro.get(4));
        drawButton("Archer's Line",
                350,105,24,250, parametro.get(5));
        drawButton(/*"Inverted Pyramid Head"*/"Not yet",
                350,70,24,250, parametro.get(6));
        drawButton("Block Formation",
                800,210,24,250, parametro.get(7));
        drawButton("Shock Lines",
                800,175,24,250, parametro.get(8));
        drawButton(/*"Pyramid Head"*/"Not yet",
                800,140,24,250, parametro.get(9));
        drawButton("Archer's Line",
                800,105,24,250, parametro.get(10));
        drawButton(/*"Inverted Pyramid Head"*/"Not yet",
                800,70,24,250, parametro.get(11));
        if (debugMode) {
            graphRenderer.renderOffScreenedGraph();
        }
        batch.end();
    }

    private void telaBatalha() {
        camera.zoom = 1f;
        //Desenha o mapa
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        batch.begin();
        drawText("Allied troops still alive: "+allied,1.0f,
                0, 50, 0);
        drawText("Enemy troops still alive: "+enemies,1.0f,
                0, 100, 0);
        batch.end();
        //Ajustando os grupos do player
        //Dá update e renderiza
        //Ativa a tática do grupo, capitão encontra seu alvo
        //Verifica se há inimigos no alcance da batalha
        for (int i=0;i<team1.size;i++){
            //A descrição será: Descrição e embaixo o código que faz a descrição
            for(int j=0;j<team1.get(i).soldiers.size;j++){
                //Update em soldado do grupo
                team1.get(i).soldiers.get(j).update(all, Gdx.graphics.getDeltaTime(),team2);
                //Render em soldado do grupo
                team1.get(i).renders.get(j).render(team1.get(i).soldiers.get(j));
                //Cada soldado escolha um alvo e vai em direção a ele
                if(team2.size>0){
                    team1.get(i).soldiers.get(j).enemyDead(team2);
                    //team1.get(i).soldiers.get(j).escolheAlvo(team2);
                    team1.get(i).soldiers.get(j).enemyInRange();
                }
                if(team1.get(i).soldiers.size>0){
                    team1.get(i).activeTatic.takeAction(team1.get(i));
                }
                /**Realiza a seguinte verificação:
                 * 1- O soldado inimigo alvo está ao alcance? (range de sua arma equipada, mudança de arma
                 * ainda a ser vista e revista)
                 * Se a resposta para for sim, criamos um objeto inimigo que recebe o inimigo no alcance
                 * Explicações continuam na mesma formula de antes.
                 */
                if(team1.get(i).soldiers.get(j).inRange) {
                    /**
                     * Realiza uma busca pelo array de grupos inimigos.
                     * O objetivo da busca é achar um inimigo que possua o id igual o id do inimigo no alcance
                     */
                    //try {
                        for (int a = 0; a < team2.size; a++) {
                            for (int b = 0; b < team2.get(a).soldiers.size; b++) {
                                if(team1.get(i).soldiers.get(j).engagedEnemy != null) {
                                    if (team2.get(a).soldiers.get(b).id == team1.get(i).soldiers.get(j).engagedEnemy.id) {
                                        /** Realizamos o ataque, passando o inimigo como parametro. Do objeto retornado pelo ataque,
                                         * pegamos seu HP atual, pois o objeto retornado teve seu HP atual modificado pelo ataque
                                         * (no caso de ter acertado o ataque). Pegamos o inimigo e igualamos seu HP atual ao HP
                                         * atual do objeto retornado
                                         */
                                        team1.get(i).soldiers.get(j).readyToAttack();
                                        if(team1.get(i).soldiers.get(j).ready) {
                                            team2.get(a).soldiers.get(b).partialHP = team1.get(i).soldiers.get(j).attack();
                                            team2.get(a).soldiers.get(b).isDead();
                                            // Verifica se o inimigo morreu
                                            if (!team2.get(a).soldiers.get(b).active) {
                                                //Caso tenha morrido, concedemos experiencia ao soldado que o matou
                                                team1.get(i).soldiers.get(j).gainExperience(team2.get(a).soldiers.get(b).directExperience());
                                                System.out.println(team1.get(i).soldiers.get(j).id + " DERROTOU " + team1.get(i).soldiers.get(j).engagedEnemy.id);
                                                team1.get(i).soldiers.get(j).engagedEnemy = null;
                                                //Removemos ele da lista de inimigos ativos
                                                //team2.get(a).soldiers.removeValue(team2.get(a).soldiers.get(b), true);
                                                team2.get(a).soldiers.removeIndex(b);
                                                fallenE++;
                                                if (team2.get(a).soldiers.size == 0) {
                                                    //team2.removeValue(team2.get(a), true);
                                                    team2.removeIndex(a);
                                                    System.out.println("REMOVEMOS O GRUPO " + a + " DO TIME 2\nSobram: " + team2.size);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    /*}catch (java.lang.NullPointerException ex){
                        ex.printStackTrace();
                    }catch (java.lang.IndexOutOfBoundsException ex1){
                        ex1.printStackTrace();
                    }*/
                }
            }
        }
        enemies = 18-fallenE;
        if(team2.size == 0){
            telaResultado();
            stage = 3;
        }
        if((fallenE+fallenA)>20){
            telaResultado();
            stage = 3;
        }
        //Ajustando os grupos inimigos
        //Dá update e renderiza
        //Ativa a tática do grupo, capitão encontra seu alvo
        //Verifica se há inimigos no alcance da batalha
        for (int i=0;i<team2.size;i++){
                //A descrição será: Descrição e embaixo o código que faz a descrição
                for(int j=0;j<team2.get(i).soldiers.size;j++){
                    //Cada soldado escolha um alvo e vai em direção a ele
                    //Update em soldado do grupo
                    team2.get(i).soldiers.get(j).update(all, Gdx.graphics.getDeltaTime(),team1);
                    //Render em soldado do grupo
                    team2.get(i).renders.get(j).render(team2.get(i).soldiers.get(j));
                    if(team1.size>0){
                        team2.get(i).soldiers.get(j).enemyDead(team1);
                        //team2.get(i).soldiers.get(j).escolheAlvo(team1);
                        team2.get(i).soldiers.get(j).enemyInRange();
                    }
                    /*stage2Level.graph.getNodeAtCoordinates(
                            (int) Math.floor(team2.get(i).soldiers.get(j).getPosition().coords.x),
                            (int) Math.floor(team2.get(i).soldiers.get(j).getPosition().coords.y))
                            .setIsOcuppied();
                    */
                    /**Realiza a verificação:
                     * 1- O soldado possui algum inimigo em sua área de ameaça (range de sua arma equipada, mudança de arma
                     * ainda a ser vista e revista)
                     * Se a resposta para for sim, criamos um objeto inimigo que recebe o inimigo no alcance
                     * Explicações continuam na mesma formula de antes.
                     */
                    if(team2.get(i).soldiers.get(j).inRange) {
                        /**
                         * Realiza uma busca pelo array de grupos inimigos.
                         * O objetivo da busca é achar um inimigo que possua o id igual o id do inimigo no alcance
                         */
                        //try {
                            for (int a = 0; a < team1.size; a++) {
                                for (int b = 0; b < team1.get(a).soldiers.size; b++) {
                                    if(team2.get(i).soldiers.get(j).engagedEnemy != null) {
                                        if (team1.get(a).soldiers.get(b).id == team2.get(i).soldiers.get(j).engagedEnemy.id) {
                                            /** Realizamos o ataque, passando o inimigo como parametro. Do objeto retornado pelo ataque,
                                             * pegamos seu HP atual, pois o objeto retornado teve seu HP atual modificado pelo ataque
                                             * (no caso de ter acertado o ataque). Pegamos o inimigo e igualamos seu HP atual ao HP
                                             * atual do objeto retornado
                                             */
                                            team2.get(i).soldiers.get(j).readyToAttack();
                                            if(team2.get(i).soldiers.get(j).ready) {
                                                //Como um ataque foi realizado, retiramos 1 ponto de ação do personagem atual
                                                team1.get(a).soldiers.get(b).partialHP = team2.get(i).soldiers.get(j).attack();
                                                team1.get(a).soldiers.get(b).isDead();
                                                // Verifica se o inimigo morreu
                                                if (!team1.get(a).soldiers.get(b).active) {
                                                    //Caso tenha morrido, concedemos experiencia ao soldado que o matou
                                                    team2.get(i).soldiers.get(j).gainExperience(team1.get(a).soldiers.get(b).directExperience());
                                                    System.out.println(team2.get(i).soldiers.get(j).id + " DERROTOU " + team2.get(i).soldiers.get(j).engagedEnemy.id);
                                                    //OPERAÇÃO ABAIXO É REDUNDANTE
                                                    team2.get(i).soldiers.get(j).engagedEnemy = null;
                                                    //Removemos ele da lista de inimigos ativos
                                                    team1.get(a).soldiers.removeIndex(b);
                                                    fallenA++;
                                                    if (team1.get(a).soldiers.size == 0) {
                                                        team1.removeIndex(a);
                                                        System.out.println("REMOVEMOS O GRUPO " + a + " DO TIME 1\nSobram: " + team1.size);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        /*}catch (java.lang.NullPointerException ex){
                            ex.printStackTrace();
                        }catch (java.lang.IndexOutOfBoundsException ex1){
                            ex1.printStackTrace();
                        }*/
                    }
                }
        }
        if((fallenE+fallenA)>20){
            telaResultado();
            stage = 3;
        }
        if(team1.size == 0){
            telaResultado();
            stage = 3;
        }
        batch.begin();
        if (debugMode) {
            graphRenderer.renderOffScreenedGraph();
        }
        batch.end();
        allied = 18-fallenA;
    }

    public void telaResultado(){
        batch.begin();
        batch.draw(fundo, 0,0, 1856, 896);
        if (assets.update()) {
            messagesFont = assets.get("fonts/sawasdee-150.fnt");
        }
        if(fallenE>10){
            drawCenterAlignedText("Vitória!",1.0f,600);
        }
        else if(fallenA>10){
            drawCenterAlignedText("derrota!!",1.0f,600);
        }
        if (assets.update()) {
            messagesFont = assets.get("fonts/sawasdee-100.fnt");
        }
        drawCenterAlignedText("derrotamos: "+fallenE,1.0f,440);
        drawCenterAlignedText("perdemos: "+fallenA,1.0f,320);
        drawCenterAlignedText("Aperte ENTER para terminar.",1.0f,200);
        batch.end();
    }

    public void drawCenterAlignedText(String text, float scale, float y) {
        if (scale > 1) {
            throw new IllegalArgumentException("Pediu-se para escrever texto "
                    + "com tamanho maior que 100% da fonte, mas isso acarreta "
                    + "em perda de qualidade do texto. Em vez disso, use uma "
                    + "fonte maior. O valor de 'scale' deve ser sempre menor "
                    + "que 1.");
        }
        final float horizontalPadding = 0.05f;
        messagesFont.setColor(WHITE);
        messagesFont.getData().setScale(scale);

        final float worldWidth = viewport.getWorldWidth();
        messagesFont.draw(
                batch,
                text,
                0 + horizontalPadding * worldWidth,
                y,
                worldWidth * (1 - horizontalPadding * 2),
                Align.center,
                true);
    }

    public void drawText(String text, float scale, float x, float y, int cor) {
        if (scale > 1) {
            throw new IllegalArgumentException("Pediu-se para escrever texto "
                    + "com tamanho maior que 100% da fonte, mas isso acarreta "
                    + "em perda de qualidade do texto. Em vez disso, use uma "
                    + "fonte maior. O valor de 'scale' deve ser sempre menor "
                    + "que 1.");
        }
        if(cor==0){
            messagesFont.setColor(Color.WHITE);
        }
        else if(cor==1){
            messagesFont.setColor(Color.BLACK);
        }
        messagesFont.getData().setScale(scale);
        messagesFont.draw(
                batch,
                text,
                x,
                y);
    }

    public void drawButton(String str, int x, int y, int altura, int largura, int param){
        if(param==0){
            batch.draw(redS, x-10 ,y-altura-5, largura+10, altura+10);
            batch.draw(fundo, x-5 ,y-altura, largura, altura);
        }
        else if(param==1){
            batch.draw(greenS, x-10 ,y-altura-5, largura+10, altura+10);
            batch.draw(fundo, x-7 ,y-altura-2, largura+4, altura+4);
        }
        else if(param==2){
            batch.draw(greenS, x-10 ,y-altura-5, largura+10, altura+10);
            batch.draw(fundo, x-5 ,y-altura, largura, altura);
        }
        drawText(str,1, x, y, 0);
    }

    public void creatorOfWeapons(){
        Weapon w0 = new Weapon(0,"Sword", 6, 1, 5, 1.0f,
                32.0f,.8f,1,5,true
        );
        Weapon w1 = new Weapon(1,"Snake's Staff", 10, 2, 8, 1.5f,
                32.0f,1.0f,3,8,true
        );
        Weapon w2 = new Weapon(2,"Blood Hoof-Pick", 10, 2, 8, 0.8f,
                32.0f,1.1f,2,6,true
        );
        Weapon w3 = new Weapon(3,"Holy Word", 10, 2, 8, 1.0f,
                96.0f,1.15f,4,6,false
        );
        Weapon w4 = new Weapon(4,"Cangaço's Crossbow", 10, 2, 8, 2.5f,
                320.0f,0.9f,1,6,false
        );
        weapons.add(w0);
        weapons.add(w1);
        weapons.add(w2);
        weapons.add(w3);
        weapons.add(w4);
    }

    public void creatorOfTatics(){
        BattalionTatic t0 = new BattalionTatic(0,"Block Formation",
                "Square around Captain, he leads into the battle. Warriors with most defense in the front." +
                        "They try to enter in the middle of the enemy army.");
        BattalionTatic t1 = new BattalionTatic(1,"Shock Lines",
                "Two lines. First one impact and spread, then the second comes after trying to clean the field." +
                        "Tend to reunite in lines if no enemy in attack radius. Highly aggressive, chase around new enemies.");
        BattalionTatic t2 = new BattalionTatic(2,"Pyramid Head",
                "Triangle formation with Captain leading in front. Could use archers in the last line as support troops.");
        BattalionTatic t3 = new BattalionTatic(3,"Archer's Line",
                "Two lines. Last one with only archers. Move in range to attack at safe distance, can have some rookies as" +
                        "the frontlinners. Captain with ranged weapon is desired.");
        BattalionTatic t4 = new BattalionTatic(4,"Inverted Pyramid Head",
                "Triangle with the base in front, first line charge, second line try to support the first line in meelee" +
                        "or take out archers with their own weapons.");
        tatics.add(t0);
        tatics.add(t1);
        tatics.add(t2);
        tatics.add(t3);
        tatics.add(t4);
    }
}
