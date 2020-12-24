package scripts.TTrekker.combat;

import lombok.Getter;
import lombok.Setter;
import org.tribot.api2007.types.RSNPC;

public class CombatProvider {

    @Getter @Setter
    private CombatStrategy strategy;

    public CombatProvider(CombatStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean handleStrategy(RSNPC rsnpc) {
        return strategy.handle(rsnpc);
    }
}
