package scripts.TTrekker.nodes.puzzles;

import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSObject;
import scripts.TTrekker.bog.AStar;
import scripts.TTrekker.bog.BogHelper;
import scripts.TTrekker.bog.BogNode;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.utils.Utils;
import scripts.dax_api.walker_engine.interaction_handling.NPCInteraction;

import java.awt.*;
import java.util.ArrayList;

public class Bog extends Puzzle {

    private ArrayList<BogNode> path;

    private boolean hasTraversed;

    public boolean validate() {
        return Objects.find(10, Constants.BOG).length > 0 && Utils.isInTrekkPuzzle();
    }

    public void execute() {
        if (NPCInteraction.isConversationWindowUp()) {
            NPCInteraction.handleConversation();
        } else if (hasTraversed) {
            Vars.get().subStatus = "Continuing Trek";
            if (continueTrek()) {
                hasTraversed = false;
                path.clear();
            }
        } else {
            Vars.get().subStatus = "Searching for Path";
            if (path == null || !path.isEmpty()) {
                path = foundPath();
            } else {
                Vars.get().subStatus = "Walking Path";
                hasTraversed = BogHelper.traverse(path);
            }
        }
    }

    public ArrayList<BogNode> foundPath() {
        ArrayList<BogNode> path = new ArrayList();
        final RSObject[] bog = Objects.find(10, Constants.BOG);
        if (bog.length > 0) {
            final Point start = new Point(BogHelper.getMinX(bog), BogHelper.getMinY(bog));
            final Point end = new Point(BogHelper.getMaxX(bog), BogHelper.getMaxY(bog));
            final ArrayList<RSObject> startObj = BogHelper.getStarting(bog);
            final ArrayList<RSObject> destObj = BogHelper.getDestinations(bog);
            for (final RSObject s : startObj) {
                for (final RSObject d : destObj) {
                    path = new AStar(bog, s, d, start, end).findPath();
                    if (!path.isEmpty()) {
                        Vars.get().subStatus = "A Path has been found";
                        return path;
                    }
                }
            }
        }
        return path;
    }

    public String status() {
        return super.status();
    }
}
