package scripts.TTrekker.nodes.puzzles;

import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSObject;
import scripts.TTrekker.bog.AStar;
import scripts.TTrekker.bog.BogHelper;
import scripts.TTrekker.bog.BogNode;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.camera.ACamera;

import java.awt.*;
import java.util.ArrayList;

public class Bog extends Puzzle {

    private ArrayList<BogNode> path;

    public Bog(ACamera aCamera) {
        super(aCamera);
    }

    public boolean validate() {
        return Objects.find(10, Constants.BOG).length > 0 && Utils.isInTrekkPuzzle();
    }

    public void solvePuzzle() {
        if (path == null || path.isEmpty()) {
            Vars.get().subStatus = "Searching for Path";
            path = findPath();
        } else {
            Vars.get().subStatus = "Walking Path";
            isPuzzleComplete = BogHelper.traverse(path);
        }
    }

    @Override
    public void resetPuzzle() {
        if (this.path != null) {
            this.path.clear();
        }
    }

    public ArrayList<BogNode> findPath() {
        ArrayList<BogNode> path = null;
        RSObject[] bog = Objects.find(10, Constants.BOG);
        if (bog.length > 0) {
            Point start = new Point(BogHelper.getMinX(bog), BogHelper.getMinY(bog));
            Point end = new Point(BogHelper.getMaxX(bog), BogHelper.getMaxY(bog));
            ArrayList<RSObject> startPositions = BogHelper.getStarting(bog);
            ArrayList<RSObject> endPositions = BogHelper.getDestinations(bog);
            for (final RSObject startPosition : startPositions) {
                for (final RSObject endPosition : endPositions) {
                    path = new AStar(bog, startPosition, endPosition, start, end).findPath();
                    if (!path.isEmpty()) {
                        Vars.get().subStatus = "A Path has been found";
                        return path;
                    }
                }
            }
        }
        return path;
    }
}
