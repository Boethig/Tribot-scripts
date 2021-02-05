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
        Logger.log("[Trekk] Continuing the trekk.");
        return takePathAction("Continue-trek");
    }

    public boolean evadePath() {
        return takePathAction("Evade-event");
    }

    private boolean takePathAction(String action) {
        RSObject path = Antiban.get().selectNextTarget(Entities.find(ObjectEntity::new)
                .nameEquals("Path", "Boat")
                .actionsContains(action)
                .sortByDistance()
                .getResults());
        if (path != null) {
            if (path.isClickable()) {
                if (AccurateMouse.click(path, action)) {
                    return Timing.waitCondition(() -> {
                        if (NPCInteraction.isConversationWindowUp()) {
                            NPCInteraction.handleConversation();
                        }
                        General.sleep(100,300);
                        return Utils.isInTrekkRoute();
                    }, General.random(10000,12500));
                } else {
                    if (AccurateMouse.clickMinimap(path)) {
                        Timing.waitCondition(() -> {
                            General.sleep(100,300);
                            return isCloseToPath(path) && (path.isOnScreen() || !Player.isMoving());
                        }, General.random(4000,6000));
                    } else {
                        WebWalking.walkTo(path.getPosition(),() -> isCloseToPath(path) && path.isOnScreen(), General.random(300,500));
                    }
                }
            } else {
                aCamera.turnToTile(path.getPosition().toLocalTile());
            }
        } else {
            if (Utils.isInTrekkPuzzle()) {
                Logger.log("[Trekk] Searching for path.");
                Utils.findPath();
            }
        }
        return false;
    }

    public boolean isCloseToPath(RSObject path) {
        return Player.getPosition().distanceTo(path) <= General.randomSD(1,5,3);
    }

    @Override
    public String status() {
        return this.getClass().getSimpleName() + " Puzzle";
    }
}
