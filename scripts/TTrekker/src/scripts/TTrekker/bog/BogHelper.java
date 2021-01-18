package scripts.TTrekker.bog;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.TTrekker.data.Vars;
import scripts.boe_api.utilities.Logger;
import scripts.dax_api.walker.utils.AccurateMouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class BogHelper {
    public static ArrayList<RSObject> getStarting(final RSObject... objects) {
        return (ArrayList<RSObject>) Arrays.stream(objects)
                .filter((rsObject) -> rsObject.getPosition().getY() == getMinY(objects))
                .collect(Collectors.toList());
    }

    public static ArrayList<RSObject> getDestinations(final RSObject... objects) {
        return (ArrayList<RSObject>) Arrays.stream(objects)
                .filter((rsObject) -> rsObject.getPosition().getY() == getMaxY(objects))
                .collect(Collectors.toList());
    }

    public static int getMaxY(final RSObject... objects) {
        return Arrays.stream(objects).mapToInt(object -> object.getPosition().getY()).max().getAsInt();
    }

    public static int getMinY(final RSObject... objects) {
        return Arrays.stream(objects).mapToInt(object -> object.getPosition().getY()).min().getAsInt();
    }

    public static int getMinX(final RSObject... objects) {
        return Arrays.stream(objects).mapToInt(object -> object.getPosition().getX()).min().getAsInt();
    }

    public static int getMaxX(final RSObject... objects) {
        return Arrays.stream(objects).mapToInt(object -> object.getPosition().getX()).max().getAsInt();
    }

    public static boolean traverse(final ArrayList<BogNode> path) {
        if (Vars.get().getSettings().escortDifficulty.isEscortingToBurgDeRott()) {
            Logger.log("[BogHelper] Traveling south, reversing path.");
            Collections.reverse(path);
        }
        RSTile destination = path.get(path.size() - 1).getPosition();
        int playerIndex = getPlayerIndex(path);
        Logger.log("[BogHelper] Starting from player index: %s", Integer.toString(playerIndex));
        for (int i = playerIndex; i < path.size(); ++i) {
            RSObject bog = path.get(i).getBog();
            if (bog != null) {
                if (!walkTo(bog)) {
                    Logger.log("[BogHelper] Error clicking on Bog @ %s",bog.getPosition().toString());
                    break;
                }
                Logger.log("[BogHelper] Clicked on Bog @ %s",bog.getPosition().toString());
            }
        }
        if (Player.getPosition().equals(destination) && Objects.find(5, 13832).length > 0) {
            return true;
        }
        return false;
    }

    private static int getPlayerIndex(final ArrayList<BogNode> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPosition().equals(Player.getPosition())) {
                return i + 1;
            }
        }
        return 0;
    }

    public static boolean walkTo(RSObject bog) {
        if (!bog.isOnScreen() || !bog.isClickable()) {
            Camera.turnToTile(bog.getPosition());
        }
        return AccurateMouse.click(bog, "Stand-on") && Timing.waitCondition(() -> {
            General.random(100,300);
            return Player.getPosition().equals(bog.getPosition()) && !Player.isMoving();
        }, General.random(3000,5000));
    }
}