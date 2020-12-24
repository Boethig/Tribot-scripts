package scripts.TTrekker.combat;

import org.tribot.api2007.types.RSNPC;

public interface CombatStrategy {
    public boolean handle(RSNPC rsnpc);
}
