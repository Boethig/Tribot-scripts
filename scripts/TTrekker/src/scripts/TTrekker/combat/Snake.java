package scripts.TTrekker.combat;

import org.tribot.api2007.Prayer;
import scripts.boe_api.camera.ACamera;

public class Snake extends CombatStrategy {

    public Snake(ACamera aCamera) {
        super(aCamera);
    }

    @Override
    public String[] npcNames() {
        return new String[] {"snake"};
    }

    @Override
    public Prayer.PRAYERS useProtectionPrayer() {
        return Prayer.PRAYERS.PROTECT_FROM_MELEE;
    }
}
