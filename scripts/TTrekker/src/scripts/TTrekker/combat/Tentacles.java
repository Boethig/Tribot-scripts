package scripts.TTrekker.combat;

import org.tribot.api2007.Prayer;

public class Tentacles extends CombatStrategy {

    @Override
    public String[] npcNames() {
        return new String[0];
    }

    @Override
    public Prayer.PRAYERS useProtectionPrayer() {
        return null;
    }
}
