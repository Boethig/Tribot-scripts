package scripts.TTrekker.combat;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.types.RSGroundItem;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.GroundItemEntity;
import scripts.boe_api.utilities.Logger;
import scripts.dax_api.walker.utils.AccurateMouse;

public class NailBeast extends CombatStrategy {

    public NailBeast(ACamera aCamera) {
        super(aCamera);
    }

    @Override
    public String[] npcNames() {
        return new String[] {"nail beast"};
    }

    @Override
    public Prayer.PRAYERS useProtectionPrayer() {
        return Prayer.PRAYERS.PROTECT_FROM_MELEE;
    }

    @Override
    public boolean isMultiCombat() {
        return true;
    }

    @Override
    public boolean handle() {
        return lootNails() && super.handle();
    }

    public boolean lootNails() {
        if (!Vars.get().getSettings().shouldLootBeastNails) {
            return true;
        }
        if (Inventory.isFull()) {
            return false;
        }
        RSGroundItem nailBeastNails = Entities.find(GroundItemEntity::new)
                .idEquals(Constants.NAIL_BEAST_NAILS)
                .getFirstResult();
        if (nailBeastNails != null) {
            Logger.log("[Looting] Picking up %s", nailBeastNails.getDefinition().getName());
            int count = Inventory.getAll().length;
            if (AccurateMouse.click(nailBeastNails, "Take")) {
                Timing.waitCondition(() -> {
                    General.sleep(100,300);
                    return Inventory.getAll().length > count;
                }, General.random(1750,2500));
            }
        }
        return true;
    }
}
