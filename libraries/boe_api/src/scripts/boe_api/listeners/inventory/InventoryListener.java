package scripts.boe_api.listeners.inventory;

import org.tribot.api2007.types.RSItem;

public interface InventoryListener {

    void inventoryItemAdded(RSItem item, int count);
    void inventoryItemRemoved(RSItem item, int count);

}