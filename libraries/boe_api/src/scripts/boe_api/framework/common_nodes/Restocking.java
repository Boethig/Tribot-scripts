package scripts.boe_api.framework.common_nodes;

import org.apache.commons.lang3.ArrayUtils;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import scripts.boe_api.banking.BankItem;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.BankItemEntity;
import scripts.boe_api.entities.finders.prefabs.ItemEntity;
import scripts.boe_api.framework.Node;
import scripts.boe_api.utilities.ClientLogger;
import scripts.dax_api.shared.helpers.BankHelper;

import java.util.ArrayList;
import java.util.function.Predicate;

public abstract class Restocking extends Node {

    public abstract ArrayList<BankItem> bankItems();

    @Override
    public void execute() {
        if (inventoryItemsFound() == getTotalItemCount()) {
            // We have all items and the correct counts
            closeBankAndWait();
        } else if (Banking.isBankScreenOpen() && Banking.isBankLoaded()) {
            for (final BankItem item : bankItems()) {
                if (item.getIds() != null) {
                    for (final int id : item.getIds()) {
                        if (id != -1) {
                            int invCount = Inventory.find(id).length;
                            if (invCount < item.getCount()) {
                                RSItem bankItem = Entities.find(BankItemEntity::new)
                                        .idEquals(id)
                                        .getFirstResult();
                                if (bankItem != null) {
                                    int stack = bankItem.getStack();
                                    if (stack < item.getCount()) {
                                        ClientLogger.warn("Withdraw amount exceeded: %s", stack);
                                    }
                                    if (withdrawItem(item.getCount() - invCount, id)) {
                                        break;
                                    }
                                } else {
                                    ClientLogger.error("Bank item with id: %s not found.", id);
                                }
                            }
                        }
                    }
                } else if (item.getNames() != null) {
                    for (String name : item.getNames()) {
                        if (name != "") {
                            int invCount = Inventory.find(nameMatchPredicate(name)).length;
                            if (invCount < item.getCount()) {
                                RSItem bankItem = Entities.find(BankItemEntity::new)
                                        .custom(nameMatchPredicate(name))
                                        .getFirstResult();
                                if (bankItem != null) {
                                    int stack = bankItem.getStack();
                                    if (stack < item.getCount()) {
                                        ClientLogger.warn("Withdraw amount exceeded: %s", stack);
                                    }
                                    withdrawItemByString(item.getCount() - invCount, bankItem);
                                } else {
                                    ClientLogger.error("Bank item: %s not found.", name);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            BankHelper.openBankAndWait();
        }
    }

    @Override
    public String status() {
        return "Restocking";
    }

    private int getTotalItemCount() {
        return bankItems().stream().mapToInt(BankItem::getCount).reduce(0, Integer::sum);
    }

    private int inventoryItemsFound() {
        int[] itemIds = bankItems().stream().map(BankItem::getIds).reduce(ArrayUtils::addAll).orElse(new int[]{});
        String[] nameMatches = bankItems().stream().map(BankItem::getNames).reduce(ArrayUtils::addAll).orElse(new String[]{});
        return Entities.find(ItemEntity::new)
                .or(true)
                .idEquals(itemIds)
                .custom(namesMatchPredicate(nameMatches))
                .getResults().length;
    }

    private boolean withdrawItem(int count, int id) {
        return Banking.withdraw(count, id) && Timing.waitCondition(() -> {
            General.sleep(100,300);
            return Inventory.getCount(id) > 0;
        }, General.random(3000,5000));
    }

    private Predicate<RSItem> nameMatchPredicate(String name) {
        return rsItem -> rsItem.getDefinition().getName().matches(name);
    }

    private Predicate<RSItem> namesMatchPredicate(String ...names) {
        return rsItem -> {
            for (String name: names) {
                if (rsItem.getDefinition().getName().matches(name)) {
                    return true;
                }
            }
            return false;
        };
    }

    private boolean withdrawItemByString(int count, RSItem item) {
        return Banking.withdrawItem(item,count) && Timing.waitCondition(() -> {
            General.sleep(100,300);
            return Inventory.getCount(item.getDefinition().getName()) > 0;
        }, General.random(3000,5000));
    }

    private boolean closeBankAndWait() {
        return Banking.close() && Timing.waitCondition(() -> {
                General.sleep(100,300);
                return !Banking.isBankScreenOpen();
            }, General.random(3000,5000));
    }
}
