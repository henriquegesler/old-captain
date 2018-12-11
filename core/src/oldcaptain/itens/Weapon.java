package oldcaptain.itens;

import oldcaptain.characters.Soldier;

public class Weapon extends Item {
    public int minDamage, maxDamage, ammunition;
    public float abilityModifier, range;
    public boolean meelee, isLoaded;
    public Weapon(int id, String name, int hp, int level, int ca, float t,
                  float range, float abilityModifier, int minDamage, int maxDamage, boolean meelee) {
        super(id, name, hp, level, ca, t);
        this.range = range;
        this.abilityModifier = abilityModifier;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.meelee = meelee;
        if(!this.meelee){
            this.ammunition = 100;
            this.isLoaded=true;
        }
    }

    public void updateAmmunition(int number){
        this.ammunition += number;
    }
}
