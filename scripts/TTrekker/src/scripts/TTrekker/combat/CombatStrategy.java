package scripts.TTrekker.combat;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.types.RSCharacter;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSItemDefinition;
import org.tribot.api2007.types.RSNPC;
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

    abstract public boolean isMultiCombat();

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
                Prayer.enable(useProtectionPrayer());
                Logger.log("[CombatStrategy] Activated %s prayer", useProtectionPrayer().getName());
            }
            if (!isProtectingEscort()) {
                RSNPC attacker = getEscortAttacker();
                if (isMultiCombat()) {
                    attackNPC(attacker);
                } else {
                    if (!Combat.isUnderAttack()) {
                        attackNPC(attacker);
                    }
                }
            } else if (Player.getRSPlayer().getInteractingCharacter() == null) {
                RSNPC npc = getNextNPC(npcs);
                if (npc != null) {
                    attackNPC(npc);
                }
            } else {
                waitForKill();
            }
        }
        return false;
    }

    public void attackNPC(RSNPC npc) {
        if (AccurateMouse.click(npc, "Attack")) {
            Logger.log("[CombatStrategy] Attacking %s", npc.getName());
            if (Timing.waitCondition(() -> {
                General.sleep(100,300);
                return Combat.getTargetEntity() != null || (npc.isInteractingWithMe() && npc.isInCombat());
                },General.random(4000,5000))) {
                waitForKill();
                Antiban.get().sleepReactionTime();
            }
        } else {
            aCamera.turnToTile(npc);
        }
    }

    public void waitForKill() {
        Vars.get().subStatus = "AFKing";
        Logger.log("[CombatStrategy] Waiting for npc to be killed.");
        Antiban.get().generateTrackers(3000);
        Timing.waitCondition(() -> {
            General.sleep(100,300);
            checkPrayer();
            checkAndEat();
            Antiban.get().timedActions();
            return Combat.getTargetEntity() == null || !isProtectingEscort();
        }, General.random(20000,30000));
        Antiban.get().setLast_under_attack_time(Timing.currentTimeMillis());
    }

    public boolean isProtectingEscort() {
        RSNPC attacker = getEscortAttacker();
        if (attacker != null) {
            Logger.log("[CombatStrategy] Escort is under attack.");
            RSCharacter character = Player.getRSPlayer().getInteractingCharacter();
            if (character != null) {
                return character.equals(attacker);
            }
        }
        return true;
    }

    public boolean checkPrayer() {
        if (Prayer.getPrayerPoints() <= Vars.get().restorePrayerAt) {
            Logger.log("[CombatStrategy] Restoring prayer points.");
            RSItem potion = Entities.find(ItemEntity::new)
                    .nameContains("Prayer potion", "Super restore")
                    .actionsContains("Drink")
                    .custom((rsItem -> {
                        RSItemDefinition itemDefinition = rsItem.getDefinition();
                        return itemDefinition == null || !itemDefinition.isNoted();
                    })).getFirstResult();
            if (potion != null) {
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
        int currentHp = Combat.getHP();
        if (currentHp < Antiban.get().getEat_at()) {
            Logger.log("[CombatStrategy] Restoring hitpoints.");
            RSItem food = Entities.find(ItemEntity::new)
                    .actionsContains("Eat", "Drink")
                    .custom((rsItem -> {
                        RSItemDefinition itemDefinition = rsItem.getDefinition();
                        return itemDefinition == null || !itemDefinition.isNoted();
                    })).getFirstResult();
            if (food != null) {
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
        return Arrays.stream(npcs)
                .filter(rsnpc -> rsnpc.isInteractingWithMe())
                .findFirst()
                .orElse(Antiban.get().selectNextTarget(npcs));
    }

    public RSNPC getEscortAttacker() {
        RSNPC escort = Vars.get().getSettings().escortDifficulty.findInInstance();
        if (escort != null && escort.isInCombat()) {
            return Entities.find(NpcEntity::new)
                    .nameContains(npcNames())
                    .custom(rsnpc -> {
                        RSCharacter character = rsnpc.getInteractingCharacter();
                        return character != null && character.equals(escort);
                    })
                    .getFirstResult();
        }
        return null;
    }
}
