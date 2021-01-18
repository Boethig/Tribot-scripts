package scripts.TTrekker.combat;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.TTrekker.data.Vars;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.ItemEntity;
import scripts.boe_api.entities.finders.prefabs.NpcEntity;
import scripts.boe_api.utilities.Antiban;
import scripts.boe_api.utilities.Logger;
import scripts.dax_api.walker.utils.AccurateMouse;

import java.util.Arrays;

public abstract class CombatStrategy {

    protected ACamera aCamera;

    public CombatStrategy(ACamera aCamera) {
        this.aCamera = aCamera;
    }

    abstract public String[] npcNames();

    abstract public Prayer.PRAYERS useProtectionPrayer();

    public boolean handle() {
        RSNPC[] npcs = Entities.find(NpcEntity::new)
                .nameContains(npcNames())
                .actionsContains("Attack")
                .sortByDistance()
                .getResults();
        if (npcs.length == 0) {
            return true;
        } else {
            if (Vars.get().getSettings().useProtectionPrayers && Prayer.getPrayerPoints() > 0 && !Prayer.isPrayerEnabled(useProtectionPrayer())) {
                Logger.log("[CombatStrategy] Activated %s prayer", useProtectionPrayer().getName());
                Prayer.enable(useProtectionPrayer());
            }
            RSNPC attackingNpc = getEscortAttacker();
            if (attackingNpc != null || Combat.getTargetEntity() == null) {
                if (attackingNpc != null) {
                    if (Player.getRSPlayer().getInteractingCharacter() != attackingNpc) {
                        attackNPC(attackingNpc);
                    }
                } else {
                    RSNPC npc = getNextNPC(npcs);
                    if (npc != null) {
                        attackNPC(npc);
                    }
                }
            } else {
                waitForKill();
            }
        }
        return false;
    }

    public void attackNPC(RSNPC npc) {
        Logger.log("[CombatStrategy] Attacking %s", npc.getName());
        if (AccurateMouse.click(npc, "Attack") &&
                Timing.waitCondition(() -> {
                    General.sleep(100,300);
                    return Combat.getTargetEntity() != null;
                },General.random(3000,5000))) {
            waitForKill();
            Antiban.get().generateTrackers(10);
            Antiban.get().sleepReactionTime();
        } else {
            aCamera.turnToTile(npc);
        }
    }

    public void waitForKill() {
        Vars.get().subStatus = "AFKing";
        Logger.log("[CombatStrategy] Waiting for npc to be killed.");
        while (Combat.getTargetEntity() != null && getEscortAttacker() == null) {
            checkPrayer();
            checkAndEat();
            Antiban.get().timedActions();
            General.sleep(50,150);
        }
    }

    public boolean checkPrayer() {
        RSItem potion = Entities.find(ItemEntity::new)
                .nameContains("Prayer potion", "Super restore")
                .actionsContains("Drink")
                .custom((rsItem -> {
                    RSItemDefinition itemDefinition = rsItem.getDefinition();
                    return itemDefinition == null || !itemDefinition.isNoted();
                })).getFirstResult();
        if (potion != null) {
            if (Prayer.getPrayerPoints() <= Vars.get().restorePrayerAt) {
                Logger.log("[CombatStrategy] Restoring prayer points.");
                Inventory.open();
                int prePrayer = Prayer.getPrayerPoints();
                if (AccurateMouse.click(potion) && Timing.waitCondition(() -> {
                    General.sleep(50,150);
                    return Prayer.getPrayerPoints() > prePrayer;
                }, General.random(700,1500))) {
                    Antiban.get().resetEatAt();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkAndEat() {
        RSItem food = Entities.find(ItemEntity::new)
                .actionsContains("Eat", "Drink")
                .custom((rsItem -> {
                    RSItemDefinition itemDefinition = rsItem.getDefinition();
                    return itemDefinition == null || !itemDefinition.isNoted();
                })).getFirstResult();

        if (food != null) {
            int currentHp = Combat.getHP();
            if (currentHp < Antiban.get().getEatAt()) {
                Logger.log("[CombatStrategy] Restoring hitpoints.");
                Inventory.open();
                if (AccurateMouse.click(food) && Timing.waitCondition(() -> {
                    General.sleep(50,150);
                    return Combat.getHP() > currentHp;
                }, General.random(700,1200))) {
                    Antiban.get().resetEatAt();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean supplyEscort() {
        if (Vars.get().foodSupply != null) {
            if (Vars.get().foodSupply.getSupplyCount() == 0) {
                RSItem foodSupply = Entities.find(ItemEntity::new)
                        .nameContains(Vars.get().foodSupply.name())
                        .actionsContains("Use")
                        .custom((rsItem -> {
                            RSItemDefinition itemDefinition = rsItem.getDefinition();
                            return itemDefinition == null || !itemDefinition.isNoted();
                        })).getFirstResult();
                if (foodSupply != null) {
                    RSNPC escort = Vars.get().getSettings().escortDifficulty.findInInstance();
                    if (escort != null) {
                        return AccurateMouse.click(foodSupply, "Use") && AccurateMouse.click(escort);
                    }
                }
            }
        }
        return false;
    }

    public RSNPC getNextNPC(RSNPC[] npcs) {
        return Arrays.stream(npcs).filter(rsnpc -> rsnpc.isInteractingWithMe() && rsnpc.isInCombat())
                .findFirst()
                .orElse(Antiban.get().selectNextTarget(npcs));
    }

    public RSNPC getEscortAttacker() {
        RSNPC escort = Vars.get().getSettings().escortDifficulty.findInInstance();
        if (escort != null && escort.isInCombat()) {
            RSCharacter character = escort.getInteractingCharacter();
            if (character != null && character.getClass() == RSNPC.class) {
                Logger.log("[CombatStrategy] Escort is under attack.");
                return (RSNPC) character;
            }
        }
        return null;
    }
}
