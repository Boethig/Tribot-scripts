package scripts.TTrekker.bog;

import lombok.Getter;
import lombok.Setter;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

public class AStar {

    private final int DEFAULT_HV_COST = 10;

    private final Point start;

    private final Point end;

    @Getter @Setter
    private PriorityQueue<BogNode> openList;
    @Setter
    private List<BogNode> closedList;

    private final BogNode initialNode;
    @Getter
    private final BogNode finalNode;
    @Getter
    private final List<BogNode> allNodes;

    public AStar(final RSObject[] Bog, final RSObject startObj, final RSObject endObj, final Point start, final Point end) {
        this.start = start;
        this.end = end;
        this.initialNode = new BogNode(startObj);
        this.finalNode = new BogNode(endObj);
        this.closedList = new ArrayList<>();
        this.allNodes = new ArrayList<>();
        this.openList = new PriorityQueue<>((node0, node1) -> (node0.getF() < node1.getF()) ? -1 : ((node0.getF() > node1.getF()) ? 1 : 0));
        this.initNodes(Bog);
    }

    private ArrayList<BogNode> getPath(BogNode currentNode) {
        final ArrayList<BogNode> path = new ArrayList<>();
        path.add(currentNode);
        BogNode parent;
        while ((parent = currentNode.getParent()) != null) {
            path.add(0, parent);
            currentNode = parent;
        }
        return path;
    }

    public ArrayList<BogNode> findPath() {
        this.openList.add(this.initialNode);
        while (!this.openList.isEmpty()) {
            final BogNode current = this.openList.poll();
            this.closedList.add(current);
            if (this.finalNode.equals(current)) {
                return this.getPath(current);
            }
            this.addNeighbors(current);
        }
        return new ArrayList<>();
    }

    private void initNodes(final RSObject[] nodes) {
        for (final RSObject node : nodes) {
            final BogNode n = new BogNode(node);
            n.calculateHeuristic(this.getFinalNode());
            this.allNodes.add(n);
        }
    }

    private void addNeighbors(final BogNode node) {
        final RSTile tile = node.getPosition();
        final int upperRow = tile.getY() + 1;
        if (upperRow <= this.end.getY()) {
            this.checkNode(node, tile.getX(), upperRow);
        }
        final int middleRow = tile.getY();
        if (tile.getX() - 1 >= this.start.getX()) {
            this.checkNode(node, tile.getX() - 1, middleRow);
        }
        if (tile.getX() + 1 <= this.end.getX()) {
            this.checkNode(node, tile.getX() + 1, middleRow);
        }
        final int lowerRow = tile.getY() - 1;
        if (lowerRow >= this.start.getY()) {
            this.checkNode(node, tile.getX(), lowerRow);
        }
    }

    private void checkNode(final BogNode currentNode, final int col, final int row) {
        final BogNode adjacentNode = this.getNodeAtPoint(new Point(col, row));
        if (adjacentNode != null && !this.closedList.contains(adjacentNode)) {
            if (!this.openList.contains(adjacentNode)) {
                adjacentNode.setNodeData(currentNode, DEFAULT_HV_COST);
                this.openList.add(adjacentNode);
            } else if (adjacentNode.checkBetterPath(currentNode, DEFAULT_HV_COST)) {
                this.openList.remove(adjacentNode);
                this.openList.add(adjacentNode);
            }
        }
    }

    private BogNode getNodeAtPoint(final Point point) {
        Optional<BogNode> node = this.allNodes
                .stream()
                .filter((bogNode -> bogNode.getX() == point.getX() && bogNode.getY() == point.getY()))
                .findFirst();
        return node.orElse(null);
    }
}
