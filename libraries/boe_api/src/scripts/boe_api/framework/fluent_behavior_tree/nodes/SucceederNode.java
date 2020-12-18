package scripts.boe_api.framework.fluent_behavior_tree.nodes;

import scripts.boe_api.framework.fluent_behavior_tree.BehaviorTreeStatus;
import scripts.boe_api.framework.fluent_behavior_tree.IBehaviorNode;
import scripts.boe_api.framework.fluent_behavior_tree.IParentBehaviorNode;

/**
 * Decorator Node
 * A succeeder node's tick always reports success regardless of the actual result of its child
 */
public class SucceederNode implements IParentBehaviorNode
{
    private String name;
    private IBehaviorNode child = null;

    public SucceederNode(String name)
    {
        this.name = name;
    }

    @Override
    public BehaviorTreeStatus tick()
    {
        if(child == null)
            throw new RuntimeException("Succeeder node (" + name + ") has no children");

        child.tick();

        return BehaviorTreeStatus.SUCCESS;
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
            throw new IllegalArgumentException("Cannot add more than 1 child to an Succeeder Node");

        this.child = node;
    }
}
