package scripts.TTrekker.utils;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSVarBit;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.ItemEntity;
import scripts.boe_api.entities.finders.prefabs.NpcEntity;
import scripts.boe_api.entities.finders.prefabs.ObjectEntity;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.walker.utils.AccurateMouse;

public class Utils {

    public static boolean isInStartingArea() {
        return Constants.BURG.contains(Player.getPosition()) || Constants.SALVE.contains(Player.getPosition());
    }

    public static boolean isInTrekk() {
        return RSVarBit.get(Constants.IN_TREKK).getValue() > 0 || Vars.get().escorts.findInInstance() != null;
    }

    public static boolean canTempleTrekk() {
        return RSVarBit.get(Constants.IN_AID_OF_MYQYREQUE_VARBIT).getValue() >= Constants.IN_AID_OF_MYQYREQUE_COMPLETE;
    }

    public static boolean canBurgDeRottRamble() {
        return RSVarBit.get(Constants.DARKNESS_OF_HALLOWVALE_VARBIT).getValue() >= Constants.DARKNESS_OF_HALLOWVALE_COMPLETE;
    }

    public static boolean isInTrekkPuzzle() {
        return RSVarBit.get(Constants.IN_TREKK).getValue() > 2;
    }

    public static boolean isInTrekkRoute() {
        return Interfaces.isInterfaceSubstantiated(329)
                || Objects.findNearest(5, "Signpost").length > 0
                || RSVarBit.get(Constants.IN_TREKK).getValue() == 2;
    }

    public static boolean isInTrekkCombatPuzzle() {
        RSObject evadeEvent = Entities.find(ObjectEntity::new)
                .nameContains("Path","Boat")
                .actionsContains("Evade-event","Continue-trek")
                .getFirstResult();
        return evadeEvent != null && isInTrekkPuzzle();
    }

    public static boolean hasTools() {
        return hasAxe() && hasHammer() && hasKnife() && (!Vars.get().useStaminas || hasStamina());
    }

    public static boolean hasRewardsToken() {
        return Entities.find(ItemEntity::new)
                .nameEquals("Reward token")
                .actionsContains("Look-at")
                .getResults().length > 0;
    }

    public static boolean hasAxe() {
        return Inventory.find(rsItem -> rsItem.getDefinition().getName().matches("([A-Za-z])\\w+\\s(axe)")).length > 0 ||
                Equipment.isEquipped(rsItem -> rsItem.getDefinition().getName().matches("([A-Za-z])\\w+\\s(axe)"));
    }

    public static boolean bankHasAxe() {
        return Banking.find(rsItem -> rsItem.getDefinition().getName().matches("([A-Za-z])\\w+\\s(axe)")).length > 0;
    }

    public static boolean hasRingOfLife() {
        return Equipment.isEquipped(Constants.RING_OF_LIFE);
    }

    public static boolean bankHasRingOfLife() {
        return Banking.find(Constants.RING_OF_LIFE).length > 0;
    }

    public static int getAxeId() {
        final RSItem[] axe = Inventory.find(rsItem -> rsItem.getDefinition().getName().matches("([A-Za-z])\\w+\\s(axe)"));
        if (axe.length > 0) {
            return axe[0].getID();
        }
        return -1;
    }

    public static int getBankAxeId() {
        final RSItem[] axe = Banking.find(rsItem -> rsItem.getDefinition().getName().matches("([A-Za-z])\\w+\\s(axe)"));
        if (axe.length > 0) {
            return axe[0].getID();
        }
        return -1;
    }

    public static boolean hasKnife() {
        return Inventory.getCount(Constants.KNIFE) > 0;
    }

    public static boolean bankHasKnife() {
        return Banking.find(Constants.KNIFE).length > 0;
    }

    public static boolean hasHammer() {
        return Inventory.getCount(Constants.HAMMER) > 0;
    }

    public static boolean bankHasHammer() {
        return Banking.find(Constants.HAMMER).length > 0;
    }

    public static boolean hasTeleportsToStart() {
        return hasMortonTeleport() || hasSalveTeleport();
    }

    public static boolean hasMortonTeleport() {
        return Inventory.getCount(Constants.MORTON_TELEPORT) > 0;
    }

    public static boolean bankHasMortonTeleport() {
        return Banking.find(Constants.MORTON_TELEPORT).length > 0;
    }

    public static boolean hasSalveTeleport() {
        return Inventory.getCount(Constants.SALVE_GRAVEYARD_TELEPORT) > 0;
    }

    public static boolean bankHasSalveTeleport() {
        return Banking.find(Constants.SALVE_GRAVEYARD_TELEPORT).length > 0;
    }

    public static boolean hasStamina() {
        return Inventory.find(Constants.STAMINA_IDS).length > 0;
    }

    public static boolean bankHasStamina() {
        return Banking.find(Constants.STAMINA_IDS).length > 0;
    }

    public static boolean shouldBank() {
        return Inventory.isFull() || !hasTools() || (Vars.get().useStaminas && !hasStamina());
    }

    public static boolean selectItem(final RSItem item) {
        return item != null && AccurateMouse.click(item) && Timing.waitCondition(() -> {
            Antiban.get().waitItemInteractionDelay();
            return Game.getItemSelectionState() == 1;
        }, General.random(2500 + Vars.get().sleepOffset, 4000 + Vars.get().sleepOffset));
    }

    public static boolean canAttackVampyres() {
        return Equipment.isEquipped("Efaritay's aid");
    }

    public static int escortFoodSupplyLeft() {
        return RSVarBit.get(Constants.ESCORT_FOOD_LEFT).getValue();
    }
}