package scripts.boe_api.framework.fluent_behavior_tree;


import scripts.boe_api.framework.fluent_behavior_tree.nodes.*;

import java.util.Stack;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class BehaviorTreeBuilder
{
    private IBehaviorNode currentNode;

    private Stack<IParentBehaviorNode> parentNodeStack = new Stack<>();

    public BehaviorTreeBuilder action(IBehaviorNode action) { return action("", action); }
    public BehaviorTreeBuilder action(String name, IBehaviorNode action)
    {
        if(parentNodeStack.size() <= 0)
            throw new RuntimeException("Actions must be created under a composite parent node (Ex. Selector, Sequence)");

        IBehaviorNode node = new ActionNode(name, action);

        parentNodeStack.peek().addChild(node);
        return this;
    }

    public BehaviorTreeBuilder perform(Runnable action) { return perform("", action); }
    public BehaviorTreeBuilder perform(String name, Runnable action)
    {
        return action(name, ()-> { action.run(); return BehaviorTreeStatus.SUCCESS; });
    }

    public BehaviorTreeBuilder condition(BooleanSupplier condition) { return condition("", condition); }
    public BehaviorTreeBuilder condition(String name, BooleanSupplier condition)
    {
        return action(name, ()-> condition.getAsBoolean() ? BehaviorTreeStatus.SUCCESS : BehaviorTreeStatus.FAILURE);
    }

    public BehaviorTreeBuilder parent(IParentBehaviorNode parentNode)
    {
        if(parentNodeStack.size() > 0)
            parentNodeStack.peek().addChild(parentNode);

        parentNodeStack.push(parentNode);
        return this;
    }

    public BehaviorTreeBuilder sequence() { return sequence(""); }
    public BehaviorTreeBuilder sequence(String name)
    {
        SequenceNode node = new SequenceNode(name);

        if(parentNodeStack.size() > 0)
            parentNodeStack.peek().addChild(node);

        parentNodeStack.push(node);
        return this;
    }

    public BehaviorTreeBuilder selector() { return selector(""); }
    public BehaviorTreeBuilder selector(String name)
    {
        SelectorNode node = new SelectorNode(name);

        if(parentNodeStack.size() > 0)
            parentNodeStack.peek().addChild(node);

        parentNodeStack.push(node);
        return this;
    }

    public BehaviorTreeBuilder inverter() { return inverter(""); }
    public BehaviorTreeBuilder inverter(String name)
    {
        InverterNode node = new InverterNode(name);

        if(parentNodeStack.size() > 0)
            parentNodeStack.peek().addChild(node);

        parentNodeStack.push(node);
        return this;
    }

    public BehaviorTreeBuilder repeater() { return repeater(""); }
    public BehaviorTreeBuilder repeater(String name)
    {
        RepeaterNode node = new RepeaterNode(name);

        if(parentNodeStack.size() > 0)
            parentNodeStack.peek().addChild(node);

        parentNodeStack.push(node);
        return this;
    }

    public BehaviorTreeBuilder repeatUntil(BehaviorTreeStatus status) { return repeatUntil("", status); }
    public BehaviorTreeBuilder repeatUntil(String name, BehaviorTreeStatus status)
    {
        RepeatUntilNode node = new RepeatUntilNode(name, status);

        if(parentNodeStack.size() > 0)
            parentNodeStack.peek().addChild(node);

        parentNodeStack.push(node);
        return this;
    }

    public BehaviorTreeBuilder repeatUntil(BooleanSupplier stopCondition, BehaviorTreeStatus status) { return repeatUntil("", stopCondition,status); }
    public BehaviorTreeBuilder repeatUntil(String name, BooleanSupplier stopCondition, BehaviorTreeStatus status)
    {
        RepeatUntilNode node = new RepeatUntilNode(name, stopCondition, status);

        if(parentNodeStack.size() > 0)
            parentNodeStack.peek().addChild(node);

        parentNodeStack.push(node);
        return this;
    }

    public BehaviorTreeBuilder succeeder() { return succeeder(""); }
    public BehaviorTreeBuilder succeeder(String name)
    {
        SucceederNode node = new SucceederNode(name);

        if(parentNodeStack.size() > 0)
            parentNodeStack.peek().addChild(node);

        parentNodeStack.push(node);
        return this;
    }

    public BehaviorTreeBuilder splice(Consumer<BehaviorTreeBuilder> behaviorTreeBuilderConsumer)
    {
        behaviorTreeBuilderConsumer.accept(this);
        return this;
    }

    public BehaviorTreeBuilder end()
    {
        currentNode = parentNodeStack.pop();
        return this;
    }

    public IBehaviorNode build()
    {
        if(currentNode == null)
            throw new RuntimeException("There are no nodes to build");

        if(parentNodeStack.size() > 1)
            System.out.println("Warning: Behavior tree is building from a child node");

        return currentNode;
    }
}
