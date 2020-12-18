package scripts.boe_api.inventory;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import scripts.dax_api.walker.utils.AccurateMouse;

public class OSInventory {
	

    private static Point getCenterPoint(RSItem i) {
        Rectangle r = i.getArea();
        if (r != null) {
            Point rPoint = r.getLocation();
            return new Point(rPoint.x + r.width / 2 , rPoint.y + r.height / 2);
        }
        return null;
    }

    private static Comparator<RSItem> closestToFarthest = (o1, o2) -> {
        Point botMousePoint = new Point(Mouse.getPos());
        Point p1 = getCenterPoint(o1);
        Point p2 = getCenterPoint(o2);
        if (p1 != null && p2 != null) {
            return Integer.compare((int)botMousePoint.distance(p1), (int)botMousePoint.distance(p2)) > 0 ? 1 : -1;
        }
        return -1;
    };

    public static RSItem[] findNearestToMouse() {
        RSItem[] items = Inventory.getAll();
        Arrays.sort(items, closestToFarthest);
        return items;
    }

    public static RSItem findFirstNearestToMouse() {
        RSItem[] items = findNearestToMouse();
        return items.length > 0 ? items[0]: null;
    }

    public static RSItem[] findNearestToMouse(Predicate<RSItem> filter) {
        RSItem[] items = Inventory.find(filter);
        Arrays.sort(items, closestToFarthest);
        return items;
    }

    public static RSItem findFirstNearestToMouse(Predicate<RSItem> filter) {
        RSItem[] items = findNearestToMouse(filter);
        return items.length > 0 ? items[0]: null;
    }

    public static RSItem[] findNearestToMouse(String... names) {
        RSItem[] items = Inventory.find(names);
        Arrays.sort(items, closestToFarthest);
        return items;
    }

    public static RSItem findFirstNearestToMouse(String... names) {
        RSItem[] items = findNearestToMouse(names);
        return items.length > 0 ? items[0]: null;
    }

    public static RSItem[] findNearestToMouse(int... ids) {
        RSItem[] items = Inventory.find(ids);
        Arrays.sort(items, closestToFarthest);
        return items;
    }

    public static RSItem findFirstNearestToMouse(int... ids) {
        RSItem[] items = findNearestToMouse(ids);
        return items.length > 0 ? items[0]: null;
    }

    public static boolean selectNearestToMouse(int... ids) {
        if (Game.getItemSelectionState() == 1) return false;
        RSItem item = findFirstNearestToMouse(ids);
        if (item != null) {
            if (AccurateMouse.click(item)) {
                return Timing.waitCondition(() -> {
                    General.random(100, 300);
                    return Game.getItemSelectionState() == 1;
                }, General.random(2500, 4000));
            }
        }
        return false;
    }

    public static boolean selectNearestToMouse(String... names) {
        if (Game.getItemSelectionState() == 1) return false;
        RSItem item = findFirstNearestToMouse(names);
        if (item != null) {
            if (AccurateMouse.click(item)) {
                return Timing.waitCondition(() -> {
                    General.random(100, 300);
                    return Game.getItemSelectionState() == 1;
                }, General.random(2500, 4000));
            }
        }
        return false;
    }
        
}
