package scripts.TTrekker.combat;

import lombok.Getter;

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
        } else if (npcName.contains("nail beast")) {
            this.strategy = new NailBeast();
        }
    }

}
