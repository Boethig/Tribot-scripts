package scripts.TTrekker.nodes.puzzles;

import org.tribot.api2007.Prayer;
import org.tribot.api2007.types.RSNPC;
import scripts.TTrekker.combat.CombatProvider;
import scripts.TTrekker.combat.CombatStrategy;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Routes;
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
        return (context != null && context.getStrategy() != null) || Utils.isInTrekkCombatPuzzle();
    }

    public void solvePuzzle() {
        if (canEvadeEvent() && evadePath()) {
            resetPuzzle();
        } else {
            if (context != null) {
                CombatStrategy strategy = context.getStrategy();
                if (strategy != null) {
                    Vars.get().subStatus = strategy.getClass().getSimpleName();
                    if (strategy.handle()) {
                        this.isPuzzleComplete = true;
                        Prayer.disable(Prayer.getCurrentPrayers());
                    }
                }
            } else {
                RSNPC rsnpc = Entities.find(NpcEntity::new)
                        .idEquals(Constants.NPCS)
                        .getFirstResult();
                if (rsnpc != null) {
                    context = new CombatProvider(rsnpc.getName().toLowerCase());
                }
            }
        }
    }

    public boolean canEvadeEvent() {
        if (Vars.get().route.equals(Routes.EASY)) {
            return Vars.get().shouldEvadeCombat;
        }
        return false;
    }

    @Override
    public void resetPuzzle() {
        this.context = null;
    }
}
