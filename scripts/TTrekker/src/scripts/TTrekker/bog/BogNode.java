package scripts.TTrekker.bog;

import lombok.Getter;
import lombok.Setter;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

public class BogNode {

    @Getter @Setter
    private BogNode parent;
    @Getter
    private final RSObject bog;
    @Getter
    private final RSTile position;
    @Getter @Setter
    private int f;
    @Getter @Setter
    private int g;
    @Getter @Setter
    private int h;
    @Getter
    private final int x;
    @Getter
    private final int y;

    public BogNode(final RSObject obj) {
        this.bog = obj;
        this.position = obj.getPosition();
        this.x = this.position.getX();
        this.y = this.position.getY();
    }

    public void calculateHeuristic(final BogNode Node) {
        this.h = Math.abs(Node.getX() - this.x + Math.abs(Node.getY() - this.y));
    }

    public void setNodeData(final BogNode currentNode, final int cost) {
        final int gCost = currentNode.getG() + cost;
        this.parent = currentNode;
        this.g = gCost;
        this.calculateFinalCost();
    }

    public boolean checkBetterPath(final BogNode currentNode, final int cost) {
        final int gCost = currentNode.g + cost;
        if (gCost < this.g) {
            this.setNodeData(currentNode, cost);
            return true;
        }
        return false;
    }

    private void calculateFinalCost() {
        final int finalCost = this.g + this.h;
        this.f = finalCost;
    }

    public boolean equals(final BogNode node) {
        return this.position.equals(node.getPosition());
    }

    @Override
    public String toString() {
        return "Node [" + this.x + ", " + this.y + "]";
    }
}
