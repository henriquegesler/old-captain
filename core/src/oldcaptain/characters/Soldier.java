package oldcaptain.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import oldcaptain.Agent;
import oldcaptain.graphics.AgentRenderer;
import oldcaptain.itens.Item;
import oldcaptain.itens.Weapon;
import oldcaptain.movement.Position;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;
import static oldcaptain.OldCaptain.*;

public class Soldier extends Agent {
    public int id, level, armorClass;
    public float totalHP, partialHP, experience, xpRadius, proficiency, salary, chanceToDie;
    public Weapon activeW, meelee, ranged;
    public ArrayList<Item> itens;
    public AgentRenderer soldierRenderer;

    public Soldier(Weapon weapon, Vector2 position, Color color) {
        super(position, color);
        soldierRenderer = new AgentRenderer(batch, camera, new Texture("warriors.png"));
        this.id = counterAgents;
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
        if(weapon.meelee){
            meelee = weapon;
            ranged = null;
        }
        else{
            meelee = null;
            ranged = weapon;
        }
        activeW = weapon;
    }

    public void atualizaAC(int ca){
        this.armorClass += ca;
    }

    public void setInitialPosition(){
        seek.target.coords.set((this.levelManager.tileWidth / 2),
                ((this.levelManager.totalPixelHeight/2)+64));
        position.coords.set(
                (this.levelManager.tileWidth / 2)+32,
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

    public boolean isDead(){
        if(partialHP <= 0){
            float chance=random(10000);
            if(chance<=7000){
                chance*=this.chanceToDie;
                if(chance <= 8500){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                this.chanceToDie += random(0.05f);
                return false;
            }
        }
        else{
            return false;
        }
    }

    public float contractCost(){
        float cost = 2;
        return (cost+(proficiency/2.00f)+(totalHP/5.00f)+(salary/2));
    }

    public void reload(){
        if(this.ranged.ammunition > 0){
            this.ranged.ammunition--;
            this.ranged.isLoaded=true;
        }
        else{
            changeWeapon();
        }
    }

    public boolean readyToAttack(){
        if(!activeW.meelee){
            if (activeW.isLoaded){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return true;
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


    public void escolheAlvo(ArrayList<Group> enemies){
        Soldier enemy;
        float dist = position.coords.dst(enemies.get(0).soldiers.get(0).position.coords);
        enemy = enemies.get(0).soldiers.get(0);
        for(int i=0;i<enemies.size();i++){
            for(int j=0;j<enemies.get(i).soldiers.size();j++){
                if (position.coords.dst(enemies.get(i).soldiers.get(j).position.coords) <= dist){
                    enemy = enemies.get(i).soldiers.get(j);
                }
            }
        }
        setGoal(enemy.position.coords.x,enemy.position.coords.y,2);
    }

    public Soldier enemyInRange(ArrayList<Group> enemies){
        Position localization;
        for(int i=0;i<enemies.size();i++){
            for(int j=0;j<enemies.get(i).soldiers.size();j++){
                localization = enemies.get(i).soldiers.get(j).getPosition();
                if(localization.coords.dst(position.coords) <= activeW.range){
                    return enemies.get(i).soldiers.get(j);
                }
            }
        }
        return null;
    }

    public Soldier attack(Soldier target){
        float attack = random(100.00f);
        attack += activeW.abilityModifier * proficiency;
        attack /= 5;
        if(attack>target.armorClass){
            float dmg = random(activeW.minDamage,activeW.maxDamage);
            target.partialHP -= dmg+((activeW.abilityModifier * proficiency)/2);
        }
        this.activeW.isLoaded=false;
        return target;
    }
}
