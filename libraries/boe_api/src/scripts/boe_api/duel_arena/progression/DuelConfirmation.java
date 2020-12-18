package scripts.boe_api.duel_arena.progression;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.types.RSInterface;
import scripts.boe_api.duel_arena.models.enums.DuelInterfaces;
import scripts.dax_api.shared.helpers.InterfaceHelper;
import scripts.dax_api.walker.utils.AccurateMouse;

public class DuelConfirmation extends DuelState {

    public DuelConfirmation() {
        super();
    }

    @Override
    public int getMaster() {
        return DuelInterfaces.DUEL_CONFIRMATION.getMaster();
    }

    @Override
    public boolean canAccept() {
        return false;
    }

    @Override
    public boolean accept() {
        if (hasPlayerAccepted()) return false;
        RSInterface accept = DuelInterfaces.DUEL_CONFIRMATION_ACCEPT.get();
        if (accept != null) {
            if (InterfaceHelper.textEquals(accept,"Check....")) return false;
            if (hasOpponentAccepted()) {
                return AccurateMouse.click(accept);
            } else {
                return AccurateMouse.click(accept) && Timing.waitCondition(() -> {
                    General.sleep(100,300);
                    return hasPlayerAccepted() || InterfaceHelper.textEquals(accept,"Check....");
                }, General.random(3000, 5000));
            }
        }
        return false;
    }

    @Override
    public boolean decline() {
        RSInterface decline = DuelInterfaces.DUEL_CONFIRMATION_DECLINE.get();
        return close(decline);
    }

    @Override
    public boolean hasPlayerAccepted() {
        RSInterface status = DuelInterfaces.DUEL_CONFIRMATION_STATUS.get();
        if (status != null) {
            return InterfaceHelper.textEquals(status, "Waiting for other player...");
        }
        return false;
    }

    @Override
    public boolean hasOpponentAccepted() {
        RSInterface status = DuelInterfaces.DUEL_CONFIRMATION_STATUS.get();
        if (status != null) {
            return InterfaceHelper.textEquals(status, "Other player has accepted.");
        }
        return false;
    }

    @Override
    public String getOpponentName() {
        return null;
    }
}
