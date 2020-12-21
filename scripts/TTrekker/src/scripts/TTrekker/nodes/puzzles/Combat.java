package scripts.TTrekker.nodes.puzzles;

import scripts.TTrekker.data.Escorts;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;

public class Combat extends Puzzle {

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
        }
    }
}
