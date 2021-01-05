package scripts.TTrekker;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Login;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.*;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.nodes.*;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.events.ConfigureScriptCompletedEvent;
import scripts.boe_api.events.EventDispatcher;
import scripts.boe_api.events.EventListener;
import scripts.boe_api.events.ScriptEndedEvent;
import scripts.boe_api.framework.Node;
import scripts.boe_api.gui.Controller;
import scripts.boe_api.gui.WebGuiLoader;
import scripts.boe_api.listeners.varbit.VarBitListener;
import scripts.boe_api.listeners.varbit.VarBitObserver;
import scripts.boe_api.scripting.BoeScript;
import scripts.boe_api.utilities.ScriptArguments;
import scripts.rsitem_services.GrandExchange;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

@ScriptManifest(authors = {"Boe123"}, category = "TTrekker", name = "TTrekker")
public class Main extends BoeScript implements Ending, Starting, Painting, VarBitListener, Breaking, PreBreaking, Arguments {

    private VarBitObserver varBitObserver;

    private WebGuiLoader webGui = new WebGuiLoader.WebGuiBuilder()
            .setController(new Controller() {
                @Override
                public void initialize(URL location, ResourceBundle resources) {
                }
            })
            .setURL("http://google.com")
            .build();
//    private final int bowStringPrice = GrandExchange.getPrice(Constants.BOWSTRING);

    @Override
    public void run() {
        setAntiban();
        EventDispatcher.get().addListener(ConfigureScriptCompletedEvent.class, new EventListener<ConfigureScriptCompletedEvent>((event) -> {
            setGuiLoaded(true);
        }));

        webGui.show();

        while (!isGuiLoaded()) {
            General.sleep(500, 1000);
        }

        while (isRunning) {
            if (Login.getLoginState() != Login.STATE.LOGINSCREEN) {
                for (final Node node : nodes) {
                    if (node.validate()) {
                        Vars.get().status = node.status();
                        node.execute();
                    }
                }
            }
            General.sleep(General.randomSD(50, 70, 30));
        }
    }

    @Override
    protected String[] scriptSpecificPaint() {
        return new String[] {
                "Status: " + Vars.get().status + " " + Vars.get().subStatus,
                "Trekks Completed: " + Vars.get().completed + " (" + getPaint().getEstimatedPerHour(Vars.get().completed) + ")",
                "Escort Difficulty: " + Vars.get().escorts.toString(),
                "Route: " + Vars.get().route.getName(),
                "Reward: " + Vars.get().reward.getName()
        };
    }

    @Override
    protected boolean isRunnable() {
        return Utils.canTempleTrekk() && (!Vars.get().burgDeRottRamble || Utils.canBurgDeRottRamble());
    }

    @Override
    protected ArrayList<Node> nodeArrayList() {
        return new ArrayList<>() {{
            add(new Trekk(aCamera));
            add(new StartTrekk(aCamera));
            add(new Claim());
            add(new SupplyEscort(aCamera));
            add(new Walking());
            add(new Bank());
        }};
    }

    @Override
    public void onStart() {
        super.onStart();
        this.varBitObserver = new VarBitObserver(Constants.IN_TREKK);
        this.varBitObserver.addListener(this);
        this.varBitObserver.start();
    }


    public void setAntiban() {
        Camera.setRotationMethod(Camera.ROTATION_METHOD.ONLY_KEYS);
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

//    public void drawString(final String s, final int x, final int y, final Graphics g) {
//        g.setColor(new Color(0, 0, 0, 155));
//        g.fillRect(x - 3, y - 12, 325, 17);
//        g.setColor(new Color(Math.abs((Mouse.getPos().x - 100) / 3), Math.abs((Mouse.getPos().y - 100) / 3), (int) (Camera.getCameraRotation() * 0.7), 122));
//        g.drawRect(x - 3, y - 12, 325, 17);
//        g.setColor(Color.WHITE);
//        g.drawString(s, x, y);
//    }

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
        Vars.get().status = "On break";
        Vars.get().subStatus = "";
    }

    public void passArguments(final HashMap<String, String> arguments) {
        final HashMap<String, String> commands = ScriptArguments.get((HashMap) arguments);
        if (commands.containsKey("stamina") && commands.get("stamina").contains("true")) {
            Vars.get().useStaminas = true;
        }
    }

    public void onPreBreakStart(final long l) {
    }

    @Override
    public void onEnd() {
        super.onEnd();
        this.varBitObserver.setRunning(false);
    }
}