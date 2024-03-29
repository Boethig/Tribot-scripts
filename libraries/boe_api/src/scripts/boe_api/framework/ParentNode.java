package scripts.boe_api.framework;

import org.tribot.api.General;
import scripts.boe_api.camera.ACamera;

import java.util.ArrayList;

public abstract class ParentNode extends Node {

    protected ArrayList<Node> children;

    protected Node currentNode;

    protected String status;

    protected abstract ArrayList<Node> setChildren();

    protected abstract void sideEffects();

    public ParentNode() {
        super();
        this.children = setChildren();
    }

    public ParentNode(ACamera aCamera) {
        super(aCamera);
        this.children = setChildren();
    }

    @Override
    public void execute() {
        if (currentNode != null) {
            sideEffects();
            status = currentNode.status();
            currentNode.execute();
        }
    }

    @Override
    public String status() {

        currentNode = children.stream()
            .filter(Node::validate)
            .findFirst()
            .orElse(null);

        return currentNode != null ? currentNode.status() : "";
    }
}
