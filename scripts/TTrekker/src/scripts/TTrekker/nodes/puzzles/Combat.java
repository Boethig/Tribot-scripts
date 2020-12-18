package scripts.TTrekker.nodes.puzzles;

import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSNPC;
import scripts.TTrekker.data.Escorts;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.dax_api.walker.utils.AccurateMouse;

public class Combat extends Puzzle {

    public Combat(final ACamera aCamera) {
        super(aCamera);
    }

    public boolean validate() {
        return Utils.isInTrekkCombatPuzzle();
    }

    public void execute() {
        Vars.get().subStatus = "Evading";
        if (Vars.get().shouldPray) {
            this.enableNPCPrayer();
        }
        if (Vars.get().escorts.equals(Escorts.EASY)) {
            evadePath();
        } else {
            //TODO: medium/hard escorts
        }
    }

    public void enableNPCPrayer() {
        final RSNPC[] snails = NPCs.find(5628);
        if (snails.length > 0) {
            if (!Prayer.isPrayerEnabled(Prayer.PRAYERS.PROTECT_FROM_MISSILES)) {
                Prayer.enable(Prayer.PRAYERS.PROTECT_FROM_MISSILES);
            }
        } else if (!Prayer.isQuickPrayerEnabled(Prayer.PRAYERS.PROTECT_FROM_MELEE)) {
            this.clickQuickPrayer();
        }
    }

    public void disableNPCPrayer() {
        if (Prayer.isQuickPrayerEnabled(Prayer.PRAYERS.PROTECT_FROM_MELEE)) {
            this.clickQuickPrayer();
        } else if (Prayer.isPrayerEnabled(Prayer.PRAYERS.PROTECT_FROM_MISSILES)) {
            Prayer.disable(Prayer.PRAYERS.PROTECT_FROM_MISSILES);
        }
    }

    public void clickQuickPrayer() {
        final RSInterface quickPrayer = Interfaces.get(160, 18);
        if (Interfaces.isInterfaceSubstantiated(quickPrayer)) {
            AccurateMouse.click(quickPrayer, "Activate");
        }
    }

    public String status() {
        return "Combat Puzzle:";
    }
}
