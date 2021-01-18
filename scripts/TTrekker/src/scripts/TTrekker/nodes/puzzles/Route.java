package scripts.TTrekker.nodes.puzzles;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.types.RSInterface;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.InterfaceEntity;
import scripts.boe_api.framework.Node;
import scripts.boe_api.utilities.Antiban;
import scripts.boe_api.utilities.Logger;
import scripts.dax_api.walker.utils.AccurateMouse;

public class Route extends Node {

    public Route() { }

    public boolean validate() {
        return Utils.isInTrekkRoute();
    }

    public void execute() {
        if (!Vars.get().hasSelectedRoute) {
            RSInterface routeSelect = Entities.find(InterfaceEntity::new)
                    .textEquals(Vars.get().getSettings().route.getName())
                    .actionEquals("Select")
                    .getFirstResult();
            if (routeSelect != null) {
                Logger.log("[Route] Selecting %s", Vars.get().getSettings().route.getName());
                if (AccurateMouse.click(routeSelect)) {
                    Antiban.get().generateTrackers(4000);
                    Vars.get().hasSelectedRoute = true;
                    Antiban.get().sleepReactionTime();
                }
            }
        } else {
            Logger.log("[Route] Waiting for Trekk puzzle");
            Timing.waitCondition(() -> {
                General.sleep(50,150);
                Antiban.get().timedActions();
                return !Utils.isInTrekkRoute();
            }, General.random(5000,10000));
        }
    }

    public String status() {
        return "Navigating";
    }
}
