package scripts.TTrekker.combat;

import org.tribot.api2007.Prayer;
import scripts.boe_api.camera.ACamera;

public class Tentacles extends CombatStrategy {

    public Tentacles(ACamera aCamera) {
        super(aCamera);
    }

    @Override
    public String[] npcNames() {
        return new String[0];
    }

    @Override
    public Prayer.PRAYERS useProtectionPrayer() {
        return null;
    }
}
