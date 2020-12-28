package scripts.TTrekker.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.InterfaceEntity;
import scripts.boe_api.framework.Node;
import scripts.boe_api.inventory.OSInventory;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.interaction_handling.InteractionHelper;

public class SupplyEscort extends Node {

    public SupplyEscort(ACamera aCamera) {
        super(aCamera);
    }

    @Override
    public boolean validate() {
        return Vars.get().foodSupply != null
                && !(Vars.get().foodSupply.getSupplyCount() > Vars.get().foodSupplyAmount)
                && !Utils.isInTrekk()
                && !Utils.hasRewardsToken();
    }

    @Override
    public void execute() {
        if (Interfaces.isInterfaceSubstantiated(Constants.SUPPLY_MASTER)) {
            if (Utils.escortFoodSupplyLeft() > 0) {
                RSInterface close = Entities.find(InterfaceEntity::new)
                        .inMaster(Constants.SUPPLY_MASTER)
                        .actionEquals("Close")
                        .isSubstantiated()
                        .getFirstResult();
                if (close != null) {
                    if (AccurateMouse.click(close,"Close")) {
                        Timing.waitCondition(() -> {
                            General.sleep(100,300);
                            return !Interfaces.isInterfaceSubstantiated(Constants.SUPPLY_MASTER);
                        }, General.random(2000,3000));
                    }
                }
            } else {
                RSItem foodSupply = OSInventory.findFirstNearestToMouse(Vars.get().foodSupply.getId());
                if (foodSupply != null) {
                    int foodSupplyCount = Vars.get().foodSupply.getSupplyCount();
                    if (AccurateMouse.click(foodSupply, "Give")) {
                        Timing.waitCondition(() -> {
                            Antiban.get().waitItemInteractionDelay(3);
                            return Vars.get().foodSupply.getSupplyCount() > foodSupplyCount;
                        }, General.random(3000,5000));
                    }
                }
            }
        } else {
            RSNPC escort = Antiban.get().selectNextTarget(Vars.get().escorts.find());
            if (escort != null) {
                if (!escort.isOnScreen() || !escort.isClickable()) {
                    aCamera.turnToTile(escort);
                }
                if (InteractionHelper.click(escort, "Supply")) {
                    Timing.waitCondition(() -> {
                        General.sleep(100,300);
                        return Interfaces.isInterfaceSubstantiated(Constants.SUPPLY_MASTER);
                    }, General.random(3000,5000));
                }
            }
        }
    }

    @Override
    public String status() {
        return "Supplying Escort";
    }
}
