package scripts.boe_api.duel_arena.models.enums;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.types.RSInterface;
import scripts.dax_api.walker.utils.AccurateMouse;

public enum DuelPreset {

    LAST_DUEL(DuelInterfaces.DUEL_OPTIONS_LOAD_PREVIOUS, DuelInterfaces.DUEL_OPTIONS_PREVIOUS_LOADED),
    SAVED(DuelInterfaces.DUEL_OPTIONS_LOAD_PRESET, DuelInterfaces.DUEL_OPTIONS_PRESET_LOADED);

    private DuelInterfaces loadInterface;
    private DuelInterfaces isLoadedInterface;

    DuelPreset(DuelInterfaces loadInterface, DuelInterfaces isLoadedInterface) {
        this.loadInterface = loadInterface;
        this.isLoadedInterface = isLoadedInterface;
    }

    public boolean loaded() {
        RSInterface loaded = isLoadedInterface.get();
        if (loaded != null) {
            return !loaded.isHidden();
        }
        return false;
    }

    public boolean load() {
        RSInterface loadPreset = loadInterface.get();
        if (loadPreset != null) {
            if (AccurateMouse.click(loadPreset)) {
                return Timing.waitCondition(() ->  {
                    General.sleep(100,300);
                    return loaded();
                }, General.random(3000, 5000));
            }
        }
        return false;
    }
}
