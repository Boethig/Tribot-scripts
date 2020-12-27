package scripts.TTrekker.combat;

import org.tribot.api2007.Prayer;

public class Vampyre extends CombatStrategy {

    @Override
    public String[] npcNames() {
        return new String[] {"vampyre"};
    }

    @Override
    public Prayer.PRAYERS useProtectionPrayer() {
        return Prayer.PRAYERS.PROTECT_FROM_MELEE;
    }
}
