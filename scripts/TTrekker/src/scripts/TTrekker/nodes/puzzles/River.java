package scripts.TTrekker.nodes.puzzles;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.ObjectEntity;
import scripts.boe_api.inventory.OSInventory;
import scripts.boe_api.utilities.Antiban;
import scripts.boe_api.utilities.Math;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

public class River extends Puzzle {

    public River(final ACamera aCamera) {
        super(aCamera);
    }

    public boolean validate() {
        return Objects.find(20, "Swamp tree").length > 0 && Utils.isInTrekkPuzzle();
    }

    public void solvePuzzle() {
        RSObject swampTreeBranch = Entities.find(ObjectEntity::new)
                .actionsContains("Swing-from")
                .getFirstResult();
        if (swampTreeBranch != null) {
            swingOnVine(swampTreeBranch);
        } else if (Inventory.getCount(Constants.ROPE) > 0) {
            attachRope();
        } else if (Inventory.getCount(Constants.VINE) >= 3) {
            createRope();
        } else {
            RSObject swampTree = Entities.find(ObjectEntity::new)
                    .actionsContains("Cut-vine")
                    .getFirstResult();
            if (swampTree != null) {
                collectVines(swampTree);
            }
        }
    }

    @Override
    public void resetPuzzle() {
    }

    public void collectVines(RSObject tree) {
        Vars.get().subStatus = "Collecting vines";
        if (!Inventory.isFull()) {
            int count = Inventory.getCount(Constants.VINE);
            if (!tree.isClickable() || !tree.isOnScreen()) {
                aCamera.turnToTile(tree);
            }
            if (AccurateMouse.click(tree, "Cut-vine")) {
                Timing.waitCondition(() -> {
                    General.sleep(100, 300);
                    return Inventory.getCount(7778) > count && Game.getItemSelectionState() != 1;
                }, General.random(2500,3000));
            }
        }
    }

    public void createRope() {
        Vars.get().subStatus = "Creating Rope";
        RSItem[] vines = OSInventory.findNearestToMouse(Constants.VINE);
        if (vines.length > 1) {
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
                    aCamera.setCameraAngle(General.random(60, 100));
                    aCamera.setCameraRotation(General.random(215, 315));
                }
                if (swampTreeBranch.isOnScreen() && swampTreeBranch.isClickable()) {
                    if (AccurateMouse.click(swampTreeBranch, "Use Long vine")) {
                        Timing.waitCondition(() -> {
                            General.sleep(General.randomSD(100, 300, 2));
                            return Objects.find(10, 13846).length > 0 && Game.getItemSelectionState() != 1;
                        }, General.random(4500 + Vars.get().sleepOffset, 6502 + Vars.get().sleepOffset));
                    } else {
                        aCamera.setCameraAngle(General.random(60, 100));
                        aCamera.setCameraRotation(General.random(215, 315));
                    }
                }
            } else {
                RSItem rope = OSInventory.findFirstNearestToMouse(Constants.ROPE);
                if (Utils.selectItem(rope)) {
                    Antiban.get().waitItemInteractionDelay();
                }
            }
        }
    }

    public void swingOnVine(RSObject vine) {
        Vars.get().subStatus = "Swinging Vine";
//        Vars.get().abc2WaitTimes.add(Antiban.get().getReactionTime());
//        Antiban.get().sleepReactionTime();
        if (!vine.isOnScreen() || !vine.isClickable()) {
            aCamera.turnToTile(vine);
        }
        if (Game.getItemSelectionState() != 1) {
            if (AccurateMouse.click(vine,"Swing-from")) {
//                Antiban.get().generateTrackers(Math.calculateAverage(Vars.get().abc2WaitTimes));
                isPuzzleComplete = Timing.waitCondition(() -> {
                    General.sleep(100, 300);
                    return Interfaces.isInterfaceSubstantiated(NPCChat.getClickContinueInterface())
                            || Player.getPosition().getY() > vine.getPosition().getY();
                }, General.random(4000,8000));
            }
        } else if (AccurateMouse.click(vine, "Use")) {
            Timing.waitCondition(() -> {
                General.sleep(100,300);
                return Game.getItemSelectionState() != 1;
            }, General.random(1500,2500));
        }
    }
}