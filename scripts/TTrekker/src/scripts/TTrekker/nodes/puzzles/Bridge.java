package scripts.TTrekker.nodes.puzzles;

import org.tribot.api.Clicking;
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
        return Entities.find(ObjectEntity::new)
                .nameContains("bridge").getFirstResult() != null && Utils.isInTrekkPuzzle();
    }

    public void solvePuzzle() {
        RSObject bridge = Entities.find(ObjectEntity::new)
                .nameContains("bridge")
                .getFirstResult();
        if (bridge != null) {
            if (isBridgeFixed(bridge)) {
                walkAcrossBridge(bridge);
            } else if (isBridgeFixable(bridge) || doWeHaveMaterials()) {
                    if (bridge.isOnScreen() && bridge.isClickable()) {
                        RSItem[] materials = Inventory.find(Constants.LOGS, Constants.PLANK);
                        if (Game.getItemSelectionState() == 1) {
                            Vars.get().subStatus = "Fixing Bridge";
                            if (AccurateMouse.click(bridge, new String[]{"Use Plank", "Use Logs"})) {
                                Timing.waitCondition(() -> Player.getAnimation() != -1, General.random(7000,9000));
                                if (materials.length > 1) {
                                    Antiban.get().hoverEntity(materials[1]);
                                }
                                Timing.waitCondition(() -> {
                                    General.sleep(100,300);
                                    return Objects.find(10, bridge.getID() + 1).length > 0 && materials.length > Inventory.getCount(Constants.LOGS, Constants.PLANK);
                                },General.random(2500,3500));
                            } else {
                                Vars.get().subStatus = "Rotating camera";
                                aCamera.turnToTile(bridge);
                            }
                        } else {
                            Vars.get().subStatus = "Selecting Materials";
                            Inventory.open();
                            if (materials.length > 0) {
                                Utils.selectItem(materials[0]);
                            }
                        }
                    } else if (AccurateMouse.clickMinimap(bridge.getPosition())) {
                        Timing.waitCondition(() -> {
                            General.sleep(General.randomSD(100, 300, 2));
                            return bridge.isOnScreen() && bridge.isClickable();
                        }, General.random(750 + Vars.get().sleepOffset, 1500 + Vars.get().sleepOffset));
                    } else {
                        Vars.get().subStatus = "Rotating camera";
                        aCamera.turnToTile(bridge);
                    }
            } else {
                RSObject tree = Antiban.get().selectNextTarget(Entities.find(ObjectEntity::new)
                        .nameEquals("Dead tree")
                        .getResults());
                if (tree != null) {
                    chopTree(tree);
                } else if (Inventory.getCount(Constants.PLANK) < 3) {
                    RSNPC zombie = Antiban.get().selectNextTarget(
                            Entities.find(NpcEntity::new)
                                    .nameEquals("Undead Lumberjack")
                                    .custom(rsnpc -> rsnpc.isInteractingWithMe())
                                    .getResults()
                    );
                    if (zombie != null) {
                        if (Combat.getTargetEntity() == null) {
                            if (!zombie.isOnScreen() || !zombie.isClickable()) {
                                aCamera.turnToTile(zombie);
                            }
                            Vars.get().subStatus = "Attacking Lumberjacks";
                            if (AccurateMouse.click(zombie, "Attack") &&
                                    Timing.waitCondition(() -> Combat.getTargetEntity() != null, General.random(2000, 3000))) {
                                long startTime = System.currentTimeMillis();
                                while (Combat.getTargetEntity() != null) {
                                    Antiban.get().timedActions();
                                    lootPlank();
                                    General.sleep(50,150);
                                }
                                Antiban.get().setLastUnderAttackTime(startTime);
                                Antiban.get().generateTrackers((int) (System.currentTimeMillis() - startTime));
                                Antiban.get().sleepReactionTime();
                            } else {
                                aCamera.turnToTile(zombie);
                            }
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

    @Override
    public void resetPuzzle() {
    }

    public boolean isBridgeFixed(RSObject bridge) {
        return Arrays.asList(bridge.getDefinition().getActions()).contains("Cross");
    }

    public boolean isBridgeFixable(RSObject bridge) {
        return !bridge.getDefinition().getName().contains("Broken") && Arrays.asList(bridge.getDefinition().getActions()).contains("Inspect");
    }

    public boolean doWeHaveMaterials() {
        return Inventory.getCount(Constants.LOGS) >= 3 || Inventory.getCount(Constants.PLANK) >= 3;
    }

    private void walkAcrossBridge(final RSObject bridge) {
        Vars.get().subStatus = "Walking across fixed bridge";
        if (!bridge.isOnScreen() || !bridge.isClickable()) {
            aCamera.turnToTile(bridge);
        }
        if (Game.getItemSelectionState() != 1) {
            if (AccurateMouse.click(bridge, "Cross") &&
                    Timing.waitCondition(() -> {
                        General.sleep(100,300);
                        return Player.getPosition().getX() > bridge.getPosition().getX();
                    }, General.random(5000,7000))) {
                isPuzzleComplete = true;
            }
        } else if (AccurateMouse.click(Player.getPosition())) {
            Timing.waitCondition(() -> Game.getItemSelectionState() != 1, General.random(1500, 2500));
        }
    }

    private void chopTree(RSObject tree) {
        Vars.get().subStatus = "Chopping Trees";
        if (!Inventory.isFull()) {
            if (!tree.isOnScreen() || !tree.isClickable()) {
                aCamera.turnToTile(tree);
            }
            if (AccurateMouse.click(tree, "Chop down")) {
                while (Player.isMoving() && tree.getModel() != null) {
                    General.sleep(50L);
                }
                if (Timing.waitCondition(() -> Player.getAnimation() != -1, General.random(2000,3000))) {
                    long startTime = System.currentTimeMillis();
                    while (Player.getAnimation() != -1) {
                        Antiban.get().timedActions();
                        General.sleep(50,70);
                    }
                    Antiban.get().generateTrackers((int) (System.currentTimeMillis() - startTime));
                    Antiban.get().sleepReactionTime();
                }
            }
        }
    }

    private boolean lootPlank() {
        if (!Inventory.isFull() && Inventory.getCount(Constants.PLANK) < 3) {
            Vars.get().subStatus = "Picking up plank";
            RSGroundItem plank = Entities.find(GroundItemEntity::new)
                    .idEquals(Constants.PLANK)
                    .getFirstResult();
            int count = Inventory.getAll().length;
            return AccurateMouse.click(plank, "Take") && Timing.waitCondition(() -> Inventory.getAll().length > count, General.random(1750, 2500));
        }
        return false;
    }
}