package scripts.TTrekker.nodes.puzzles;

import org.tribot.api2007.types.RSNPC;
import scripts.TTrekker.combat.CombatProvider;
import scripts.TTrekker.combat.Ghast;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Escorts;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.NpcEntity;

public class Combat extends Puzzle {

    private CombatProvider context;

    public Combat(final ACamera aCamera) {
        super(aCamera);
    }

    public boolean validate() {
        return Utils.isInTrekkCombatPuzzle();
    }

    public void execute() {
        Vars.get().subStatus = "Evading";
        if (Vars.get().escorts.equals(Escorts.EASY) && Vars.get().shouldEvadeCombat) {
            evadePath();
        } else {
            //TODO: medium/hard escorts
            RSNPC rsnpc = Entities.find(NpcEntity::new)
                    .idEquals(Constants.NPCS).getFirstResult();
            if (rsnpc != null) {
                context.setStrategy(new Ghast());
                context.handleStrategy(rsnpc);
            }
        }
    }
}
