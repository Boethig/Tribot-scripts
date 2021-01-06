package scripts.TTrekker.nodes.puzzles;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.framework.Node;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.ObjectEntity;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.api_lib.DaxWalker;
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
        Vars.get().subStatus = "Continuing Trekk";
        return takePathAction("Continue-trek");
    }

    public boolean evadePath() {
        Vars.get().subStatus = "Evading Event";
        return takePathAction("Evade-event");
    }

    private boolean takePathAction(String action) {
        RSObject path = Antiban.get().selectNextTarget(Entities.find(ObjectEntity::new)
                .nameEquals("Path", "Boat")
                .actionsContains(action)
                .getResults());
        if (path == null || NPCInteraction.isConversationWindowUp()) {
            return false;
        }
        if (!path.isOnScreen() || !path.isClickable()) {
            aCamera.turnToTile(path);
        }
        if (AccurateMouse.click(path, action)) {
            General.sleep(General.randomSD(600, 320));
            if (NPCInteraction.isConversationWindowUp()) {
                return false;
            }
            return Timing.waitCondition(() -> {
                General.sleep(100,300);
                return Utils.isInTrekkRoute();
            }, General.random(12000,14500));
        } else if (AccurateMouse.clickMinimap(path)) {
            Timing.waitCondition(() -> {
                General.sleep(100,300);
                return path.isClickable() || path.isOnScreen();
            }, General.random(4000,6000));
        } else {
            DaxWalker.walkTo(path.getAnimablePosition().toLocalTile());
            WebWalking.walkTo(path.getPosition(), () -> path.isClickable() || path.isOnScreen(), General.random(300, 500));
        }
        return false;
    }

    public String status() {
        return this.getClass().getSimpleName() + " Puzzle:";
    }
}
