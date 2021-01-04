package scripts.TTrekker.combat;

import lombok.Getter;
import scripts.boe_api.camera.ACamera;

public class CombatProvider {

    @Getter
    private CombatStrategy strategy;

    public CombatProvider(ACamera aCamera, String npcName) {
        if (npcName.contains("ghast")) {
            this.strategy = new Ghast(aCamera);
        } else if (npcName.contains("vampyre")) {
            this.strategy = new Vampyre(aCamera);
        } else if (npcName.contains("shade") || npcName.contains("shadow")) {
            this.strategy = new Shade(aCamera);
        } else if (npcName.contains("snake")) {
            this.strategy = new Snake(aCamera);
        } else if (npcName.contains("snail")) {
            this.strategy = new Snail(aCamera);
        } else if (npcName.contains("tentacle")) {
            this.strategy = new Tentacles(aCamera);
        } else if (npcName.contains("nail beast")) {
            this.strategy = new NailBeast(aCamera);
        } else if (npcName.contains("tentacle") || npcName.contains("head")) {
            this.strategy = new Tentacles(aCamera);
        }
    }

}
