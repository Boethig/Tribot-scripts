package scripts.TTrekker.nodes;

import org.apache.commons.lang3.ArrayUtils;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.framework.Node;
import scripts.boe_api.inventory.OSInventory;
import scripts.dax_api.shared.helpers.BankHelper;
import scripts.dax_api.walker.utils.AccurateMouse;

public class Bank extends Node {

    public boolean validate() {
        return BankHelper.isInBank() || Banking.isInBank();
    }

    public void execute() {
        if (Banking.isBankScreenOpen()) {
            Banking.depositAllExcept(ArrayUtils.addAll(Constants.STAMINA_IDS, Utils.getAxeId(), 2347, 946, 19619, 12406));
            if (!Utils.hasTeleportsToStart()) {
                if (!Utils.hasSalveTeleport() && Utils.bankHasSalveTeleport()) {
                    this.withdraw(1, 19619);
                } else if (!Utils.hasMortonTeleport() && Utils.bankHasMortonTeleport()) {
                    this.withdraw(1, 12406);
                }
            }
            if (!Utils.hasRingOfLife() && Utils.bankHasRingOfLife() && this.withdraw(1, 2570)) {
                final RSItem ringOfLife = OSInventory.findFirstNearestToMouse(2570);
                if (ringOfLife != null && AccurateMouse.click(ringOfLife, "Wear")) {
                    Timing.waitCondition(() -> Equipment.isEquipped(2570), General.random(3000, 4000));
                }
            }
            if (!Utils.hasTools()) {
                if (!Utils.hasKnife() && Utils.bankHasAxe()) {
                    this.withdraw(1, 946);
                }
                if (!Utils.hasHammer() && Utils.bankHasHammer()) {
                    this.withdraw(1, 2347);
                }
                if (!Utils.hasAxe() && Utils.bankHasAxe()) {
                    this.withdraw(1, Utils.getBankAxeId());
                }
            }
            if (Vars.get().getSettings().shouldUseStaminas && !Utils.hasStamina()) {
                final RSItem[] staminas = Banking.find(Constants.STAMINA_IDS);
                if (staminas.length > 0) {
                    final int count = staminas[0].getStack();
                    Banking.withdraw(General.random(1, General.random(2, count)), staminas[0].getID());
                }
            }
            if (Utils.hasTools()) {
                this.closeBankAndWait();
            }
        } else if (BankHelper.openBankAndWait()) {
            Timing.waitCondition(() -> Banking.isBankLoaded(), General.random(2000, 4000));
        }
    }

    private boolean closeBankAndWait() {
        if (Banking.close()) {
            return Timing.waitCondition(() -> {
                General.sleep(General.randomSD(100, 300, 2));
                return !Banking.isBankScreenOpen();
            }, General.random(3000, 5000));
        }
        return false;
    }

    private boolean withdraw(final int count, final int item) {
        return Banking.withdraw(count, item) && Timing.waitCondition(() -> {
            General.sleep(General.randomSD(100, 300, 2));
            return Inventory.getCount(item) > 0;
        }, General.random(3000, 5000));
    }

    public String status() {
        return "Withdrawing Supplies";
    }
}