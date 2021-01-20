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
import scripts.boe_api.utilities.Logger;
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
        Inventory.open();
        Vars.get().subStatus = "Collecting vines";
        if (!Inventory.isFull()) {
            int count = Inventory.getCount(Constants.VINE);
            if (!tree.isClickable() || !tree.isOnScreen()) {
                aCamera.turnToTile(tree);
            }
            Logger.log("[River] Cutting down vine.");
            if (AccurateMouse.click(tree, "Cut-vine")) {
                Timing.waitCondition(() -> {
                    General.sleep(100, 300);
                    return Inventory.getCount(Constants.VINE) > count && Player.getAnimation() == -1;
                }, General.random(3000,4000));
            }
        }
    }

    public void createRope() {
        Logger.log("[River] Cutting down vine.");
        RSItem[] vines = OSInventory.findNearestToMouse(Constants.VINE);
        if (vines.length > 1) {
            if (Game.getItemSelectionState() == 1 && Game.getSelectedItemName().equals("Short vine")) {
                if (AccurateMouse.click(vines[1], "Use")) {
                    Timing.waitCondition(() -> {
                        General.sleep(General.randomSD(100, 300, 2));
                        return NPCInteraction.isConversationWindowUp();
                    }, General.random(2000,3000));
                }
            } else if (Utils.selectItem(vines[0])) {
                Antiban.get().waitItemInteractionDelay();
            }
        }
    }

    public void attachRope() {
        RSObject swampTreeBranch = Entities.find(ObjectEntity::new)
                .nameContains("Swamp tree branch")
                .sortByDistance()
                .getFirstResult();
        if (swampTreeBranch != null) {
            if (Game.getItemSelectionState() == 1 && Game.getSelectedItemName().equals("Long vine")) {
                if (!swampTreeBranch.isOnScreen() || !swampTreeBranch.isClickable()) {
                    rotateCamera();
                }
                if (swampTreeBranch.isOnScreen() && swampTreeBranch.isClickable()) {
                    Logger.log("[River] Attaching rope to tree branch.");
                    if (AccurateMouse.click(swampTreeBranch, "Use Long vine")) {
                        Timing.waitCondition(() -> {
                            General.sleep(General.randomSD(100, 300, 2));
                            return Objects.find(10, 13846).length > 0 && Game.getItemSelectionState() != 1;
                        }, General.random(4500,6500));
                    } else {
                        rotateCamera();
                    }
                }
            } else {
                RSItem rope = OSInventory.findFirstNearestToMouse(Constants.ROPE);
                Logger.log("[River] Selecting rope.");
                if (Utils.selectItem(rope)) {
                    Antiban.get().waitItemInteractionDelay();
                }
            }
        }
    }

    public void rotateCamera() {
        aCamera.setCameraAngle(General.randomSD(60, 100, 81));
        aCamera.setCameraRotation(General.randomSD(215, 315, 303));
    }

    public void swingOnVine(RSObject vine) {
        if (!vine.isOnScreen() || !vine.isClickable()) {
            aCamera.turnToTile(vine);
        }
        if (Game.getItemSelectionState() != 1) {
            Logger.log("[River] Swinging across tree branch.");
            if (AccurateMouse.click(vine,"Swing-from")) {
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