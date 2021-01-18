package scripts.TTrekker.nodes;

import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
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
            if (Constants.SALVE.contains(Player.getPosition())) {
                Vars.get().subStatus = "Canifis Bank";
                DaxWalker.walkToBank(RunescapeBank.CANIFIS);
            } else if (Constants.BURG.contains(Player.getPosition())) {
                Vars.get().subStatus = "Burgh de Rott Bank";
                WebWalking.walkTo(Constants.BURG_BANK.getRandomTile());
            } else {
                Vars.get().subStatus = "Bank";
                DaxWalker.walkToBank();
            }
        } else {
            Vars.get().subStatus = "Start";
            RSArea area;
            if (Utils.hasSalveTeleport()) {
                area = Constants.SALVE;
            } else if (Utils.hasMortonTeleport()) {
                area = Constants.BURG_ENT;
            } else {
                area = Constants.BURG.getRandomTile().distanceTo(Player.getPosition()) > Constants.SALVE.getRandomTile().distanceTo(Player.getPosition()) ? Constants.SALVE : Constants.BURG;
            }
            DaxWalker.walkTo(area.getRandomTile());
        }
    }

    public String status() {
        return "Walking to:";
    }
}
