package scripts.boe_api.skills.magic;

import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceComponent;
import scripts.dax_api.walker.utils.AccurateMouse;

public enum Autocast {

    AIR_STRIKE(1, 3),
    WATER_STRIKE(2, 5),
    EARTH_STRIKE(3, 7),
    FIRE_STRIKE(4, 9),
    WIND_BOLT(5, 11),
    WATER_BOLT(6, 13),
    EARTH_BOLT(7, 15),
    FIRE_BOLT(8, 17),
    WIND_BLAST(9, 19),
    WATER_BLAST(10, 21),
    EARTH_BLAST(11, 23),
    FIRE_BLAST(12, 25),
    WIND_WAVE(13, 27),
    WATER_WAVE(14, 29),
    EARTH_WAVE(15, 31),
    FIRE_WAVE(16, 33),
    WIND_SURGE(17, 35),
    WATER_SURGE(18, 37),
    EARTH_SURGE(19, 39),
    FIRE_SURGE(20, 41),
    // ANCIENT MAGICKS
    SMOKE_RUSH(31, 63),
    SHADOW_RUSH(32, 65),
    BLOOD_RUSH(33, 67),
    ICE_RUSH(34, 69),
    SMOKE_BURST(35, 71),
    SHADOW_BURST(36, 73),
    BLOOD_BURST(37, 75),
    ICE_BURST(38, 77),
    SMOKE_BLITZ(39, 79),
    SHADOW_BLITZ(40, 81),
    BLOOD_BLITZ(41, 83),
    ICE_BLITZ(42, 85),
    SMOKE_BARRAGE(43, 87),
    SHADOW_BARRAGE(44, 89),
    BLOOD_BARRAGE(45, 91),
    ICE_BARRAGE(46, 93);

    private int componentID;
    private int enabledSetting;

    Autocast(int componentID, int enabledSetting) {
        this.componentID = componentID;
        this.enabledSetting = enabledSetting;
    }

    public static boolean isAutocastEnabled(Autocast spellName) {
        int autocastSetting = Game.getSetting(108);
        return spellName.enabledSetting == autocastSetting;
    }

    public static void enableAutocast(Autocast spellName, boolean defensive) {
        RSInterfaceChild autocastSelection = Interfaces.get(201, 1);
        RSInterfaceChild autocastSelectionBox = Interfaces.get(593, defensive == true ? 21 : 26);

        if (autocastSelection != null && !autocastSelection.isHidden()) {
            RSInterfaceComponent spellToSelect = autocastSelection.getChild(spellName.componentID);
            if (spellToSelect != null) {
                if (AccurateMouse.click(spellToSelect, "Choose spell"))
                    Timing.waitCondition(() -> Game.getSetting(108) == spellName.enabledSetting, 2000);
            }
        } else {
            if (GameTab.open(GameTab.TABS.COMBAT) && autocastSelectionBox != null) {
                if (autocastSelectionBox.click())
                    Timing.waitCondition(() -> Interfaces.get(201, 1) != null, 2000);
            }
        }
    }
}