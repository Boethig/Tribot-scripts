package scripts.TTrekker.combat;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.ItemEntity;
import scripts.boe_api.entities.finders.prefabs.NpcEntity;
import scripts.boe_api.entities.finders.prefabs.ObjectEntity;
import scripts.boe_api.inventory.OSInventory;
import scripts.boe_api.utilities.Antiban;
import scripts.boe_api.utilities.Logger;
import scripts.dax_api.walker.utils.AccurateMouse;

import java.util.Arrays;

public class Ghast extends CombatStrategy {

    public Ghast(ACamera aCamera) {
        super(aCamera);
    }

    @Override
    public String[] npcNames() {
        return new String[] {"ghast"};
    }

    @Override
    public Prayer.PRAYERS useProtectionPrayer() { return Prayer.PRAYERS.PROTECT_FROM_MELEE; }

    @Override
    public boolean isMultiCombat() {
        return true;
    }

    @Override
    public boolean handle() {
        if (Vars.get().getSettings().useProtectionPrayers && Prayer.getPrayerPoints() > 0) {
            Prayer.enable(useProtectionPrayer());
        }
        RSNPC[] hiddenGhasts = Entities.find(NpcEntity::new)
                .nameContains("ghast")
                .actionsNotContains("Attack")
                .getResults();
        if (hiddenGhasts.length > 0) {
            if (hasEnoughDruidPouchCharges(hiddenGhasts.length)) {
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
                if (ghast != null && ghast.isValid()) {
                    int previousCharges = druidPouch.getStack();
                    if (!ghast.isOnScreen() || !ghast.isClickable()) {
                        aCamera.turnToTile(ghast);
                    }
                    if (Clicking.click("Use", ghast)) {
                        Logger.log("[Ghast] Making Ghast visible");
                        Timing.waitCondition(() -> {
                            General.sleep(100,300);
                            RSItem updatedPouch = OSInventory.findFirstNearestToMouse(Constants.FILLED_DRUID_POUCH);
                            return updatedPouch != null && previousCharges > updatedPouch.getStack() && Game.getItemSelectionState() != 1;
                        }, General.random(6000,8000));
                    }
                }
            } else {
                if (AccurateMouse.click(druidPouch, "Use")) {
                    Timing.waitCondition(() -> {
                        Antiban.get().waitItemInteractionDelay();
                        return Game.getItemSelectionState() == 1;
                    }, General.random(3000,5000));
                }
            }
        }
    }

    public boolean chargeDruidPouch() {
        if (Inventory.getCount(Constants.MORT_MYRE_FUNGI) >= 3) {
            fillDruidPouch();
        } else {
            return walkToRottingLog() && canCastBloom() && castBloom() && pickFungi();
        }

        return false;
    }

    public boolean walkToRottingLog() {
        RSObject rottingLog = Entities.find(ObjectEntity::new)
                .nameContains("log")
                .custom(log -> log.isClickable())
                .sortByDistance()
                .getFirstResult();
        if (rottingLog != null) {
            if (Timing.waitCondition(() -> {
                General.sleep(50,150);
                return Player.getPosition().distanceTo(rottingLog) <= 1;
            }, General.random(1500,2000))) {
                return true;
            }
            RSTile bloomPos = Arrays.stream(new RSArea(rottingLog.getPosition(), 1).getAllTiles())
                    .filter(rsTile -> rsTile.isClickable() && rsTile.getPosition() != rottingLog.getPosition())
                    .findFirst()
                    .orElse(null);
            if (bloomPos != null) {
                Logger.log("[Ghast] Walking to a rotting log");
                return AccurateMouse.walkScreenTile(bloomPos) && Timing.waitCondition(() -> Player.getPosition().distanceTo(bloomPos) <= 1, 5000);
            }
        }
        return false;
    }

    public boolean castBloom() {
        if (Entities.find(ObjectEntity::new)
                .nameEquals("Fungi on log")
                .actionsContains("Pick")
                .getFirstResult() != null) {
            return true;
        }
        Inventory.open();
        RSItem blessedSickle = OSInventory.findFirstNearestToMouse(Constants.BLESSED_SILVER_SICKLE);
        if (blessedSickle != null) {
            Logger.log("[Ghast] Casting bloom");
            return AccurateMouse.click(blessedSickle, "Cast Bloom")
                    && Timing.waitCondition(() ->  {
                        General.sleep(100,300);
                        return Entities.find(ObjectEntity::new)
                                .nameEquals("Fungi on log")
                                .actionsContains("Pick")
                                .getFirstResult() != null;
                        },General.random(3000,5000));
        }
        return false;
    }

    public boolean pickFungi() {
        if (Inventory.isFull()) {
            return false;
        }
        if (Inventory.getCount(Constants.MORT_MYRE_FUNGI) >= 3) {
            return true;
        }
        RSObject fungi = Entities.find(ObjectEntity::new)
                .nameEquals("Fungi on log")
                .actionsContains("Pick")
                .getFirstResult();
        if (fungi != null) {
            Logger.log("[Ghast] Picking Mort Myre Fungi");
            int prevCount = Inventory.getCount(Constants.MORT_MYRE_FUNGI);
            return AccurateMouse.click(fungi, "Pick")
                    && Timing.waitCondition(() ->  {
                        General.sleep(100,300);
                        return Entities.find(ObjectEntity::new)
                        .nameEquals("Fungi on log")
                        .actionsContains("Pick")
                        .getFirstResult() == null || Inventory.getCount(Constants.MORT_MYRE_FUNGI) > prevCount;
                    }, General.random(3000,5000));
        }
        return false;
    }

    public boolean canCastBloom() {
        return Prayer.getPrayerPoints() > 0;
    }

    public boolean hasEnoughDruidPouchCharges(int hiddenGhasts) {
        RSItem chargedPouch = Entities.find(ItemEntity::new)
                .idEquals(Constants.FILLED_DRUID_POUCH)
                .getFirstResult();
        return chargedPouch != null && chargedPouch.getStack() >= hiddenGhasts;
    }

    public boolean fillDruidPouch() {
        if (Inventory.getCount(Constants.MORT_MYRE_FUNGI) < 3) {
            return false;
        }
        RSItem chargedDruidPouch = OSInventory.findFirstNearestToMouse(Constants.EMPTY_DRUID_POUCH, Constants.FILLED_DRUID_POUCH);
        if (chargedDruidPouch != null) {
            Logger.log("[Ghast] Charging Druid Pouch");
            int charges = chargedDruidPouch.getStack();
            return AccurateMouse.click(chargedDruidPouch, "Fill")
                    && Timing.waitCondition(() -> {
                        General.sleep(100,300);
                        RSItem updatedDruidPouch = OSInventory.findFirstNearestToMouse(Constants.FILLED_DRUID_POUCH);
                        return updatedDruidPouch != null && updatedDruidPouch.getStack() == charges + 3;
            }, General.random(3000,5000));
        }
        return false;
    }
}
