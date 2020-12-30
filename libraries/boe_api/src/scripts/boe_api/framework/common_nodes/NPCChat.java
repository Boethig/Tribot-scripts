package scripts.boe_api.framework.common_nodes;

import scripts.boe_api.framework.Node;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

public class NPCChat extends Node {
    @Override
    public boolean validate() {
        return NPCInteraction.isConversationWindowUp();
    }

    @Override
    public void execute() {
        NPCInteraction.handleConversation();
    }

    @Override
    public String status() {
        return "Talking to NPC";
    }
}
