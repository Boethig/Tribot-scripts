package scripts.Debug;

import org.tribot.api.General;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.MouseActions;

import java.awt.*;
import java.util.ArrayList;

public class Debug extends Script implements MouseActions, Ending {

    ArrayList<Double> x = new ArrayList<>();
    ArrayList<Double> y = new ArrayList<>();

    @Override
    public void run() {
        while (true) {
            sleep(100);
        }
    }

    public static RSTile minimapToTile(Point p) {
        if (!Projection.isInMinimap(p)) return null;
        RSTile pos = Player.getPosition();
        for (int x = pos.getX() - 20; x < pos.getX() + 20; x++) {
            for (int y = pos.getY() - 20; y < pos.getY() + 20; y++) {
                RSTile tile = new RSTile(x, y, pos.getPlane());
                Point t = Projection.tileToMinimap(tile);
                if (Math.abs(t.x - p.x) <= 2 && Math.abs(t.y - p.y) <= 2) return tile;
            }
        }
        return null;
    }

    public static double calculateSD(double numArray[])
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }

    @Override
    public void mouseMoved(Point point, boolean b) {

    }

    @Override
    public void mouseReleased(Point point, int i, boolean b) {

    }

    @Override
    public void mouseClicked(Point point, int i, boolean b) {
        RSTile tile = minimapToTile(point);
        if (tile != null) {
            x.add((double) tile.getX() - Player.getPosition().getX());
            y.add((double) tile.getY() - Player.getPosition().getY());
        }
    }

    @Override
    public void mouseDragged(Point point, int i, boolean b) {

    }

    @Override
    public void onEnd() {
        General.println(String.format("Total clicks recorded: %d", x.size()));
        General.println(String.format("max x: %.1f", x.stream().mapToDouble(d -> d).max().getAsDouble()));
        General.println(String.format("min x:  %.1f", x.stream().mapToDouble(d -> d).min().getAsDouble()));
        General.println(String.format("x standard deviation %.2f", calculateSD(x.stream().mapToDouble(d -> d).toArray())));

        General.println(String.format("max y:  %.1f", y.stream().mapToDouble(d -> d).max().getAsDouble()));
        General.println(String.format("min y:  %.1f", y.stream().mapToDouble(d -> d).min().getAsDouble()));
        General.println(String.format("y standard deviation %.2f", calculateSD(y.stream().mapToDouble(d -> d).toArray())));
    }
}
