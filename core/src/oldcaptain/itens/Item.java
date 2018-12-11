package oldcaptain.itens;

import com.badlogic.gdx.graphics.Texture;

public class Item {
    public int id, hp, level, armorClass;
    public float price, timeToUse;
    public String name;
    public Texture sprite;
    public Item (int id, String name, int hp, int level, int ca, float t){
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.level = level;
        this.armorClass = ca;
        this.timeToUse = t;
    }

    public float actualTime(float prof){
        float time;
        time = timeToUse;
        return (time);
    }
}
