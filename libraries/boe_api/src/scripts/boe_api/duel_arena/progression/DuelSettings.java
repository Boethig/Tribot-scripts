package scripts.boe_api.duel_arena.progression;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.types.RSInterface;
import scripts.boe_api.duel_arena.models.enums.DuelInterfaces;
import scripts.boe_api.duel_arena.models.enums.DuelPreset;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.InterfaceEntity;
import scripts.dax_api.shared.helpers.InterfaceHelper;
import scripts.dax_api.walker.utils.AccurateMouse;

public class DuelSettings extends DuelState {

    public DuelSettings() {
        super();
    }

    public boolean isPresetLoaded(DuelPreset preset) {
        return preset.loaded();
    }

    public boolean loadPreset(DuelPreset preset) {
        return preset.load();
    }

    public boolean savePreset() {
        RSInterface save = DuelInterfaces.DUEL_OPTIONS_SAVE_PRESET.get();
        if (save != null) {
            return AccurateMouse.click(save) && Timing.waitCondition(() -> {
                General.sleep(100,300);
                return DuelPreset.SAVED.loaded();
                }, General.random(3000, 5000));
        }
        return false;
    }

    @Override
    public int getMaster() {
        return DuelInterfaces.DUEL_OPTIONS.getMaster();
    }

    @Override
    public boolean canAccept() {
        return Entities.find(InterfaceEntity::new)
                .inMasterAndChild(DuelInterfaces.DUEL_OPTIONS_ACCEPT.getMaster(), DuelInterfaces.DUEL_CONFIRMATION_ACCEPT.getChild())
                .textEquals("Accept")
                .getFirstResult() != null;
    }

    @Override
    public boolean accept() {
        if (hasPlayerAccepted()) return true;
        RSInterface accept = DuelInterfaces.DUEL_OPTIONS_ACCEPT.get();
        if (accept != null) {
            if (InterfaceHelper.textEquals(accept,"Wait....")) return false;
            return AccurateMouse.click(accept) && Timing.waitCondition(() -> {
                General.sleep(100,300);
                return hasPlayerAccepted();
                }, General.random(3000, 5000));
        }
        return false;
    }

    @Override
    public boolean decline() {
        RSInterface decline = DuelInterfaces.DUEL_OPTIONS_DECLINE.get();
        return close(decline);
    }

    @Override
    public boolean hasPlayerAccepted() {
        RSInterface status = DuelInterfaces.DUEL_OPTIONS_STATUS.get();
        if (status != null) {
            return InterfaceHelper.textEquals(status, "Waiting for other player...");
        }
        return false;
    }

    @Override
    public boolean hasOpponentAccepted() {
        RSInterface status = DuelInterfaces.DUEL_OPTIONS_STATUS.get();
        if (status != null) {
            return InterfaceHelper.textEquals(status, "Other player has accepted.");
        }
        return false;
    }

    public boolean hasOptionsChanged() {
        RSInterface status = DuelInterfaces.DUEL_OPTIONS_STATUS.get();
        if (status != null) {
            return InterfaceHelper.textEquals(status, "An option has changed - check before accepting!");
        }
        return false;
    }

    @Override
    public String getOpponentName() {
        RSInterface name = DuelInterfaces.DUEL_OPTIONS_OPPONENT_NAME.get();
        if (name != null) {
            return name.getText().replaceAll("(.+):","");
        }
        return "";
    }
}
