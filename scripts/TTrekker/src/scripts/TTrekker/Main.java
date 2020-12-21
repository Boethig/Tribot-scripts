package scripts.TTrekker;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Login;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Breaking;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.PreBreaking;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.nodes.*;
import scripts.TTrekker.nodes.puzzles.Combat;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.framework.Node;
import scripts.boe_api.listeners.varbit.VarBitListener;
import scripts.boe_api.listeners.varbit.VarBitObserver;
import scripts.boe_api.scripting.BoeScript;
import scripts.rsitem_services.GrandExchange;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;

@ScriptManifest(authors = {"Boe123"}, category = "TTrekker", name = "TTrekker")
public class Main extends BoeScript implements Painting, VarBitListener, Breaking, PreBreaking, Arguments {

//    private final int bowStringPrice = GrandExchange.getPrice(Constants.BOWSTRING);

    @Override
    public void run() {
        Collections.addAll(this.nodes, new Trekk(this.aCamera), new StartTrekk(this.aCamera), new Claim(), new Walking(), new Bank());
        this.setAntiban();
        while (Vars.get().shouldRun) {
            if (Login.getLoginState() != Login.STATE.LOGINSCREEN) {
                for (final Node node : this.nodes) {
                    if (node.validate()) {
                        Vars.get().status = node.status();
                        node.execute();
                    }
                }
            }
            General.sleep(General.randomSD(50, 70, 5));
        }
    }

    @Override
    protected String[] scriptSpecificPaint() {
        return new String[]{ "Status: " + Vars.get().status + " " + Vars.get().subStatus };
    }

    @Override
    public void onStart() {
        super.onStart();
        Vars.get().runTime = System.currentTimeMillis();
        Vars.get().status = "Initializing Script.....";
        final VarBitObserver varBitObserver = new VarBitObserver(Constants.IN_TREKK);
        varBitObserver.addListener(this);
        varBitObserver.start();
    }

    public void onEnd() { }

    public void setAntiban() {
        Mouse.setSpeed(General.random(100, 110));
        Vars.get().abc2WaitTimes.add(General.random(105, 350));
    }

    public void onPaint(Graphics g) {
        super.onPaint(g);
    }

//    public void onPaint(final Graphics gg) {
//        final Graphics2D g = (Graphics2D) gg;
//        final long timeRan = System.currentTimeMillis() - Vars.get().runTime - Vars.get().breakTimes;
//        Vars.get().profit = Vars.get().completed * 101 * this.bowStringPrice;
//        final int hourlyProfit = (int) (Vars.get().profit / 1000.0 * 3600000.0 / timeRan);
//        final int hourlyTrekks = (int) (Vars.get().completed * 3600000.0 / timeRan);
//        this.drawString("Runtime: " + Timing.msToString(timeRan), 195, 275, g);
//        this.drawString("Trekks Completed : " + Vars.get().completed + " (" + hourlyTrekks + ")", 195, 292, g);
//        this.drawString("Profit: " + hourlyProfit + "k / h", 195, 309, g);
//        this.drawString("Status: " + Vars.get().status + " " + Vars.get().subStatus, 195, 326, g);
//    }

    public void drawString(final String s, final int x, final int y, final Graphics g) {
        g.setColor(new Color(0, 0, 0, 155));
        g.fillRect(x - 3, y - 12, 325, 17);
        g.setColor(new Color(Math.abs((Mouse.getPos().x - 100) / 3), Math.abs((Mouse.getPos().y - 100) / 3), (int) (Camera.getCameraRotation() * 0.7), 122));
        g.drawRect(x - 3, y - 12, 325, 17);
        g.setColor(Color.WHITE);
        g.drawString(s, x, y);
    }

    public void varBitChanged(final int varBit, final int newValue) {
        if (newValue == 2) {
            Vars.get().inTrekk = true;
        } else if (newValue == 0) {
            Vars.get().reset();
            Vars.get().completed++;
        }
    }

    public void onBreakEnd() { }

    public void onBreakStart(final long l) {
        final Vars value = Vars.get();
        value.breakTimes += l;
        Vars.get().status = "On break";
        Vars.get().subStatus = "";
    }

    public void passArguments(final HashMap<String, String> arguments) {
        final HashMap<String, String> commands = scripts.boe_api.utilities.Arguments.get((HashMap) arguments);
        if (commands.containsKey("stamina") && commands.get("stamina").contains("true")) {
            Vars.get().useStaminas = true;
        }
    }

    public void onPreBreakStart(final long l) {
        final Node combat = new Combat(this.aCamera);
        while (combat.validate()) {
            combat.execute();
            General.sleep(100L);
        }
    }
}