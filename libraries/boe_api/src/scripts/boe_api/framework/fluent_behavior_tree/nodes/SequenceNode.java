package scripts.boe_api.framework.fluent_behavior_tree.nodes;

import org.tribot.api.General;
import scripts.boe_api.framework.fluent_behavior_tree.BehaviorTreeStatus;
import scripts.boe_api.framework.fluent_behavior_tree.IBehaviorNode;
import scripts.boe_api.framework.fluent_behavior_tree.IParentBehaviorNode;

import java.util.ArrayList;

/**
 * Composite Node
 * A sequence node will run its children until 1 fails. If a child fails, the sequence will report failure. If all the nodes run and succeed, the sequence will report success.
 */
public class SequenceNode implements IParentBehaviorNode
{
    private String name;

    private ArrayList<IBehaviorNode> children = new ArrayList<>();

    public SequenceNode(String name)
    {
        this.name = name;
    }

    @Override
    public BehaviorTreeStatus tick()
    {
        for(IBehaviorNode node : children)
        {
            BehaviorTreeStatus status;
            do
            {
                status = node.tick();

                if(status == BehaviorTreeStatus.RUNNING)
                    General.sleep(50,250);
            }
            while(status == BehaviorTreeStatus.RUNNING);

            if(status != BehaviorTreeStatus.SUCCESS)
                return status;
        }

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
        children.add(node);
    }
}
