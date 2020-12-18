package scripts.TTrekker.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.types.RSItem;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.nodes.puzzles.*;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.framework.Node;
import scripts.boe_api.framework.ParentNode;
import scripts.boe_api.inventory.OSInventory;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.walker.utils.AccurateMouse;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

import java.util.ArrayList;

public class Trekk extends ParentNode {

    public Trekk(ACamera aCamera) {
        super();
        this.aCamera = aCamera;
    }

    public boolean validate() {
        return Vars.get().escorts.findInInstance().length > 0 || Utils.isInTrekk();
    }

    @Override
    protected ArrayList<Node> setChildren() {
        return new ArrayList<Node>() {{
            add(new Route());
            add(new Combat(aCamera));
            add(new Bridge(aCamera));
            add(new River(aCamera));
            add(new Bog());
            add(new Abidor(aCamera));
        }};
    }

    @Override
    protected void sideEffects() {
        if (Antiban.get().getRunAt() > Game.getRunEnergy() && Vars.get().useStaminas) {
            this.drinkStamina();
        }
        if (NPCInteraction.isConversationWindowUp()) {
            NPCInteraction.handleConversation();
        }
    }

    public void drinkStamina() {
        final RSItem stamina = OSInventory.findFirstNearestToMouse(Constants.STAMINA_IDS);
        if (AccurateMouse.click(stamina, "Drink")) {
            Timing.waitCondition(() -> Game.getRunEnergy() > Antiban.get().getRunAt(), General.random(1000, 3000));
        }
    }
}