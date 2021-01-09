package scripts.TTrekker.nodes.puzzles;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
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

    private RSNPC[] rsnpcs;

    public Combat(final ACamera aCamera) {
        super(aCamera);
    }

    public boolean validate() {
        this.rsnpcs = Entities.find(NpcEntity::new)
                .nameContains(Constants.NPC_NAMES)
                .getResults();
        return (context != null && context.getStrategy() != null)
                || (this.rsnpcs.length > 0 && Utils.isInTrekkCombatPuzzle());
    }

    public void solvePuzzle() {
        if (canEvadeEvent()) {
            if (evadePath()) {
                resetPuzzle();
            }
        } else {
            if (context != null) {
                CombatStrategy strategy = context.getStrategy();
                General.println(strategy.getClass().getSimpleName());
                if (strategy != null) {
                    Vars.get().subStatus = strategy.getClass().getSimpleName();
                    if (strategy.handle()) {
                        this.isPuzzleComplete = true;
                        Prayer.disable(Prayer.getCurrentPrayers());
                    }
                }
            } else {
                RSNPC rsnpc = Entities.find(NpcEntity::new)
                        .nameContains(Constants.NPC_NAMES)
                        .getFirstResult();
                if (rsnpc != null) {
                    context = new CombatProvider(aCamera, rsnpc.getName().toLowerCase());
                }
            }
        }
    }

    public boolean canEvadeEvent() {
        if (!Vars.get().getSettings().shouldEvadeCombat) {
            return false;
        }
        Routes route = Vars.get().getSettings().route;
        if (route.equals(Routes.EASY)) {
            return true;
        } else if (route.equals(Routes.MEDIUM)) {
            return this.rsnpcs.length <= Vars.get().getSettings().escortDifficulty.ordinal() + 1;
        }
        return false;
    }

    @Override
    public void resetPuzzle() {
        this.context = null;
    }
}
