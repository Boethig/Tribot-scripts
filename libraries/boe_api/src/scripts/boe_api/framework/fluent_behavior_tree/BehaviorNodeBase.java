package scripts.boe_api.framework.fluent_behavior_tree;

import java.util.HashMap;

public abstract class BehaviorNodeBase implements IBehaviorNode
{
    protected HashMap<String, String> sharedData;

    public BehaviorNodeBase(HashMap sharedData)
    {
        this.sharedData = sharedData;
    }
}
