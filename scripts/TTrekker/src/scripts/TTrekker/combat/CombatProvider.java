package scripts.TTrekker.combat;

import lombok.Getter;
import org.tribot.api2007.types.RSNPC;

public class CombatProvider {

    @Getter
    private CombatStrategy strategy;

    public CombatProvider(String npcName) {
        if (npcName.contains("ghast")) {
            this.strategy = new Ghast();
        } else if (npcName.contains("vampyre")) {
            this.strategy = new Vampyre();
        } else if (npcName.contains("shade") || npcName.contains("shadow")) {
            this.strategy = new Shade();
        } else if (npcName.contains("snake")) {
            this.strategy = new Snake();
        } else if (npcName.contains("snail")) {
            this.strategy = new Snail();
        } else if (npcName.contains("tentacle")) {
            this.strategy = new Tentacles();
        }
    }

    public boolean handleStrategy(RSNPC rsnpc) {
        return strategy.handle(rsnpc);
    }

}
