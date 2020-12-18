package scripts.boe_api.framework.fluent_behavior_tree.nodes;

import org.tribot.api.General;
import scripts.boe_api.framework.fluent_behavior_tree.BehaviorTreeStatus;
import scripts.boe_api.framework.fluent_behavior_tree.IBehaviorNode;
import scripts.boe_api.framework.fluent_behavior_tree.IParentBehaviorNode;

import java.util.ArrayList;

/**
 * Composite Node
 * A selector node will run its children until 1 succeeds. If a child succeeds, the selector will report success. If all the children run and fail, the selector will report failure.
 */
public class SelectorNode implements IParentBehaviorNode
{
    private String name;

    private ArrayList<IBehaviorNode> children = new ArrayList<>();


    public SelectorNode(String name)
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

            if(status != BehaviorTreeStatus.FAILURE)
                return status;
        }

        return BehaviorTreeStatus.FAILURE;
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
