package scripts.TTrekker.combat;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.types.RSGroundItem;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.GroundItemEntity;
import scripts.dax_api.walker.utils.AccurateMouse;

public class NailBeast extends CombatStrategy {

    @Override
    public String[] npcNames() {
        return new String[] {"nail beast"};
    }

    @Override
    public Prayer.PRAYERS useProtectionPrayer() {
        return Prayer.PRAYERS.PROTECT_FROM_MELEE;
    }

    @Override
    public boolean handle() {
        lootNails();
        return super.handle();
    }

    public void lootNails() {
        if (!Vars.get().lootNails) {
            return;
        }
        if (!Inventory.isFull()) {
            RSGroundItem plank = Entities.find(GroundItemEntity::new)
                    .idEquals(Constants.NAIL_BEAST_NAILS)
                    .getFirstResult();
            int count = Inventory.getAll().length;
            if (AccurateMouse.click(plank, "Take")) {
                Timing.waitCondition(() -> {
                    General.sleep(100,300);
                    return Inventory.getAll().length > count;
                }, General.random(1750,2500));
            }
        }
    }
}
