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
import scripts.boe_api.event_dispatcher.events.ConfigureScriptCompletedEvent;
import scripts.boe_api.event_dispatcher.EventDispatcher;
import scripts.boe_api.event_dispatcher.EventListener;
import scripts.boe_api.framework.Node;
import scripts.boe_api.gui.WebGuiLoader;
import scripts.boe_api.listeners.varbit.VarBitListener;
import scripts.boe_api.listeners.varbit.VarBitObserver;
import scripts.boe_api.scripting.BoeScript;
import scripts.boe_api.utilities.ScriptArguments;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

@ScriptManifest(authors = {"Boe123"}, category = "TTrekker", name = "TTrekker")
public class Main extends BoeScript implements Ending, Starting, Painting, VarBitListener, Breaking, PreBreaking, Arguments {

    private VarBitObserver varBitObserver;

    private WebGuiLoader webGui = new WebGuiLoader.WebGuiBuilder()
            .setURL("http://localhost:8080/")
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
            General.sleep(500,1000);
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