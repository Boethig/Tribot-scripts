package scripts.TTrekker.combat;

import org.tribot.api2007.Prayer;

public class Shade extends CombatStrategy {

    @Override
    public String[] npcNames() {
        return new String[] {"shade", "shadow"};
    }

    @Override
    public Prayer.PRAYERS useProtectionPrayer() {
        return Prayer.PRAYERS.PROTECT_FROM_MELEE;
    }
}
