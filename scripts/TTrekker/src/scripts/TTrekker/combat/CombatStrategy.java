package scripts.TTrekker.combat;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSItemDefinition;
import org.tribot.api2007.types.RSNPC;
import scripts.TTrekker.data.Vars;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.ItemEntity;
import scripts.boe_api.entities.finders.prefabs.NpcEntity;
import scripts.boe_api.utilities.Antiban;
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
            if (Prayer.getPrayerPoints() > 0) {
                Prayer.enable(useProtectionPrayer());
            }
            RSNPC attackingEscortNPC = getEscortAttacker();
            if (attackingEscortNPC != null || Combat.getTargetEntity() == null || !Combat.isUnderAttack()) {
                RSNPC npc = attackingEscortNPC != null && !attackingEscortNPC.isInteractingWithMe() ? attackingEscortNPC : getNextNPC(npcs);
                if (npc != null) {
                    if (!npc.isClickable() || !npc.isOnScreen()) {
                        aCamera.turnToTile(npc);
                    }
                    if (AccurateMouse.click(npc, "Attack") &&
                            Timing.waitCondition(() -> {
                                General.sleep(100,300);
                                return Combat.getTargetEntity() != null || (npc.isInteractingWithMe() && npc.isInCombat());
                            },General.random(3000,5000))) {
                        waitForKill();
                        Antiban.get().generateTrackers(10);
                        Antiban.get().sleepReactionTime();
                    } else {
                        aCamera.turnToTile(npc);
                    }
                }
            } else {
                waitForKill();
            }
        }
        return false;
    }

    public void waitForKill() {
        Vars.get().subStatus = "AFKing";
        while (Combat.getTargetEntity() != null) {
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
            if (Prayer.getPrayerPoints() < Antiban.get().getEatAt()) {
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
        return escort != null && escort.isInCombat() ? (RSNPC) (escort.getInteractingCharacter()) : null;
    }
}
