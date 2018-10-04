package oldcaptain.itens;

import oldcaptain.characters.Soldier;

public class Weapon extends Item {
    public int minDamage, maxDamage, ammunition;
    public float abilityModifier, range;
    public boolean meelee, isLoaded;
    public Weapon(int id, String name, int hp, int level, int ca,
                  float range, float abilityModifier, int minDamage, int maxDamage, boolean meelee) {
        super(id, name, hp, level, ca);
        this.range = range;
        this.abilityModifier = abilityModifier;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.meelee = meelee;
        if(!this.meelee){
            this.ammunition = 20;
            this.isLoaded=false;
        }
    }

    public void updateAmmunition(int number){
        this.ammunition += number;
    }
}
