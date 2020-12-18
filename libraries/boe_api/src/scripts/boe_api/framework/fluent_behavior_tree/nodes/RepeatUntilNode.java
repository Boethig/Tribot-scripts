package scripts.boe_api.framework.fluent_behavior_tree.nodes;

import org.tribot.api.General;
import scripts.boe_api.framework.fluent_behavior_tree.BehaviorTreeStatus;
import scripts.boe_api.framework.fluent_behavior_tree.IBehaviorNode;
import scripts.boe_api.framework.fluent_behavior_tree.IParentBehaviorNode;

import java.util.function.BooleanSupplier;

public class RepeatUntilNode implements IParentBehaviorNode
{
    private String name;
    private BehaviorTreeStatus status;
    private BooleanSupplier stopCondition = null;
    private IBehaviorNode child = null;

    public RepeatUntilNode(String name, BooleanSupplier stopCondition, BehaviorTreeStatus status)
    {
        this.name = name;
        this.stopCondition = stopCondition;
        this.status = status;
    }
    public RepeatUntilNode(BooleanSupplier stopCondition)
    {
        this.stopCondition = stopCondition;
    }

    public RepeatUntilNode(String name, BehaviorTreeStatus status)
    {
        this.name = name;
        this.status = status;
    }
    public RepeatUntilNode(BehaviorTreeStatus status)
    {
        this.status = status;
    }

    @Override
    public BehaviorTreeStatus tick()
    {
        if(child == null)
            throw new RuntimeException("RepeatUntil node (" + name + ") has no children");

        BehaviorTreeStatus childStatus = child.tick();

        if(stopCondition == null)
        {
            while(childStatus != this.status)
            {
                childStatus = child.tick();
                General.sleep(50, 350);
            }
        }
        else
        {
            while(childStatus != this.status && !stopCondition.getAsBoolean())
            {
                childStatus = child.tick();
                General.sleep(50, 350);
            }
        }

        return childStatus;
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
            throw new IllegalArgumentException("Cannot add more than 1 child to a RepeatUntilSuccess Node");

        this.child = node;
    }
}
