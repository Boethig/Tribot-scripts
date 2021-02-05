package scripts.TTrekker.nodes;

import org.tribot.api2007.Banking;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.banking.BankItem;
import scripts.boe_api.framework.common_nodes.Restocking;
import scripts.dax_api.shared.helpers.BankHelper;

import java.util.ArrayList;

public class SupplyRestocking extends Restocking {

    public boolean validate() {
        return !Utils.hasTools() && BankHelper.isInBank() || Banking.isInBank();
    }

    @Override
    public ArrayList<BankItem> bankItems() {
        ArrayList items = new ArrayList<>() {{
            add(new BankItem(1, new int[]{Constants.HAMMER}, null));
            add(new BankItem(1, new int[]{Constants.KNIFE},null));
            add(new BankItem(1, null, new String[]{"([A-Za-z])\\w+\\s(axe)"}));
        }};
        if (Vars.get().getSettings().shouldUseStaminas) {
            items.add(new BankItem(1, null, new String[]{"Stamina potion\\([1-4]\\)"}));
        }
        if (!Vars.get().getSettings().shouldEvadeCombat) {
            items.add(new BankItem(1, null, new String[]{"Druid pouch"}));
            items.add(new BankItem(1, null, Constants.SILVER_WEAPONS));
        }
        return items;
    }
}