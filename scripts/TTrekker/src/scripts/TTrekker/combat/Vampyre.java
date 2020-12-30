package scripts.TTrekker.combat;

import org.tribot.api2007.Prayer;
import scripts.boe_api.camera.ACamera;

public class Vampyre extends CombatStrategy {

    public Vampyre(ACamera aCamera) {
        super(aCamera);
    }

    @Override
    public String[] npcNames() {
        return new String[] {"vampyre"};
    }

    @Override
    public Prayer.PRAYERS useProtectionPrayer() {
        return Prayer.PRAYERS.PROTECT_FROM_MELEE;
    }
}
