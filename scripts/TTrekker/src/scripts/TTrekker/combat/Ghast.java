package scripts.TTrekker.combat;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.types.*;
import scripts.TTrekker.data.Constants;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.NpcEntity;
import scripts.boe_api.entities.finders.prefabs.ObjectEntity;
import scripts.boe_api.inventory.OSInventory;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.walker.utils.AccurateMouse;

public class Ghast implements CombatStrategy {
    @Override
    public boolean handle(RSNPC rsnpc) {
//        RSNPC ghast = Antiban.get().selectNextTarget(Entities.find(NpcEntity::new)
//                .nameEquals("Ghast")
//                .actionsContains("Attack")
//                .getResults());
//        if (ghast != null) {
//            AccurateMouse.click(ghast, "Attack");
//        }
        return false;
    }

    public boolean chargeDruidPouch() {
        if (hasEnoughDruidPouchCharges()) {
            return true;
        }
        return walkToRottingLog() && canCastBloom() && castBloom() && pickFungi() && fillDruidPouch();
    }

    public boolean walkToRottingLog() {
        RSObject rottingLog = Entities.find(ObjectEntity::new)
                .nameEquals("Rotting log").
                        sortByDistance()
                .getFirstResult();
        if (rottingLog != null) {
            RSTile bloomPosition = new RSArea(rottingLog.getPosition(), 1).getRandomTile();
             return DaxWalker.walkTo(bloomPosition);
        }
        return false;
    }

    public boolean castBloom() {
        Inventory.open();
        RSItem blessedSickle = OSInventory.findFirstNearestToMouse(Constants.BLESSED_SILVER_SICKLE);
        if (blessedSickle != null) {
            return AccurateMouse.click(blessedSickle, "Cast Bloom")
                    && Timing.waitCondition(() -> Entities.find(ObjectEntity::new)
                    .nameEquals("Fungi on log")
                    .actionsContains("Pick")
                    .getFirstResult() != null,
                    General.random(3000, 5000));
        }
        return false;
    }

    public boolean pickFungi() {
        if (Inventory.isFull()) {
            return false;
        }
        RSObject fungi = Entities.find(ObjectEntity::new)
                .nameEquals("Fungi on log")
                .actionsContains("Pick")
                .getFirstResult();
        if (fungi != null) {
            int count = Inventory.getCount(Constants.MORT_MYRE_FUNGI);
            return AccurateMouse.click(fungi, "Pick")
                    && Timing.waitCondition(() -> Inventory.getCount(Constants.MORT_MYRE_FUNGI) > count,
                    General.random(3000, 5000));
        }
        return false;
    }

    public boolean canCastBloom() {
        return Prayer.getPrayerPoints() > 0;
    }

    public boolean hasEnoughDruidPouchCharges() {
        if (OSInventory.findFirstNearestToMouse(Constants.EMPTY_DRUID_POUCH) != null) {
            return false;
        }
        RSItem chargedDruidPouch = OSInventory.findFirstNearestToMouse(Constants.FILLED_DRUID_POUCH);
        if (chargedDruidPouch != null) {
            RSNPC[] hiddenGhasts = Entities.find(NpcEntity::new)
                    .nameEquals("Ghast")
                    .actionsNotContains("Attack")
                    .getResults();
            return chargedDruidPouch.getStack() >= hiddenGhasts.length;
        }
        return false;
    }

    public boolean fillDruidPouch() {
        if (Inventory.getCount(Constants.MORT_MYRE_FUNGI) < 3) {
            return false;
        }
        RSItem chargedDruidPouch = OSInventory.findFirstNearestToMouse(Constants.EMPTY_DRUID_POUCH, Constants.FILLED_DRUID_POUCH);
        if (chargedDruidPouch != null) {
            int charges = chargedDruidPouch.getStack();
            return AccurateMouse.click(chargedDruidPouch, "Fill")
                    && Timing.waitCondition(() -> {
                RSItem updatedDruidPouch = OSInventory.findFirstNearestToMouse(Constants.FILLED_DRUID_POUCH);
                return updatedDruidPouch != null && updatedDruidPouch.getStack() == charges + 3;
            }, General.random(3000, 5000));
        }
        return false;
    }

    public boolean useDruidPouch() {
        return false;
    }
}
