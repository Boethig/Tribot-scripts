package scripts.TTrekker.bog;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
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
        boolean hasReachedDestination = false;
        if (path.get(path.size() - 1).getY() <= Player.getPosition().getY()) {
            General.println("Path Reversed");
            Collections.reverse(path);
        }
        final RSTile destination = path.get(path.size() - 1).getPosition();
        final int playerIndex = getPlayerIndex(path);
        final int start = (playerIndex == -1) ? 0 : (playerIndex + 1);
        General.println("Starting index: " + start);
        for (int i = start; i < path.size(); ++i) {
            final RSObject bog = path.get(i).getBog();
            if (bog != null) {
//                Antiban.getReactionTime();
                if (!walkBogTile(bog)) {
                    General.println("Error clicking on tile @ " + bog.getPosition().toString());
                    break;
                }
                General.println("Successfully clicked on tile @ " + bog.getPosition().toString());
//                Antiban.sleepReactionTime();
            }
        }
        if (Player.getPosition().equals(destination) && Objects.find(5, 13832).length > 0) {
            General.println("Player position: " + Player.getPosition() + "equals" + destination);
            hasReachedDestination = true;
        }
        return hasReachedDestination;
    }

    private static int getPlayerIndex(final ArrayList<BogNode> list) {
//        BogNode node = list.stream().filter((bogNode -> bogNode.getPosition().equals(Player.getPosition()))).findFirst().get();
//        return list.indexOf(node);
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPosition().equals(Player.getPosition())) {
                return i;
            }
        }
        return index;
    }

    public static boolean walkBogTile(final RSObject bog) {
        if (!bog.isOnScreen() || !bog.isClickable()) {
            Camera.turnToTile(bog.getPosition());
        }
        return AccurateMouse.click(bog, "Stand-on") && Timing.waitCondition(() -> {
            General.random(100, 300);
            return Player.getPosition().equals(bog.getPosition()) && !Player.isMoving();
        }, General.random(3000, 5000));
    }
}