package scripts.boe_api.combat;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSItem;

public class SpecialAttack {

    public static boolean activate() {
        return openTab() && setSpec();
    }

    public static int getSpecPercent() {
        return (Game.getSetting(300) / 10);
    }

    public static boolean setSpec() {
        if (specEnabled())
            return true;

        Mouse.clickBox(579, 419, 700, 428, 1);
        return Timing.waitCondition(() -> {
            General.sleep(30, 50);
            return specEnabled();
        }, General.randomSD(1000, 1125, 150));
    }

    public static boolean weildingSpecWep(int specwep) {
        for (RSItem i : Interfaces.get(387, 28).getItems()) {
            if (i.getID() == specwep)
                return true;
        }
        return false;
    }

    public static boolean openTab() {
        if (GameTab.getOpen().equals(TABS.COMBAT))
            return true;

        return GameTab.open(TABS.COMBAT);
    }

    public static boolean specEnabled() {
        return Game.getSetting(301) == 1;
    }
}
