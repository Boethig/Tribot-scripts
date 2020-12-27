package scripts.TTrekker.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSNPC;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.framework.Node;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.walker_engine.interaction_handling.InteractionHelper;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

public class StartTrekk extends Node {

    public StartTrekk(final ACamera aCamera) {
        super(aCamera);
    }

    public boolean validate() {
        return Utils.isInStartingArea()
                && !Inventory.isFull()
                && !Utils.isInTrekk()
                && !Utils.hasRewardsToken()
                && Utils.hasTools();
    }

    public void execute() {
        if (NPCInteraction.isConversationWindowUp()) {
            Vars.get().subStatus = "Talking to Escort";
            NPCInteraction.handleConversation(Constants.OPTIONS);
            Timing.waitCondition(() -> Vars.get().escorts.findInInstance().length > 0 ||
                    NPCInteraction.isConversationWindowUp(), General.random(1250 + Vars.get().sleepOffset, 1500 + Vars.get().sleepOffset));
        } else {
            RSNPC escort = Antiban.get().selectNextTarget(Vars.get().escorts.find());
            if (escort != null) {
                Vars.get().subStatus = "Clicking on Escort";
                if (!escort.isOnScreen() || !escort.isClickable()) {
                    aCamera.turnToTile(escort.getPosition());
                }
                if (InteractionHelper.click(escort, "Escort")) {
                    NPCInteraction.waitForConversationWindow();
                }
            }
        }
    }

    public String status() {
        return "Starting Trekk:";
    }
}