package scripts.boe_api.framework.fluent_behavior_tree;

public interface IParentBehaviorNode extends IBehaviorNode
{
    void addChild(IBehaviorNode node);
}
