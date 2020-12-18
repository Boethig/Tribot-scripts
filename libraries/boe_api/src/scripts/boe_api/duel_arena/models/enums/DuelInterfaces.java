package scripts.boe_api.duel_arena.models.enums;

import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;

public enum DuelInterfaces {

    DUEL_OPTIONS(482),
    DUEL_OPTIONS_ACCEPT(482,106),
    DUEL_OPTIONS_DECLINE(482,105),
    DUEL_OPTIONS_STATUS(482,118),
    DUEL_OPTIONS_SAVE_PRESET(482,108),
    DUEL_OPTIONS_LOAD_PRESET(482,110),
    DUEL_OPTIONS_PRESET_LOADED(482,114),
    DUEL_OPTIONS_LOAD_PREVIOUS(482,109),
    DUEL_OPTIONS_PREVIOUS_LOADED(482,115),
    DUEL_OPTIONS_OPPONENT_NAME(482, 35),// Dueling with:
    DUEL_STAKE(481),
    DUEL_STAKE_ACCEPT(481,74),
    DUEL_STAKE_DECLINE(481, 75),
    DUEL_STAKE_STATUS(481,81),
    DUEL_STAKE_PLAYER_OFFERED(481,17),
    DUEL_STAKE_OPPONENT_OFFERED(481,27),
    DUEL_STAKE_OPPONENT_NAME(481,24), // Boethig's stake
    DUEL_STAKE_COINS(481,20),
    DUEL_STAKE_TOKENS(481,19),
    DUEL_CONFIRMATION(476),
    DUEL_CONFIRMATION_ACCEPT(476, 72),
    DUEL_CONFIRMATION_DECLINE(476, 74),
    DUEL_CONFIRMATION_STATUS(476, 51),
    DUEL_RESULTS(372),
    DUEL_RESULTS_CLOSE(372, 18),
    DUEL_RESULTS_WINNER(372, 7),
    DUEL_RESULTS_LOSER(372, 3),
    ENTER_QUANTITY(162,44),
    FORFEIT(219, 1);

    private int master, child, component;

    DuelInterfaces(int master) {
        this.master = master;
        this.child = -1;
        this.component = -1;
    }

    DuelInterfaces(int master, int child) {
        this.master = master;
        this.child = child;
        this.component = -1;
    }

    DuelInterfaces(int master, int child, int component) {
        this.master = master;
        this.child = child;
        this.component = component;
    }

    public boolean isSubstantiated() { return Interfaces.isInterfaceSubstantiated(master, child, component); }

    public RSInterface get() {
        return this.component >= 0 ? Interfaces.get(master, child, component) : this.child >= 0 ? Interfaces.get(master, child) : Interfaces.get(master);
    }

    public int getMaster() {
        return master;
    }

    public int getChild() {
        return child;
    }

    public int getComponent() {
        return component;
    }
}
