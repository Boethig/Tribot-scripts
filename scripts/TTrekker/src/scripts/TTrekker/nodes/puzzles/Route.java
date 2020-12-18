package scripts.TTrekker.nodes.puzzles;

import org.tribot.api2007.Interfaces;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.walker.utils.AccurateMouse;

public class Route extends Puzzle {

    public boolean validate() {
        return Utils.isInTrekkRoute();
    }

    public void execute() {
        if (Interfaces.isInterfaceSubstantiated(329, 21) && !Vars.get().hasSelectedRoute) {
            Vars.get().subStatus = "Selecting Route 1";
            Antiban.get().getReactionTime();
            if (AccurateMouse.click(Interfaces.get(329, 21), "Ok")) {
                Antiban.get().sleepReactionTime();
                Vars.get().hasSelectedRoute = true;
            }
        } else {
            Vars.get().subStatus = "Antiban";
            Antiban.get().timedActions();
        }
    }

    public String status() {
        return "Choosing Route:";
    }
}
