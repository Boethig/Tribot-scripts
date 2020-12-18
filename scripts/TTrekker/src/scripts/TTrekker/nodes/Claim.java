package scripts.TTrekker.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.boe_api.framework.Node;
import scripts.boe_api.inventory.OSInventory;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

public class Claim extends Node {

    public boolean validate() {
        return Inventory.getCount(Constants.REWARDS_TOKEN) >= Vars.get().claimCount && !Vars.get().inTrekk;
    }

    public void execute() {
        final RSItem[] tokens = OSInventory.findNearestToMouse("Reward token");
        for (final RSItem token : tokens) {
            if (NPCInteraction.isConversationWindowUp()) {
                NPCInteraction.handleConversation();
            } else if (token != null) {
                if (Interfaces.isInterfaceSubstantiated(274)) {
                    if (Interfaces.isInterfaceSubstantiated(274, 12)) {
                        Vars.get().subStatus = "Claiming";
                        if (AccurateMouse.click(Interfaces.get(274, 12), "Claim")) {
                            Timing.waitCondition(() -> {
                                Antiban.get().waitItemInteractionDelay();
                                return NPCInteraction.isConversationWindowUp() || Inventory.getCount(Constants.REWARDS_TOKEN) < 1;
                            }, General.random(3000 + Vars.get().sleepOffset, 5000 + Vars.get().sleepOffset));
                        }
                    } else if (Interfaces.isInterfaceSubstantiated(274, 6, 1)) {
                        Vars.get().subStatus = "Clicking Bowstrings";
                        if (AccurateMouse.click(Interfaces.get(274, 6, 1))) {
                            Timing.waitCondition(() -> Interfaces.isInterfaceSubstantiated(274, 12), General.random(2000 + Vars.get().sleepOffset, 4000 + Vars.get().sleepOffset));
                        }
                    }
                } else {
                    Vars.get().subStatus = "Opening up Interface";
                    if (AccurateMouse.click(token, "Look-at")) {
                        Timing.waitCondition(() -> {
                            Antiban.get().waitItemInteractionDelay();
                            return Interfaces.isInterfaceSubstantiated(274);
                        }, General.random(3000 + Vars.get().sleepOffset, 5000 + Vars.get().sleepOffset));
                    }
                }
            }
        }
    }

    public String status() {
        return "Claiming Tokens..";
    }
}