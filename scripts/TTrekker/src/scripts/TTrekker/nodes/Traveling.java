package scripts.TTrekker.nodes;

import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.framework.Node;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.RunescapeBank;

public class Traveling extends Node {

    public boolean validate() {
        return Vars.get().isEscortDead ||
                (!Utils.isInTrekk() && (!Utils.isInStartingArea() || Utils.shouldBank()));
    }

    public void execute() {
        if (!Utils.hasTeleportsToStart() || Utils.shouldBank()) {
            RunescapeBank bank = null;
            if (Constants.SALVE.contains(Player.getPosition())) {
                Vars.get().subStatus = "Canifis Bank";
                bank = RunescapeBank.CANIFIS;
            } else if (Constants.BURG.contains(Player.getPosition())) {
                Vars.get().subStatus = "Burgh de Rott Bank";
                bank = RunescapeBank.BURG_DE_ROTT;
            }
            DaxWalker.walkToBank(bank);
        } else {
            Vars.get().subStatus = "Start";
            RSArea area = Constants.BURG.getRandomTile().distanceTo(Player.getPosition()) > Constants.SALVE.getRandomTile().distanceTo(Player.getPosition()) ? Constants.SALVE : Constants.BURG;
            DaxWalker.walkTo(area.getRandomTile());
        }
    }

    public String status() {
        return "Walking to:";
    }
}
