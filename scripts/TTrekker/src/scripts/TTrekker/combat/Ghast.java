package scripts.TTrekker.combat;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.TTrekker.data.Constants;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.NpcEntity;
import scripts.boe_api.entities.finders.prefabs.ObjectEntity;
import scripts.boe_api.inventory.OSInventory;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.walker.utils.AccurateMouse;

public class Ghast extends CombatStrategy {

    @Override
    public String[] npcNames() {
        return new String[] {"ghast"};
    }

    @Override
    public Prayer.PRAYERS useProtectionPrayer() { return Prayer.PRAYERS.PROTECT_FROM_MELEE; }

    @Override
    public boolean handle() {
        RSNPC[] hiddenGhasts = Entities.find(NpcEntity::new)
                .nameEquals("Ghast")
                .actionsNotContains("Attack")
                .getResults();
        if (hiddenGhasts != null) {
            if (hasEnoughDruidPouchCharges(hiddenGhasts)) {
                General.println("Making ghasts visible");
                makeGhastsVisible(hiddenGhasts);
            } else {
                chargeDruidPouch();
            }
        } else {
            return super.handle();
        }
        return false;
    }

    public void makeGhastsVisible(RSNPC[] hiddenGhasts) {
        RSItem druidPouch = OSInventory.findFirstNearestToMouse(Constants.FILLED_DRUID_POUCH);
        if (druidPouch != null) {
            if (Game.getItemSelectionState() == 1) {
                RSNPC ghast = Antiban.get().selectNextTarget(hiddenGhasts);
                if (ghast != null) {
                    int charges = druidPouch.getStack();
                    if (AccurateMouse.click(ghast, "Use")) {
                        Timing.waitCondition(() -> {
                            General.sleep(100, 300);
                            RSItem updatedPouch = OSInventory.findFirstNearestToMouse(Constants.FILLED_DRUID_POUCH);
                            return charges > updatedPouch.getStack();
                        }, General.random(3000, 5000));
                    }
                }
            } else {
                if (AccurateMouse.click(druidPouch, "Use")) {
                    Timing.waitCondition(() -> {
                        Antiban.get().waitItemInteractionDelay();
                        return Game.getItemSelectionState() == 1;
                    }, General.random(3000, 5000));
                }
            }
        }
    }

    public boolean chargeDruidPouch() {
        return walkToRottingLog() && canCastBloom() && castBloom() && pickFungi() && fillDruidPouch();
    }

    public boolean walkToRottingLog() {
        RSObject rottingLog = Entities.find(ObjectEntity::new)
                .nameEquals("Rotting log").
                        sortByDistance()
                .getFirstResult();
        if (rottingLog != null) {
            if (Player.getPosition().distanceTo(rottingLog) <= 1) {
                return true;
            }
            RSTile bloomPosition = new RSArea(rottingLog.getPosition(), 1).getRandomTile();
            return Walking.blindWalkTo(bloomPosition);
        }
        return false;
    }

    public boolean castBloom() {
        Inventory.open();
        RSItem blessedSickle = OSInventory.findFirstNearestToMouse(Constants.BLESSED_SILVER_SICKLE);
        if (blessedSickle != null) {
            return AccurateMouse.click(blessedSickle, "Cast Bloom")
                    && Timing.waitCondition(() ->  {
                        General.sleep(100,300);
                        return Entities.find(ObjectEntity::new)
                                .nameEquals("Fungi on log")
                                .actionsContains("Pick")
                                .getFirstResult() != null;
                        },General.random(3000, 5000));
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
                    && Timing.waitCondition(() ->  {
                        General.sleep(100,300);
                        return Inventory.getCount(Constants.MORT_MYRE_FUNGI) > count;
                    },
                    General.random(3000, 5000));
        }
        return false;
    }

    public boolean canCastBloom() {
        return Prayer.getPrayerPoints() > 0;
    }

    public boolean hasEnoughDruidPouchCharges(RSNPC[] hiddenGhasts) {
        if (OSInventory.findFirstNearestToMouse(Constants.EMPTY_DRUID_POUCH) != null) {
            return false;
        }
        RSItem chargedDruidPouch = OSInventory.findFirstNearestToMouse(Constants.FILLED_DRUID_POUCH);
        if (chargedDruidPouch != null) {
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
                        General.sleep(100,300);
                        RSItem updatedDruidPouch = OSInventory.findFirstNearestToMouse(Constants.FILLED_DRUID_POUCH);
                        return updatedDruidPouch != null && updatedDruidPouch.getStack() == charges + 3;
            }, General.random(3000, 5000));
        }
        return false;
    }
}
