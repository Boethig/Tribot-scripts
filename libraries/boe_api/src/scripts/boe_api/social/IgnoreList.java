package scripts.boe_api.social;

import java.util.ArrayList;
import java.util.List;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceComponent;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.InterfaceEntity;
import scripts.dax_api.walker.utils.AccurateMouse;

public class IgnoreList {

    private final static int IGNORE_MASTER = 432;
    private final static int FRIENDS_MASTER = 429;
    private final static int NAMES_CHILD = 9;
    private final static int INPUT_MASTER = 162;

    public static boolean isOpen()
    {
        return GameTab.getOpen() == TABS.IGNORE;
    }

    public static boolean openTab() {
        if (isOpen())
            return true;
        if (!GameTab.open(TABS.IGNORE) && GameTab.open(TABS.FRIENDS)) {
            RSInterface switchToIgnoreList = Entities.find(InterfaceEntity::new)
                    .inMaster(FRIENDS_MASTER)
                    .actionContains("View Ignore List")
                    .getFirstResult();
            if (switchToIgnoreList != null) {
                return AccurateMouse.click(switchToIgnoreList);
            }
        }

        return false;
    }

    public static List<String> getNames() {
        List<String> names = new ArrayList<String>();

        RSInterfaceChild namesChild = Interfaces.get(IGNORE_MASTER, NAMES_CHILD);

        if (namesChild != null && namesChild.getChildren() != null) {
            for (RSInterfaceComponent component : namesChild.getChildren()) {
                if (component.getText() != null) {
                    names.add(component.getText());
                }
            }
        }
        return names;
    }

    public static boolean addName(String name) {

        if (!isOpen()) {
            openTab();
        }

        RSInterface addName = Entities.find(InterfaceEntity::new)
                .inMaster(IGNORE_MASTER)
                .actionEquals("Add Name")
                .isNotHidden()
                .getFirstResult();

        if (addName != null && AccurateMouse.click(addName)) {

            General.sleep(General.randomSD(90,15));

            RSInterface inputText = Entities.find(InterfaceEntity::new)
                    .inMaster(INPUT_MASTER)
                    .textContains("Enter name")
                    .isNotHidden()
                    .getFirstResult();

            if (inputText != null) {
                Keyboard.typeSend(name);
                General.sleep(General.randomSD(250, 75));
                return true;
            }
        }

        return false;
    }

    public static boolean removeName(String name) {

        if (!isOpen()) {
            openTab();
        }

        int index = indexOf(name);

        if (index != -1) {
            RSInterfaceComponent nameComponent = Interfaces.get(IGNORE_MASTER, NAMES_CHILD).getChild(index);
            return AccurateMouse.click(nameComponent, "Delete");
        }

        return false;
    }

    public static boolean removeAll() {
        for (String name : getNames()) {
            removeName(name);
        }

        return getNames().size() == 0;
    }

    public static boolean contains(String name) {
        for (String str : getNames()) {
            if (str.equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    private static int indexOf(String name) {
        List<String> names = getNames();

        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equalsIgnoreCase(name)) {
                return i;
            }
        }

        return -1;
    }

}
