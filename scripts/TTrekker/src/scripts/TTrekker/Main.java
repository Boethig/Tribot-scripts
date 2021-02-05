package scripts.TTrekker;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Login;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.*;
import scripts.TTrekker.data.Constants;
import scripts.TTrekker.data.TrekkScriptSettings;
import scripts.TTrekker.data.Vars;
import scripts.TTrekker.nodes.*;
import scripts.TTrekker.utils.Utils;
import scripts.boe_api.event_dispatcher.EventDispatcher;
import scripts.boe_api.event_dispatcher.EventListener;
import scripts.boe_api.event_dispatcher.events.ConfigureScriptCompletedEvent;
import scripts.boe_api.framework.Node;
import scripts.boe_api.gui.WebGuiLoader;
import scripts.boe_api.listeners.varbit.VarBitListener;
import scripts.boe_api.listeners.varbit.VarBitObserver;
import scripts.boe_api.profile_manager.BasicScriptSettings;
import scripts.boe_api.scripting.BoeScript;
import scripts.boe_api.scripting.ScriptManager;
import scripts.boe_api.utilities.ScriptArguments;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

@ScriptManifest(authors = {"Boe123"}, category = "Minigames", name = "TTrekker")
public class Main extends BoeScript implements Ending, Starting, Painting, VarBitListener, Arguments, MessageListening07, MousePainting {

    private VarBitObserver varBitObserver;

    private WebGuiLoader webGui = new WebGuiLoader.WebGuiBuilder()
            .setURL("http://localhost:8080/")
            .build();
//    private final int bowStringPrice = GrandExchange.getPrice(Constants.BOWSTRING);

    @Override
    public void run() {
        ScriptManager.create(this);
        Camera.setRotationMethod(Camera.ROTATION_METHOD.ONLY_KEYS);
        EventDispatcher.get().addListener(ConfigureScriptCompletedEvent.class, new EventListener<ConfigureScriptCompletedEvent>((event) -> {
            TrekkScriptSettings settings = (TrekkScriptSettings) event.getSettings();
            if (settings != null) {
                Vars.get().setSettings(settings);
            }
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
            sleep(50,70);
            daxTracker.trackData("runtime", getPaint().getTimeRanS());
        }
    }

    @Override
    protected String[] scriptSpecificPaint() {
        return new String[] {
                "Status: " + Vars.get().status,
                "Trekks Completed: " + Vars.get().completed + " (" + getPaint().getEstimatedPerHour(Vars.get().completed) + ")",
                "Escort Difficulty: " + Vars.get().getSettings().escortDifficulty.toString(),
                "Route: " + Vars.get().getSettings().route.getName(),
                "Reward: " + Vars.get().getSettings().reward.getName()
        };
    }

    @Override
    public boolean isRunnable() {
        return Utils.canTempleTrekk() && (!Vars.get().getSettings().burgDeRottRamble || Utils.canBurgDeRottRamble());
    }

    @Override
    protected ArrayList<Node> nodeArrayList() {
        return new ArrayList<>() {{
            add(new Trekk(aCamera));
            add(new EscortChat());
            add(new Claim());
            add(new StartTrekk(aCamera));
            add(new SupplyEscort(aCamera));
            add(new Traveling());
            add(new SupplyRestocking());
        }};
    }

    @Override
    public Class<? extends BasicScriptSettings> getScriptSettingsType() {
        return TrekkScriptSettings.class;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.varBitObserver = new VarBitObserver(Constants.IN_TREKK);
        this.varBitObserver.addListener(this);
        this.varBitObserver.start();
    }

    @Override
    public void onEnd() {
        super.onEnd();
        this.varBitObserver.setRunning(false);
    }

    public void onPaint(Graphics g) {
        paint.paint(g);
    }

    public void varBitChanged(int newValue) {
        if (newValue == Constants.OUT_OF_INSTANCE) {
            if (!Vars.get().isEscortDead) {
                Vars.get().completed += 1;
                daxTracker.trackData("trekks", 1);
            }
            Vars.get().reset();
        }
    }


    public void passArguments(final HashMap<String, String> arguments) {
        final HashMap<String, String> commands = ScriptArguments.get((HashMap) arguments);
        if (commands.containsKey("stamina") && commands.get("stamina").contains("true")) {
            Vars.get().getSettings().shouldUseStaminas = true;
        }
    }

    @Override
    public void serverMessageReceived(String message) {
        if (message.contains("Your companion has been killed.")) {
            Vars.get().isEscortDead = true;
        }
    }

    @Override
    public void paintMouse(Graphics graphics, Point point, Point point1) {

    }
}