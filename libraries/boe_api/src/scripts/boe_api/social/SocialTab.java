package scripts.boe_api.social;

import org.tribot.api2007.types.RSInterface;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.InterfaceEntity;
import scripts.dax_api.walker.utils.AccurateMouse;

public enum SocialTab {

    ALL(-1, -1),
    GAME(-1, -1),
    PUBLIC(-1, -1),
    PRIVATE(-1, -1),
    CLAN(-1, -1),
    TRADE(30, 28);

    private int selectedId;

    private int switchId;

    SocialTab(int selectedId, int switchId) {
        this.selectedId = selectedId;
        this.switchId = switchId;
    }

    public boolean switchTo() {
        RSInterface tab = Entities.find(InterfaceEntity::new)
                .inMasterAndChild(162, switchId)
                .actionContains("Switch tab")
                .getFirstResult();

        return tab != null && AccurateMouse.click(tab, "Switch tab");
    }

    public boolean isSelected() {
        RSInterface tab = Entities.find(InterfaceEntity::new)
                .inMasterAndChild(162, selectedId)
                .textureIdEquals(1022)
                .getFirstResult();

        return tab != null;
    }
}
