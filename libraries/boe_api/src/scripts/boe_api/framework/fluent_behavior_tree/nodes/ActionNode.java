package scripts.boe_api.framework.fluent_behavior_tree.nodes;


import scripts.boe_api.framework.fluent_behavior_tree.BehaviorTreeStatus;
import scripts.boe_api.framework.fluent_behavior_tree.IBehaviorNode;

public class ActionNode implements IBehaviorNode
{
    protected String name;
    private IBehaviorNode action;

    public ActionNode(String name, IBehaviorNode action)
    {
        this.name = name;
        this.action = action;
    }

    @Override
    public BehaviorTreeStatus tick()
    {
        return action.tick();
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
