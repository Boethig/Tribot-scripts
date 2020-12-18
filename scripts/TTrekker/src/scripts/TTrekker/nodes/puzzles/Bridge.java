package scripts.TTrekker.nodes.puzzles;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Combat;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.nodes.Trekk;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.GroundItemEntity;
import scripts.boe_api.entities.finders.prefabs.NpcEntity;
import scripts.boe_api.entities.finders.prefabs.ObjectEntity;
import scripts.boe_api.inventory.OSInventory;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.walker.utils.AccurateMouse;

import java.util.Arrays;

public class Bridge extends Puzzle {

    public Bridge(final ACamera aCamera) {
        super(aCamera);
    }

    public boolean validate() {
        return (Objects.find(20, Constants.BRIDGEINSTANCES).length > 0 || NPCs.find(5647).length > 0) && Utils.isInTrekkPuzzle();
    }

    public void execute() {
        RSObject continueTrek = Entities.find(ObjectEntity::new)
                .setDistance(13)
                .idEquals(Constants.CONTINUE_TREK)
                .getFirstResult();
        if (continueTrek != null && Vars.get().walked) {
            continueTrek();
        } else {
            RSObject bridge = Entities.find(ObjectEntity::new)
                    .idEquals(Constants.BRIDGEINSTANCES)
                    .getFirstResult();
            if (bridge != null) {
                if (isBridgeFixed(bridge)) {
                    walkAcrossBridge(bridge);
                } else if (bridge.getID() > 13834 || this.doWeHaveMaterials()) {
                        if (bridge.isOnScreen() && bridge.isClickable()) {
                            if (Game.getItemSelectionState() == 1) {
                                Vars.get().subStatus = "Fixing Bridge";
                                if (AccurateMouse.click(bridge, "Use Plank", "Use Logs")) {
                                    Timing.waitCondition(() -> {
                                        General.sleep(General.randomSD(100, 300, 2));
                                        return Objects.find(10, bridge.getID() + 1).length > 0 && Game.getItemSelectionState() != 1;
                                    }, General.random(4505 + Vars.get().sleepOffset, 6010 + Vars.get().sleepOffset));
                                } else {
                                    Vars.get().subStatus = "Rotating camera";
                                    Camera.turnToTile(bridge);
                                }
                            } else {
                                Vars.get().subStatus = "Selecting Materials";
                                final RSItem material = OSInventory.findFirstNearestToMouse(Constants.LOGS, Constants.PLANK);
                                if (material != null) {
                                    Utils.selectItem(material);
                                }
                            }
                        } else if (AccurateMouse.clickMinimap(bridge.getPosition())) {
                            Timing.waitCondition(() -> {
                                General.sleep(General.randomSD(100, 300, 2));
                                return bridge.isOnScreen() && bridge.isClickable();
                            }, General.random(750 + Vars.get().sleepOffset, 1500 + Vars.get().sleepOffset));
                        } else {
                            Vars.get().subStatus = "Rotating camera";
                            this.aCamera.turnToTile(bridge.getPosition());
                        }
                } else {
                    final RSObject tree = Antiban.get().selectNextTarget(
                            Entities.find(ObjectEntity::new).idEquals(Constants.DEADTREE).getResults()
                    );
                    if (tree != null) {
                        if (Utils.hasAxe()) {
                            chopTree(tree);
                        } else {
                            collectAxe();
                        }
                    } else if (Inventory.getCount(Constants.PLANK) < 3) {
                        RSNPC zombie = Antiban.get().selectNextTarget(NPCs.find(Constants.LUMBERJACK));
                        if (zombie != null) {
                            Vars.get().abc2WaitTimes.add(Antiban.get().getReactionTime());
                            final long startTime = System.currentTimeMillis();
                            if (Combat.getTargetEntity() == null || Combat.getAttackingEntities().length < 1) {
                                if (!zombie.isOnScreen() || !zombie.isClickable()) {
                                    this.aCamera.turnToTile(zombie);
                                }
                                if (AccurateMouse.click(zombie, "Attack")) {
                                    Vars.get().subStatus = "Attacking Lumberjacks";
                                    Timing.waitCondition(() -> Combat.getTargetEntity() != null || Combat.getAttackingEntities().length > 0, General.random(2005 + Vars.get().sleepOffset, 3005 + Vars.get().sleepOffset));
                                    Antiban.get().sleepReactionTime();
                                }
                            }
                            while (Player.getRSPlayer().isInCombat() && Inventory.getCount(Constants.PLANK) < 3) {
                                Antiban.get().timedActions();
                                lootPlank();
                                General.sleep(General.randomSD(50, 70, 2));
                            }
                            if (!Player.getRSPlayer().isInCombat()) {
                                Antiban.get().generateTrackers((int) (System.currentTimeMillis() - startTime));
                                Vars.get().abc2WaitTimes.add(Antiban.get().getReactionTime());
                            }
                        } else {
                            Vars.get().subStatus = "Waiting for Spawn";
                            lootPlank();
                            Antiban.get().timedActions();
                        }
                    }
                }
            }
        }
    }

    public String status() {
        return "Bridge Puzzle:";
    }

    public boolean isBridgeFixed(final RSObject bridge) {
        return bridge != null && Arrays.stream(Constants.FIXED_BRIDGES).anyMatch(id -> id == bridge.getID());
    }

    public boolean doWeHaveMaterials() {
        return Inventory.getCount(Constants.LOGS) >= 3 || Inventory.getCount(Constants.PLANK) >= 3;
    }

    private void walkAcrossBridge(final RSObject bridge) {
        Vars.get().subStatus = "Walking across fixed bridge";
        if (bridge.isOnScreen() || !bridge.isClickable()) {
            this.aCamera.turnToTile(bridge.getPosition());
        }
        if (Game.getItemSelectionState() != 1) {
            if (AccurateMouse.click(bridge, "Walk-across")) {
                Timing.waitCondition(() -> {
                    General.sleep(General.randomSD(100, 300, 2));
                    return Vars.get().walked && Player.getPosition().getX() > bridge.getPosition().getX();
                }, General.random(5005 + Vars.get().sleepOffset, 7005 + Vars.get().sleepOffset));
            }
        } else if (AccurateMouse.click(Player.getPosition())) {
            Timing.waitCondition(() -> Game.getItemSelectionState() != 1, General.random(1500, 2500));
        }
    }

    private void collectAxe() {
        Vars.get().subStatus = "Collecting Axe";
        final RSNPC zombie = Entities.find(NpcEntity::new).idEquals(Constants.ZOMBIE).getFirstResult();
        if (zombie != null) {
            if (!zombie.isOnScreen() || !zombie.isClickable()) {
                Camera.turnToTile(zombie);
            }
            if (AccurateMouse.click(zombie, "Attack")) {
                Timing.waitCondition(() -> {
                    General.sleep(General.randomSD(100, 300, 2));
                    return Combat.getTargetEntity() != null || Combat.getAttackingEntities().length > 0;
                }, General.random(3005 + Vars.get().sleepOffset, 4005 + Vars.get().sleepOffset));
            }
        }
    }

    private void chopTree(final RSObject tree) {
        Vars.get().subStatus = "Chopping Trees";
        if (!Inventory.isFull()) {
            if (!tree.isOnScreen() || !tree.isClickable()) {
                aCamera.turnToTile(tree.getPosition());
            }
            if (AccurateMouse.click(tree, "Chop down")) {
                while (Player.isMoving() && tree.getModel() != null) {
                    General.sleep(50L);
                }
                Timing.waitCondition(() -> Player.getAnimation() != -1, General.random(1750, 2750));
                while (Player.getAnimation() != -1) {
                    General.sleep(General.randomSD(100, 300, 2));
                }
            }
        }
    }

    private void lootPlank() {
        Vars.get().subStatus = "Looting";
        if (!Inventory.isFull() && Inventory.getCount(Constants.PLANK) < 3) {
            RSGroundItem plank = Entities.find(GroundItemEntity::new)
                    .idEquals(Constants.PLANK)
                    .getFirstResult();
            int count = Inventory.getAll().length;
            if (AccurateMouse.click(plank, "Take")) {
                Timing.waitCondition(() -> Inventory.getAll().length > count, General.random(1750, 2500));
            }
        }
    }
}