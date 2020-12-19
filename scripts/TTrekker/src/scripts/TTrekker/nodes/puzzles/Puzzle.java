package scripts.TTrekker.nodes.puzzles;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.framework.Node;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.ObjectEntity;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

public abstract class Puzzle extends Node {

    public Puzzle() {}

    public Puzzle(final ACamera aCamera) { super(aCamera); }

    public boolean continueTrek() {
        RSObject continueTrekk = Entities.find(ObjectEntity::new)
                .idEquals(Constants.CONTINUE_TREK)
                .getFirstResult();
        if (continueTrekk != null) {
            final RSTile ctPosition = continueTrekk.getPosition();
            if (!continueTrekk.isClickable() || !continueTrekk.isOnScreen()) {
                Camera.turnToTile(ctPosition);
            }
            if (Player.getPosition().distanceTo(ctPosition) < 12) {
                Vars.get().subStatus = "Leaving";
                if (AccurateMouse.click(continueTrekk, "Continue-trek")) {
                    return Timing.waitCondition(() -> {
                        General.sleep(General.randomSD(100, 300, 2));
                        return Utils.isInTrekkRoute() || NPCInteraction.isConversationWindowUp();
                    }, General.randomLong(8050 + Vars.get().sleepOffset, 10005 + Vars.get().sleepOffset));
                }
            } else if (AccurateMouse.clickMinimap(ctPosition)) {
                Vars.get().subStatus = "Clicking on Minimap";
                Timing.waitCondition(() -> {
                    General.sleep(General.randomSD(100, 300, 2));
                    return Player.getPosition().distanceTo(ctPosition) < General.random(3, 7);
                }, General.random(2000 + Vars.get().sleepOffset, 4000 + Vars.get().sleepOffset));
            } else {
                Vars.get().subStatus = "Webwalking";
                WebWalking.walkTo(ctPosition, () -> continueTrekk.isOnScreen() && ctPosition.isClickable(), General.random(200, 400));
            }
        }
        return false;
    }

    public boolean evadePath() {
        Antiban.get().activateRun();
        RSObject path = Entities.find(ObjectEntity::new)
                .idEquals(Constants.EVADE_PATH)
                .getFirstResult();

        if (path == null) { return false; }
        if (!path.isOnScreen() || !path.isClickable()) {
            Camera.turnToTile(path);
        }
        if (AccurateMouse.click(path, "Evade-event")) {
            return Timing.waitCondition(() -> {
                General.sleep(General.randomSD(100, 300, 2));
                return Utils.isInTrekkRoute() || NPCInteraction.isConversationWindowUp();
            }, General.random(12000, 14500));
        } else if (AccurateMouse.clickMinimap(path)) {
            Timing.waitCondition(() -> {
                General.sleep(General.randomSD(100, 300, 2));
                return Player.getPosition().distanceTo(path.getPosition()) < General.randomSD(5, 3) || path.isOnScreen();
            }, General.random(4000, 6000));
        } else {
           WebWalking.walkTo(path.getPosition(), () -> path.isClickable() && path.isOnScreen(), General.random(300, 500));
        }
        return false;
    }

    public String status() {
        return this.getClass().getSimpleName() + " Puzzle:";
    }
}
