package scripts.boe_api.duel_arena.progression;

import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterface;
import scripts.boe_api.duel_arena.models.enums.DuelInterfaces;

public class DuelResult extends DuelState {

    public String getWinner() {
        RSInterface winner = DuelInterfaces.DUEL_RESULTS_WINNER.get();
        return winner != null ? winner.getText() : null;
    }

    public String getLoser() {
        RSInterface loser = DuelInterfaces.DUEL_RESULTS_LOSER.get();
        return loser != null ? loser.getText() : null;
    }

    public boolean hasPlayerWon() {
        String winner = getWinner();
        if (winner != null) {
            return Player.getRSPlayer().getName().equals(winner);
        }
        return false;
    }

    @Override
    protected int getMaster() {
        return DuelInterfaces.DUEL_RESULTS.getMaster();
    }

    @Override
    public boolean canAccept() {
        return false;
    }

    @Override
    public boolean accept() {
        return false;
    }

    @Override
    public boolean decline() {
        RSInterface claim = DuelInterfaces.DUEL_RESULTS_CLOSE.get();
        return close(claim);
    }

    @Override
    public boolean hasPlayerAccepted() {
        return false;
    }

    @Override
    public boolean hasOpponentAccepted() {
        return false;
    }

    @Override
    public String getOpponentName() {
        return null;
    }
}
