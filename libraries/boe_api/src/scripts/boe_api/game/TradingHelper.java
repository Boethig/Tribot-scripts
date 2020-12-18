package scripts.boe_api.game;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Trading;
import org.tribot.api2007.types.RSPlayer;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.PlayerEntity;
import scripts.dax_api.walker.utils.AccurateMouse;

public class TradingHelper {

    public boolean handleTrade(String playerName) {
        switch (Trading.getWindowState()) {
            case FIRST_WINDOW:
                if (tradePlayer(playerName))
                break;
            case SECOND_WINDOW:
                return Trading.hasAccepted(true) && Trading.accept();
            default:
                return false;
        }
        return false;
    }

    public boolean tradePlayer(String playerName) {
        if (Trading.getWindowState() == Trading.WINDOW_STATE.FIRST_WINDOW) return true;
        RSPlayer player = Entities.find(PlayerEntity::new)
                .nameEquals(playerName)
                .getFirstResult();
        if (player != null) {
            if (!player.isOnScreen() || !player.isClickable()) {
                Camera.turnToTile(player.getPosition());
            }
            return AccurateMouse.click(player, "Trade with " + player.getName()) &&
                    Timing.waitCondition(() -> Trading.getWindowState() == Trading.WINDOW_STATE.FIRST_WINDOW, General.random(4000, 6000));
        }
        return false;
    }

    public boolean hasOtherPlayerOfferedItem() {
        return Trading.getOfferedItems(true).length > 0;
    }

    public boolean hasOtherPlayerOfferedItem(int id) {
        if (Trading.getWindowState() != Trading.WINDOW_STATE.FIRST_WINDOW) return false;
        return Trading.getCount(true, id) > 0;
    }

    public boolean hasOtherPlayerOfferedItem(int id, int count) {
        if (Trading.getWindowState() != Trading.WINDOW_STATE.FIRST_WINDOW) return false;
        return Trading.getCount(true, id) == count;
    }

}
