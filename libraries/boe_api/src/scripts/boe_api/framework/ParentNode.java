package scripts.boe_api.framework;

import java.util.ArrayList;

public abstract class ParentNode extends Node {

    protected ArrayList<Node> children;

    protected Node currentNode;

    protected abstract ArrayList<Node> setChildren();

    protected abstract void sideEffects();

    public ParentNode() {
        super();
        this.children = setChildren();
    }

    @Override
    public void execute() {
        currentNode = children.stream()
                .filter(node -> node.validate())
                .findFirst()
                .orElse(null);

        if (currentNode != null) {
            sideEffects();
            currentNode.execute();
        }
    }

    @Override
    public String status() {
        return currentNode.status();
    }
}