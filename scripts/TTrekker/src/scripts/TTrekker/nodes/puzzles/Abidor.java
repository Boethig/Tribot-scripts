package scripts.TTrekker.nodes.puzzles;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Combat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Projection;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.NpcEntity;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.interaction_handling.InteractionHelper;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

public class Abidor extends Puzzle {

    private boolean hasBeenBlessed;

    public Abidor(final ACamera aCamera) {
        super(aCamera);
    }

    public boolean validate() {
        return (Vars.get().talkedWithAbidor || NPCs.find(Constants.ABIDOR).length > 0) && Utils.isInTrekkPuzzle();
    }

    public void execute() {
        if (hasBeenBlessed && continueTrek()) {
            hasBeenBlessed = false;
        } else {
            RSNPC abidor = Entities.find(NpcEntity::new)
                    .idEquals(Constants.ABIDOR)
                    .getFirstResult();
            if (abidor != null) {
                RSTile abidorPosition = abidor.getPosition();
                if ((abidor.isInteractingWithMe() && !NPCInteraction.isConversationWindowUp()) || hasBeenHealed()) {
                    hasBeenBlessed = true;
                } else {
                    if (!abidor.isOnScreen() || !abidor.isClickable()) {
                        this.aCamera.turnToTile(abidorPosition);
                    }
                    if (abidor.isOnScreen()) {
                        Vars.get().subStatus = "Talking to Abidor";
                        if (InteractionHelper.click(abidor, "Talk-to")) {
                            NPCInteraction.waitForConversationWindow();
                        }
                    } else if (AccurateMouse.clickMinimap(abidorPosition)) {
                        Vars.get().subStatus = "Clicking on Minimap";
                        Timing.waitCondition(abidor::isOnScreen, General.random(2000, 5000));
                    }
                }
            } else {
            }
        }
    }

    public boolean hasBeenHealed() { return Combat.getHP() > Combat.getMaxHP(); }

    public String status() {
        return super.status();
    }
}