package scripts.TTrekker.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.framework.Node;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

public class EscortChat extends Node {
    @Override
    public boolean validate() {
        return !Utils.isInTrekk() && NPCInteraction.isConversationWindowUp();
    }

    @Override
    public void execute() {
        Vars.get().subStatus = "Talking";
        NPCInteraction.handleConversation();
        Timing.waitCondition(() -> Utils.isInTrekkRoute() || Utils.hasRewardsToken(), General.random(2500,3500));
    }

    @Override
    public String status() {
        return "EscortChat:";
    }
}
