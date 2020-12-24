package scripts.TTrekker.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.InterfaceEntity;
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
        RSItem token = OSInventory.findFirstNearestToMouse("Reward token");
        if (NPCInteraction.isConversationWindowUp()) {
            NPCInteraction.handleConversation();
        } else if (token != null) {
            if (Interfaces.isInterfaceSubstantiated(Constants.REWARDS)) {
                RSInterface claimReward = Entities.find(InterfaceEntity::new)
                        .inMaster(Constants.REWARDS)
                        .isSubstantiated()
                        .actionContains("Claim")
                        .getFirstResult();
                if (claimReward != null) {
                    Vars.get().subStatus = "Claiming";
                    //TODO: add antiban reaction times
                    if (AccurateMouse.click(claimReward, "Claim")) {
                        Timing.waitCondition(() -> {
                            Antiban.get().waitItemInteractionDelay();
                            return NPCInteraction.isConversationWindowUp();
                        }, General.random(3000 + Vars.get().sleepOffset, 5000 + Vars.get().sleepOffset));
                    }
                } else {
                    Vars.get().subStatus = "Selecting Reward";
                    RSInterface rewardSelect = Entities.find(InterfaceEntity::new)
                            .inMaster(Constants.REWARDS)
                            .isSubstantiated()
                            .textContains(Vars.get().reward.getName())
                            .getFirstResult();
                    if (rewardSelect != null) {
                        //TODO: add antiban reaction times
                        if (AccurateMouse.click(rewardSelect, "Details")) {
                            Timing.waitCondition(() -> Interfaces.isInterfaceSubstantiated(Constants.REWARDS, Constants.CLAIMCHILD), General.random(2000 + Vars.get().sleepOffset, 4000 + Vars.get().sleepOffset));
                        }
                    }
                }
            } else {
                Vars.get().subStatus = "Opening up Interface";
                GameTab.open(GameTab.TABS.INVENTORY);
                if (AccurateMouse.click(token, "Look-at")) {
                    Timing.waitCondition(() -> {
                        Antiban.get().waitItemInteractionDelay();
                        return Interfaces.isInterfaceSubstantiated(Constants.REWARDS);
                    }, General.random(3000 + Vars.get().sleepOffset, 5000 + Vars.get().sleepOffset));
                }
            }
        }
    }

    public String status() {
        return "Claiming Rewards: ";
    }
}