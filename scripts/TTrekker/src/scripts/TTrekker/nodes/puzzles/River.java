package scripts.TTrekker.nodes.puzzles;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.nodes.Trekk;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.ObjectEntity;
import scripts.boe_api.inventory.OSInventory;
import scripts.boe_api.utilities.Antiban;
import scripts.boe_api.utilities.Math;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

import java.util.Arrays;

public class River extends Puzzle {

    public River(final ACamera aCamera) {
        super(aCamera);
    }

    public boolean validate() {
        return Objects.find(20, "Swamp tree").length > 0 && Utils.isInTrekkPuzzle();
    }

    public void execute() {
        final RSObject swampTreeBranch = Entities.find(ObjectEntity::new)
                .actionsContains("Swing-from")
                .getFirstResult();
        if (Objects.findNearest(10, 13832).length > 0) {
            continueTrek();
        } else if (swampTreeBranch != null) {
            this.swingOnVine(swampTreeBranch);
        } else if (Inventory.getCount(7777) > 0) {
            this.attachRope();
        } else {
            if (Inventory.getCount(946) > 0) {
                if (Inventory.getCount(7778) >= 3) {
                    this.createRope();
                } else {
                    RSObject swampTree = Entities.find(ObjectEntity::new)
                            .actionsContains("Cut-vine")
                            .getFirstResult();
                    if (swampTree != null) {
                        this.collectVine(swampTree);
                    }
                }
            } else {
                this.searchBackpack();
            }
        }
    }

    public void collectVine(final RSObject tree) {
        Vars.get().subStatus = "Collecting vines";
        final RSItem[] knife = Inventory.find(946);
        if (knife.length > 0 && !Inventory.isFull()) {
            if (Game.getItemSelectionState() == 1) {
                final int count = Inventory.getCount(7778);
                if (tree.isClickable() && tree.isOnScreen()) {
                    if (AccurateMouse.click(tree, "Use Knife")) {
                        Timing.waitCondition(() -> {
                            General.sleep(General.randomSD(100, 300, 2));
                            return Inventory.getCount(7778) > count && Game.getItemSelectionState() != 1;
                        }, General.random(2000 + Vars.get().sleepOffset, 3000 + Vars.get().sleepOffset));
                    } else {
                        this.aCamera.turnToTile(tree.getPosition());
                    }
                } else {
                    this.aCamera.turnToTile(tree.getPosition());
                }
            } else if (Utils.selectItem(knife[0])) {
                Antiban.get().waitItemInteractionDelay();
            }
        }
    }

    public void createRope() {
        Vars.get().subStatus = "Creating Rope";
        final RSItem[] vines = OSInventory.findNearestToMouse(7778);
        if (vines.length > 0) {
            if (Game.getItemSelectionState() == 1 && Game.getSelectedItemName().equals("Short vine")) {
                if (AccurateMouse.click(vines[1], "Use")) {
                    Timing.waitCondition(() -> {
                        General.sleep(General.randomSD(100, 300, 2));
                        return NPCInteraction.isConversationWindowUp();
                    }, General.random(2000 + Vars.get().sleepOffset, 3000 + Vars.get().sleepOffset));
                }
            } else if (Utils.selectItem(vines[0])) {
                Antiban.get().waitItemInteractionDelay();
            }
        }
    }

    public void attachRope() {
        Vars.get().subStatus = "Attaching Rope";
        RSObject swampTreeBranch = Entities.find(ObjectEntity::new)
                .nameContains("Swamp tree branch")
                .sortByDistance()
                .getFirstResult();
        if (swampTreeBranch != null) {
            if (Game.getItemSelectionState() == 1 && Game.getSelectedItemName().equals("Long vine")) {
                if (!swampTreeBranch.isOnScreen() || !swampTreeBranch.isClickable()) {
                    this.aCamera.setCameraAngle(General.random(60, 100));
                    this.aCamera.setCameraRotation(General.random(215, 315));
                }
                if (swampTreeBranch.isOnScreen() && swampTreeBranch.isClickable()) {
                    if (AccurateMouse.click(swampTreeBranch, "Use Long vine")) {
                        Timing.waitCondition(() -> {
                            General.sleep(General.randomSD(100, 300, 2));
                            return Objects.find(10, 13846).length > 0 && Game.getItemSelectionState() != 1;
                        }, General.random(4500 + Vars.get().sleepOffset, 6502 + Vars.get().sleepOffset));
                    } else {
                        this.aCamera.setCameraAngle(General.random(60, 100));
                        this.aCamera.setCameraRotation(General.random(215, 315));
                    }
                }
            } else {
                final RSItem[] rope = OSInventory.findNearestToMouse(7777);
                if (rope.length > 0 && Utils.selectItem(rope[0])) {
                    Antiban.get().waitItemInteractionDelay();
                }
            }
        }
    }

    public void searchBackpack() {
        final RSObject backpack = Entities.find(ObjectEntity::new)
                .idEquals(Constants.BACKPACK)
                .getFirstResult();
        if (backpack != null) {
            Vars.get().subStatus = "Searching Backpack";
            Vars.get().abc2WaitTimes.add(Antiban.get().getReactionTime());
            Antiban.get().sleepReactionTime();
            if (backpack.isOnScreen() && backpack.click("Search")) {
                Antiban.get().generateTrackers(Math.calculateAverage(Vars.get().abc2WaitTimes));
                Timing.waitCondition(() -> {
                    General.sleep(General.randomSD(100, 300, 2));
                    return Inventory.getCount(946) > 0;
                }, General.random(3005 + Vars.get().sleepOffset, 5005 + Vars.get().sleepOffset));
            } else {
                this.aCamera.turnToTile(backpack.getPosition());
            }
        }
    }

    public void swingOnVine(final RSObject vine) {
        Vars.get().subStatus = "Swinging Vine";
//        Vars.get().abc2WaitTimes.add(Antiban.get().getReactionTime());
//        Antiban.get().sleepReactionTime();
        if (!vine.isOnScreen() || !vine.isClickable()) {
            Camera.turnToTile(vine);
        }
        if (Game.getItemSelectionState() != 1) {
            if (AccurateMouse.click(vine,"Swing-from")) {
//                Antiban.get().generateTrackers(Math.calculateAverage(Vars.get().abc2WaitTimes));
                Timing.waitCondition(() -> {
                    General.sleep(General.randomSD(100, 300, 2));
                    return Interfaces.isInterfaceSubstantiated(NPCChat.getClickContinueInterface());
                }, General.random(4005 + Vars.get().sleepOffset, 8005 + Vars.get().sleepOffset));
            } else {
                aCamera.turnToTile(vine.getPosition());
            }
        } else if (AccurateMouse.click(vine, "Use")) {
            Timing.waitCondition(() -> Game.getItemSelectionState() != 1, General.random(1500, 2500));
        }
    }

    public String status() {
        return super.status();
    }
}