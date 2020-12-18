package scripts.boe_api.social;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.types.RSInterface;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.InterfaceEntity;
import scripts.dax_api.walker.utils.AccurateMouse;

public class AutoChat {

    public static final int PUBLIC_CHAT = 162;
    public static final int CHAT_STATUS = 17;
    public static final int CHAT_ACTIONS = 13;
    public static final int CHAT_MESSAGE = 44;

    public static boolean set(String message) {
        RSInterface autochat = Entities.find(InterfaceEntity::new)
                .inMasterAndChild(PUBLIC_CHAT, CHAT_ACTIONS)
                .actionContains("Setup your autochat")
                .getFirstResult();
        if (autochat != null) {
            if (AccurateMouse.click(autochat, "Setup your autochat")) {
                if (Timing.waitCondition(() -> Entities.find(InterfaceEntity::new)
                        .inMasterAndChild(PUBLIC_CHAT, CHAT_MESSAGE)
                        .isNotHidden()
                        .getFirstResult() != null, General.random(4000, 6000))) {
                    General.sleep(General.randomSD(600, 175));
                    RSInterface chatMessage = Entities.find(InterfaceEntity::new)
                            .inMasterAndChild(PUBLIC_CHAT, CHAT_MESSAGE)
                            .isNotHidden()
                            .getFirstResult();
                    if (chatMessage != null) {
                        Keyboard.typeSend(message);
                        General.sleep(General.randomSD(70, 300, 100));
                        return isOn();
                    }
                }
            }
        }
        return false;
    }

    public static boolean isOn() {
        return Entities.find(InterfaceEntity::new)
                .inMasterAndChild(PUBLIC_CHAT, CHAT_STATUS)
                .textContains("Autochat")
                .getFirstResult() != null;
    }

    public static boolean pause() {
        return selectChatAction("Pause autochat");
    }

    public static boolean resume() {
        return selectChatAction("Resume autochat");
    }

    public static boolean selectChatAction(String action) {
        RSInterface autochat = Entities.find(InterfaceEntity::new)
                .inMasterAndChild(PUBLIC_CHAT, CHAT_ACTIONS)
                .actionContains(action)
                .getFirstResult();
        if (autochat != null) {
            if (AccurateMouse.click(autochat, action)) {
                return Timing.waitCondition(() ->  {
                    General.sleep(100, 300);
                    return Entities.find(InterfaceEntity::new)
                            .inMasterAndChild(PUBLIC_CHAT, CHAT_ACTIONS)
                            .actionContains(action)
                            .getFirstResult() == null;
                }, General.random(4000, 6000));
            }
        }
        return false;
    }

}
