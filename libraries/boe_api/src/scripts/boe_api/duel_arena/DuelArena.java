package scripts.boe_api.duel_arena;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSVarBit;
import scripts.boe_api.duel_arena.models.Arena;
import scripts.boe_api.duel_arena.models.enums.DuelInterfaces;
import scripts.boe_api.duel_arena.progression.*;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.ObjectEntity;
import scripts.boe_api.entities.finders.prefabs.PlayerEntity;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.interaction_handling.InteractionHelper;

import java.util.ArrayList;

public class DuelArena {

    private static DuelSettings settings;
    private static DuelStake stake;
    private static DuelConfirmation confirmation;
    private static DuelResult result;

    public static boolean challenge(String username) {
        RSPlayer player = Entities.find(PlayerEntity::new)
                .nameEquals(username)
                .getFirstResult();
        if (player != null) {
            if (InteractionHelper.click(player, "Challenge")) {
                return Timing.waitCondition(() -> { General.sleep(100, 300); return isDuelScreenOpen(); }, General.random(3000, 5000));
            }
        }
        return false;
    }

    public static boolean forfeit() {
        if (!isInArena()) return false;
        RSObject trapdoor = Entities.find(ObjectEntity::new).nameEquals("Trapdoor").getFirstResult();
        if (trapdoor != null) {
            if (AccurateMouse.click(trapdoor, "Forfeit")) {
                RSInterface forfeit = DuelInterfaces.FORFEIT.get();
                if (forfeit != null) {
                    return AccurateMouse.click(forfeit, "Continue")
                            && Timing.waitCondition(() -> !DuelArena.isInArena(), General.random(3000, 5000));
                }
            }
        }
        return false;
    }

    public static boolean isInArena() {
        return RSVarBit.get(8121).getValue() == 1 && (Arena.MOVEMENT.contains(Player.getPosition()) || Arena.NO_MOVEMENT.contains(Player.getPosition()));
    }

    public static boolean isInDuelProcess() {
        return isDuelScreenOpen() || isInArena();
    }

    public static DuelSettings getDuelSettings() {
        return settings != null ? settings : (settings = new DuelSettings());
    }

    public static DuelStake getDuelStake() {
        return stake != null ? stake : (stake = new DuelStake());
    }

    public static DuelConfirmation getDuelConfirmation() {
        return confirmation != null ? confirmation : (confirmation = new DuelConfirmation());
    }

    public static DuelResult getDuelResult() {
        return result != null ? result : (result = new DuelResult());
    }

    public static boolean isDuelScreenOpen() {
        return DuelInterfaces.DUEL_OPTIONS.isSubstantiated() || DuelInterfaces.DUEL_STAKE.isSubstantiated() || DuelInterfaces.DUEL_CONFIRMATION.isSubstantiated();
    }

    public static DuelState getState() {
        ArrayList<DuelState> states = new ArrayList() {
            {
                add(new DuelResult());
                add(new DuelConfirmation());
                add(new DuelStake());
                add(new DuelSettings());
            }
        };
        return states.stream().filter(DuelState::isOpen).findFirst().orElse(null);
    }
}
