package scripts.boe_api.duel_arena.models.enums;

import lombok.Getter;
import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSVarBit;
import scripts.boe_api.duel_arena.DuelArena;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum DuelOption {

    NO_FORFEIT(482, 50, 0, -1),
    NO_MOVEMENT(482, 51, 1, -1),
    NO_WEAPON_SWITCH(482, 52, 2, -1),
    SHOW_INVENTORIES(482, 53, 3, -1),
    NO_RANGED(482, 54, 4, -1),
    NO_MELEE(482, 55, 5, -1),
    NO_MAGIC(482, 56, 6, -1),
    NO_DRINKS(482, 57, 7, -1),
    NO_FOOD(482, 58, 8, -1),
    NO_PRAYER(482, 59, 9, -1),
    OBSTACLES(482, 60, 10, -1),
    FUN_WEAPONS(482, 61, 12, -1),
    NO_SPECIAL_ATTACK(482, 62, 13, -1),
    HELMET(482, 80, 14, 643),
    CAPE(482, 81, 15, 644),
    NECKLACE(482, 82, 16, 645),
    WEAPON(482, 83, 17, 646),
    BODY(482, 84, 18, 647),
    SHIELD(482, 85, 19, 648),
    LEGS(482, 86, 21, 650),
    GLOVES(482, 87, 23, 652),
    BOOTS(482, 88, 24, 653),
    RING(482, 89, 26, 655),
    AMMO(482, 90, 27, 656);

    private static final int SETTING_INDEX = 286;

    @Getter
    private int parent;
    @Getter
    private int child;
    @Getter
    private final int bytemod;
    @Getter
    private final int varbit;

    DuelOption(int parent, int child, int bytemod, int varbit) {
        this.parent = parent;
        this.child = child;
        this.bytemod = bytemod;
        this.varbit = varbit;
    }

    public boolean isSelected() {
        return getVarbit() == -1 ? ((Game.getSetting(SETTING_INDEX) >> getBytemod()) & 0x1) == 1
                : RSVarBit.get(getVarbit()).getValue() == 1;
    }

    public static DuelOption[] getAllWhipping() {
        List<DuelOption> list = new ArrayList<DuelOption>();
        for (DuelOption f : values())
            if (!f.equals(NO_MELEE) && !f.equals(OBSTACLES) && !f.equals(FUN_WEAPONS) && !f.equals(WEAPON)
                    && !f.equals(NO_WEAPON_SWITCH))
                list.add(f);
        return list.toArray(new DuelOption[list.size()]);
    }

    public static DuelOption[] getAllDDSing() {
        List<DuelOption> list = new ArrayList<DuelOption>();
        for (DuelOption f : values())
            if (!f.equals(NO_MELEE) && !f.equals(OBSTACLES) && !f.equals(FUN_WEAPONS) && !f.equals(WEAPON)
                    && !f.equals(NO_SPECIAL_ATTACK) && !f.equals(NO_WEAPON_SWITCH))
                list.add(f);
        return list.toArray(new DuelOption[list.size()]);
    }

    public static DuelOption[] getAllBoxing() {
        List<DuelOption> list = new ArrayList<DuelOption>();
        for (DuelOption f : values())
            if (!f.equals(NO_MELEE) && !f.equals(OBSTACLES) && !f.equals(FUN_WEAPONS) && !f.equals(NO_WEAPON_SWITCH))
                list.add(f);
        return list.toArray(new DuelOption[list.size()]);
    }

    public static DuelOption[] getRequiredInterfacesForFix(DuelOption[] mode) {
        List<DuelOption> list = new ArrayList<DuelOption>();
        List<DuelOption> mode_list = Arrays.asList(mode);
        for (DuelOption x : values()) {
            boolean is_selected = x.isSelected();
            if (is_selected && !mode_list.contains(x))
                list.add(x);
            if (!is_selected && mode_list.contains(x))
                list.add(x);
        }
        return list.toArray(new DuelOption[list.size()]);
    }

    public static void clickAll(DuelOption[] all_for_fix, DuelOption[] mode_full,
                                boolean isFix) {
        List<DuelOption> x = Arrays.asList(mode_full);
        for (DuelOption r : all_for_fix) {
            if (!DuelArena.getDuelSettings().isOpen())
                return;
            if (DuelPreset.SAVED.loaded())
                return;
            RSInterface y = Interfaces.get(r.getParent(), r.getChild());
            if (x.contains(r) && r.isSelected())
                continue;
            if (y != null) {
                if (!isFix) {
                    if (General.random(1, 25) <= 1)
                        continue;
                }
                if (DuelPreset.SAVED.loaded())
                    return;
                y.click("");
                General.sleep(150, 300);
            }
        }
    }

}
