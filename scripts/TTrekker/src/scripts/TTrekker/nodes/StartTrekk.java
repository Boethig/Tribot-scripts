package scripts.TTrekker.nodes;

import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSNPC;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Escort;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.framework.Node;
import scripts.boe_api.utilities.Antiban;
import scripts.boe_api.utilities.Logger;
import scripts.dax_api.walker_engine.interaction_handling.InteractionHelper;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

public class StartTrekk extends Node {

    public StartTrekk(final ACamera aCamera) {
        super(aCamera);
    }

    public boolean validate() {
        return Utils.isInStartingArea()
                && Utils.hasTools()
                && !Utils.isInTrekk()
                && !Inventory.isFull()
                && !Utils.hasRewardsToken()
                && !Interfaces.isInterfaceSubstantiated(Constants.SUPPLY_MASTER);
    }

    public void execute() {
        RSNPC escort = Antiban.get().selectNextTarget(Vars.get().getSettings().escortDifficulty.find());
        if (escort != null) {
            Vars.get().currentEscort = Escort.fromInstanceId(escort.getID());
            Logger.log("[Trekk] Escorting %s",escort.getName());
            if (!escort.isOnScreen() || !escort.isClickable()) {
                aCamera.turnToTile(escort.getPosition());
            }
            if (InteractionHelper.click(escort, "Escort")) {
                NPCInteraction.waitForConversationWindow();
            }
        }
    }

    public String status() {
        return "Starting Trekk";
    }
}