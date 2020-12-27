package scripts.TTrekker.combat;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Player;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.types.RSNPC;
import scripts.TTrekker.data.Escort;
import scripts.TTrekker.data.Escorts;
import scripts.TTrekker.data.Vars;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.NpcEntity;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.walker.utils.AccurateMouse;

public abstract class CombatStrategy {

    abstract public String[] npcNames();

    abstract public Prayer.PRAYERS useProtectionPrayer();

    public boolean handle() {
        RSNPC[] npcs = Entities.find(NpcEntity::new)
                .nameContains(npcNames())
                .actionsContains("Attack")
                .getResults();
        if (npcs.length == 0) {
            return true;
        } else {
            if (Prayer.getPrayerPoints() > 0) {
                Prayer.enable(useProtectionPrayer());
            }
            if (Combat.getTargetEntity() != null || Player.getRSPlayer().isInCombat()) {
                while (Combat.getTargetEntity() != null) {
                    Antiban.get().timedActions();
                    General.sleep(150,250);
                    Vars.get().subStatus = "AFKing";
                }
                //TODO: handle food and potions for escort, user
            } else {
                RSNPC npc = getNPC(npcs);
                if (npc != null) {
                    if (!npc.isClickable() || !npc.isOnScreen()) {
                        Camera.turnToTile(npc);
                    }
                    if (AccurateMouse.click(npc, "Attack")) {
                        Timing.waitCondition(() ->  {
                            General.sleep(100, 300);
                            return Combat.getTargetEntity() != null;
                        }, General.random(2500, 3500));
                    }
                }
            }
        }
        return false;
    }

    public RSNPC getNPC(RSNPC[] npcs) {
        RSNPC[] escorts = Vars.get().escorts.findInInstance();
        return escorts.length > 0
                && escorts[0].isInCombat()
                && escorts[0].getInteractingCharacter() != null
                ? (RSNPC) escorts[0].getInteractingCharacter()
                : Antiban.get().selectNextTarget(npcs);
    }
}
