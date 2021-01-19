package scripts.TTrekker.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.InterfaceEntity;
import scripts.boe_api.framework.Node;
import scripts.boe_api.inventory.OSInventory;
import scripts.boe_api.utilities.Antiban;
import scripts.boe_api.utilities.Logger;
import scripts.dax_api.walker.utils.AccurateMouse;

public class Claim extends Node {

    public boolean validate() {
        return !Utils.isInTrekk() && Utils.hasRewardsToken();
    }

    public void execute() {
        RSItem token = OSInventory.findFirstNearestToMouse("Reward token");
        if (token != null) {
            if (Interfaces.isInterfaceSubstantiated(Constants.REWARDS)) {
                RSInterface claimReward = Entities.find(InterfaceEntity::new)
                        .inMaster(Constants.REWARDS)
                        .isSubstantiated()
                        .actionContains("Claim")
                        .getFirstResult();
                if (claimReward != null) {
                    Logger.log("[Rewards] Claiming %s", Vars.get().getSettings().reward.getName());
                    if (AccurateMouse.click(claimReward, "Claim")) {
                        Timing.waitCondition(() -> {
                            General.sleep(100,300);
                            return !Interfaces.isInterfaceSubstantiated(Constants.REWARDS);
                        }, General.random(3000,5000));
                        General.randomSD(500, 627);
                    }
                } else {
                    RSInterface rewardSelect = Entities.find(InterfaceEntity::new)
                            .inMaster(Constants.REWARDS)
                            .isSubstantiated()
                            .textContains(Vars.get().getSettings().reward.getName())
                            .getFirstResult();
                    if (rewardSelect != null) {
                        if (AccurateMouse.click(rewardSelect, "Details")) {
                            Timing.waitCondition(() -> {
                                General.sleep(100,300);
                                return Interfaces.isInterfaceSubstantiated(Constants.REWARDS, Constants.CLAIMCHILD);
                            }, General.random(2000,4000));
                            General.randomSD(600, 828);
                        }
                    }
                }
            } else {
                Logger.log("[Rewards] Looking at Rewards Token");
                Inventory.open();
                if (AccurateMouse.click(token, "Look-at")) {
                    Timing.waitCondition(() -> {
                        Antiban.get().waitItemInteractionDelay();
                        return Interfaces.isInterfaceSubstantiated(Constants.REWARDS);
                    }, General.random(3000,5000));
                    General.randomSD(300,500, 327);
                }
            }
        }
    }

    public String status() {
        return "Claiming Rewards";
    }
}