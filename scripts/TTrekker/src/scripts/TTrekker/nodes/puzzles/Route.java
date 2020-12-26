package scripts.TTrekker.nodes.puzzles;

import org.tribot.api2007.types.RSInterface;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.InterfaceEntity;
import scripts.boe_api.framework.Node;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.walker.utils.AccurateMouse;

public class Route extends Node {

    public Route() { }

    public boolean validate() {
        return Utils.isInTrekkRoute();
    }

    public void execute() {
        if (!Vars.get().hasSelectedRoute) {
            RSInterface routeSelect = Entities.find(InterfaceEntity::new)
                    .textEquals(Vars.get().route.getName())
                    .actionEquals("Select")
                    .getFirstResult();
            if (routeSelect != null) {
                Vars.get().subStatus = "Selecting " + Vars.get().route.getName();
                Antiban.get().getReactionTime();
                if (AccurateMouse.click(routeSelect)) {
                    Vars.get().hasSelectedRoute = true;
                    Antiban.get().sleepReactionTime();
                }
            }
        } else {
            Vars.get().subStatus = "Antiban";
            Antiban.get().timedActions();
        }
    }

    public String status() {
        return "Navigating:";
    }
}
