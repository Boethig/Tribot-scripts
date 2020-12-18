package scripts.boe_api.framework.fluent_behavior_tree.nodes;


import scripts.boe_api.framework.fluent_behavior_tree.BehaviorTreeStatus;
import scripts.boe_api.framework.fluent_behavior_tree.IBehaviorNode;
import scripts.boe_api.framework.fluent_behavior_tree.IParentBehaviorNode;

public class InverterNode implements IParentBehaviorNode
{
    private String name = "";
    private IBehaviorNode child = null;

    public InverterNode(String name)
    {
        this.name = name;
    }

    @Override
    public BehaviorTreeStatus tick()
    {
        if(child == null)
            throw new RuntimeException("Inverter node (" + name + ") has no children");

        BehaviorTreeStatus status = child.tick();

        if(status == BehaviorTreeStatus.SUCCESS)
            return BehaviorTreeStatus.FAILURE;
        else if(status == BehaviorTreeStatus.FAILURE)
            return BehaviorTreeStatus.SUCCESS;
        else
            return status;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    @Override
    public void addChild(IBehaviorNode node)
    {
        if(child != null)
            throw new IllegalArgumentException("Cannot add more than 1 child to an Inverter Node");

        this.child = node;
    }
}
