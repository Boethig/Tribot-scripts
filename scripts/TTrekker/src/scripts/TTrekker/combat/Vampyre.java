package scripts.TTrekker.combat;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.types.RSItem;
import scripts.TTrekker.data.Constants;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.ItemEntity;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.walker.utils.AccurateMouse;

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

    @Override
    public boolean isMultiCombat() {
        return true;
    }

    @Override
    public boolean handle() {
        return equipSilverWeapon() && super.handle();
    }

    public boolean equipSilverWeapon() {
        if (Equipment.isEquipped(Constants.SILVER_WEAPONS)) {
            return true;
        }
        RSItem silverWeapon = Entities.find(ItemEntity::new)
                .nameContains(Constants.SILVER_WEAPONS)
                .actionsContains("Wear", "Equip", "Wield")
                .getFirstResult();
        if (silverWeapon != null) {
            if (AccurateMouse.click(silverWeapon)) {
                Antiban.get().waitItemInteractionDelay();
                return Timing.waitCondition(() -> Equipment.isEquipped(silverWeapon.getID()), General.random(1500,2500));
            }
        }
        return false;
    }
}
