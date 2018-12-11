package oldcaptain.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import oldcaptain.Agent;
import oldcaptain.LevelManager;
import oldcaptain.graphics.AgentRenderer;
import oldcaptain.itens.Item;
import oldcaptain.itens.Weapon;
import oldcaptain.movement.Position;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;
import com.badlogic.gdx.utils.Array;
import static oldcaptain.OldCaptain.*;

public class Soldier extends Agent {
    public int level, armorClass;
    public float totalHP, partialHP, experience, xpRadius, proficiency, salary, chanceToDie;
    public Weapon activeW, meelee, ranged;
    public ArrayList<Item> itens;
    public AgentRenderer soldierRenderer;

    public boolean alive, active, ready;
    public long lastAttack = 0;

    public Soldier(Weapon meelee, Weapon ranged, Vector2 position, Color color) {
        super(position, color);
        soldierRenderer = new AgentRenderer(batch, camera, new Texture("warriors.png"));
        this.level = 1;
        this.experience = 0;
        this.chanceToDie = random(0.00f,0.51f);
        this.proficiency = random(0.5f,2.01f);
        this.salary = random(0.10f,1.01f);
        this.totalHP = 10;
        this.partialHP = totalHP;
        this.itens = new ArrayList<>();
        this.xpRadius = 1.5f;
        this.armorClass = 10;
        this.meelee = meelee;
        this.ranged = ranged;
        if(ranged.id==4){
            this.activeW = ranged;
        }
        else {
            this.activeW = meelee;
        }
        this.engagedEnemy = null;
        this.alive = true;
        this.active = true;
        this.inRange = false;
        this.ready = true;
    }

    public void atualizaAC(int ca){
        this.armorClass += ca;
    }

    public void setInitialPosition(){
        seek.target.coords.set((this.levelManager.tileWidth / 2),
                ((this.levelManager.totalPixelHeight/2)+64));
        position.coords.set(
                (this.levelManager.tileWidth / 2)+96,
                (this.levelManager.totalPixelHeight/2)+64);
    }
    public void setInitialPosition2(){
        seek.target.coords.set((this.levelManager.tileWidth / 2),
                ((this.levelManager.totalPixelHeight/2)+64));
        position.coords.set(
                (this.levelManager.tileWidth / 2),
                (this.levelManager.totalPixelHeight/2)+64);
    }

    public void gainExperience(float xp){
        int chance=random(0,101);
        if(chance>=25){
            float add=random(0.0f,0.5f);
            this.proficiency += add;
            this.salary += this.salary*add;
        }
        xp /= this.level;
        this.experience += xp;
        if(this.experience >= 100){
            this.experience -= 100.00f;
            levelUP();
        }

    }

    public void levelUP(){
        this.level++;
        this.proficiency += random(0.5f,1.1f);
        this.salary += .5;
        this.totalHP += random(1,6);
        this.partialHP = this.totalHP;
    }

    public float directExperience(){
        float xp = 20*this.level;
        return xp;
    }

    public float indirectExperience(){
        float xp = 5*this.level;
        return xp;
    }

    public void isDead(){
        if(partialHP <= 0){
            active=false;
            float chance=random(10000);
            if(chance<=7000){
                chance*=this.chanceToDie;
                if(chance <= 8500){
                    alive = false;
                }
                else{
                    alive = true;
                }
            }
            else{
                this.chanceToDie += random(0.05f);
                alive = true;
            }
        }
        else{
            active=true;
            alive = true;
        }
    }

    public float contractCost(){
        float cost = 2;
        return (cost+(proficiency/2.00f)+(totalHP/5.00f)+(salary/2));
    }

    public void reload(){
        if(this.activeW.ammunition > 0 && !activeW.meelee && !activeW.isLoaded){
            this.activeW.ammunition--;
            this.activeW.isLoaded=true;
        }
        else{
            //changeWeapon();
        }
    }

    public void readyToAttack(){
        if(!ready){
            long tim = System.nanoTime();
            tim = tim - lastAttack;
            tim = tim/1000000;
            tim = tim/1000;
            if(tim >= activeW.timeToUse){
                ready = true;
            }
        }
    }

    public void changeWeapon(){
        if(activeW.meelee){
            activeW = ranged;
        }
        else{
            activeW = meelee;
        }
    }

    public void enemyInRange(){
        if(engagedEnemy!=null) {
            Position localization;
            localization = engagedEnemy.getPosition();
            if (localization.coords.dst(position.coords) <= activeW.range) {
                inRange = true;
            } else {
                inRange = false;
            }
        }
    }

    @Override
    public void escolheAlvo(Array<Group> enemies){
        if(enemies!=null) {
            Soldier enemy;
            if (!inRange) {
                //if (this.engagedEnemy == null) {
                float dist = position.coords.dst(enemies.get(0).soldiers.get(0).position.coords);
                enemy = enemies.get(0).soldiers.get(0);
                for (int i = 0; i < enemies.size; i++) {
                    for (int j = 0; j < enemies.get(i).soldiers.size; j++) {
                        if (position.coords.dst(enemies.get(i).soldiers.get(j).position.coords) <= dist) {
                            enemy = enemies.get(i).soldiers.get(j);
                            dist = position.coords.dst(enemies.get(i).soldiers.get(j).position.coords);
                        }
                    }
                }
                this.engagedEnemy = enemy;
                // }
                setGoal(this.engagedEnemy.position.coords.x, this.engagedEnemy.position.coords.y, 2);
            } else {
                shouldMove = false;
                nextNode = currentNode;
            }
        }
    }

    public void enemyDead(Array<Group> enemies){
        if(engagedEnemy != null) {
            if(engagedEnemy.partialHP<0){
                inRange = false;
                engagedEnemy = null;
            }
        }
        else{
            inRange = false;
        }
    }

    public float attack(){
        float attack = random(100.00f);
        attack += (activeW.abilityModifier * proficiency);
        attack = attack / 5;
        lastAttack = System.nanoTime();
        if(activeW.meelee){
            slash.play(4.0f);
        }
        else{
            bow.play(4.0f);
        }
        if(attack>engagedEnemy.armorClass){
            float dmg = random(activeW.minDamage,activeW.maxDamage);
            dmg+=((activeW.abilityModifier * proficiency)/2);
            System.out.println(this.id + " ESTÁ ATACANDO " + engagedEnemy.id
                    +" COM DANO = "+dmg+" HP: "+engagedEnemy.partialHP+"//");
            engagedEnemy.partialHP -= dmg;
            System.out.println(engagedEnemy.partialHP);
            hit.play(0.3f);
        }
        else{
            block.play(0.3f);
        }
        this.activeW.isLoaded=false;
        this.ready = false;
        return engagedEnemy.partialHP;
    }

}
