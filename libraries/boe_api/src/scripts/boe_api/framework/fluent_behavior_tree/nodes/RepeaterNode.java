package scripts.boe_api.framework.fluent_behavior_tree.nodes;


import scripts.boe_api.framework.fluent_behavior_tree.BehaviorTreeStatus;
import scripts.boe_api.framework.fluent_behavior_tree.IBehaviorNode;
import scripts.boe_api.framework.fluent_behavior_tree.IParentBehaviorNode;

/**
 * Decorator Node
 * A repeater node will run its child in an indefinite loop
 */
public class RepeaterNode implements IParentBehaviorNode
{
    private String name;
    private IBehaviorNode child = null;

    public RepeaterNode(String name)
    {
        this.name = name;
    }

    @Override
    public BehaviorTreeStatus tick()
    {
        if(child == null)
            throw new RuntimeException("Repeater node (" + name + ") has no children");

        while(true)
            child.tick();
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
            throw new IllegalArgumentException("Cannot add more than 1 child to an Repeater Node");

        this.child = node;
    }
}
