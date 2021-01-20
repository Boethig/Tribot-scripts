package scripts.TTrekker.nodes.puzzles;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.ObjectEntity;
import scripts.boe_api.framework.Node;
import scripts.boe_api.utilities.Antiban;
import scripts.boe_api.utilities.Logger;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

public abstract class Puzzle extends Node {

    protected boolean isPuzzleComplete;

    abstract public void solvePuzzle();

    // Resets any variables local to this puzzle
    abstract public void resetPuzzle();

    @Override
    public void execute() {
        if (isPuzzleComplete) {
            if (continueTrek()) {
                resetPuzzle();
                this.isPuzzleComplete = false;
            }
        } else {
            solvePuzzle();
        }
    }

    public Puzzle(final ACamera aCamera) { super(aCamera); }

    public boolean continueTrek() {
        Logger.log("[Trekk] Continuing the trekk");
        return takePathAction("Continue-trek");
    }

    public boolean evadePath() {
        Logger.log("[Trekk] Evading the trekk puzzle");
        return takePathAction("Evade-event");
    }

    private boolean takePathAction(String action) {
        RSObject path = Antiban.get().selectNextTarget(Entities.find(ObjectEntity::new)
                .nameEquals("Path", "Boat")
                .actionsContains(action)
                .getResults());
        if (path != null) {
            if (!path.isOnScreen() || !path.isClickable()) {
                aCamera.turnToTile(path);
            }
            if (path.isOnScreen() && AccurateMouse.click(path, action)) {
                Timing.waitCondition(() -> {
                    General.sleep(100,300);
                    return Utils.isInTrekkRoute() || NPCInteraction.isConversationWindowUp();
                }, General.random(10000,12500));
                if (NPCInteraction.isConversationWindowUp()) {
                    NPCInteraction.handleConversation();
                }
                return Utils.isInTrekkRoute();
            }
            if (AccurateMouse.clickMinimap(path)) {
                Timing.waitCondition(() -> {
                    General.sleep(100,300);
                    return path.isOnScreen() || !Player.isMoving();
                }, General.random(4000,6000));
            } else {
                WebWalking.walkTo(path.getPosition(), () -> path.isOnScreen(), General.random(300,500));
            }
        } else {
            Utils.findPath();
        }
        return false;
    }

    public String status() {
        return this.getClass().getSimpleName() + " Puzzle";
    }
}
