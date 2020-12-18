package scripts.boe_api.duel_arena.progression;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.types.RSInterface;
import scripts.boe_api.duel_arena.helpers.DuelHelper;
import scripts.boe_api.duel_arena.models.enums.DuelCurrency;
import scripts.boe_api.duel_arena.models.enums.DuelInterfaces;
import scripts.dax_api.shared.helpers.InterfaceHelper;
import scripts.dax_api.walker.utils.AccurateMouse;

public class DuelStake extends DuelState {

    public DuelStake() {
        super();
    }

    @Override
    public int getMaster() {
        return DuelInterfaces.DUEL_STAKE.getMaster();
    }

    @Override
    public boolean canAccept() {
        RSInterface accept = DuelInterfaces.DUEL_STAKE_ACCEPT.get();
        if (accept != null) {
            return accept.getText().contains("Accept") && !accept.getText().contains("Wait....");
        }
        return false;
    }

    @Override
    public boolean accept() {
        if (hasPlayerAccepted()) return false;
        RSInterface accept = DuelInterfaces.DUEL_STAKE_ACCEPT.get();
        if (accept != null) {
            if (hasOpponentAccepted()) {
                return AccurateMouse.click(accept) && Timing.waitCondition(() -> {
                    General.sleep(100,300);
                    return DuelInterfaces.DUEL_CONFIRMATION.isSubstantiated();
                }, General.random(5000, 7000));
            } else {
                return AccurateMouse.click(accept) && Timing.waitCondition(() -> {
                    General.sleep(100,300);
                    return hasPlayerAccepted();
                }, General.random(3000, 5000));
            }
        }
        return false;
    }

    @Override
    public boolean decline() {
        RSInterface decline = DuelInterfaces.DUEL_STAKE_DECLINE.get();
        return close(decline);
    }

    @Override
    public boolean hasPlayerAccepted() {
        RSInterface status = DuelInterfaces.DUEL_STAKE_STATUS.get();
        if (status != null) {
            return InterfaceHelper.textEquals(status, "Waiting for other player...");
        }
        return false;
    }

    @Override
    public boolean hasOpponentAccepted() {
        RSInterface status = DuelInterfaces.DUEL_STAKE_STATUS.get();
        if (status != null) {
            return InterfaceHelper.textEquals(status, "Other player has accepted.");
        }
        return false;
    }

    @Override
    public String getOpponentName() {
        RSInterface name = DuelInterfaces.DUEL_STAKE_OPPONENT_NAME.get();
        if (name != null) {
            return name.getText().replaceAll("(?=.+)'s stake:","");
        }
        return "";
    }

    public boolean offer(DuelCurrency currency, int amount) {
        int gp = currency.equals(DuelCurrency.PLATINUM_TOKENS) ? (amount / 1000) : amount;
        return currency.offer(gp);
    }

    public boolean offer(DuelCurrency currency, String amount) {
        return offer(currency, DuelHelper.parseOffer(amount));
    }

    public int getPlayerOffer() {
        return DuelCurrency.getPlayerOffer();
    }

    public int getOpponentOffer() {
        return DuelCurrency.getOpponentOffer();
    }
}
