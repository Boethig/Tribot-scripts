package scripts.boe_api.scripting;

import lombok.Getter;
import lombok.Setter;
import org.tribot.api.General;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.event_dispatcher.EventDispatcher;
import scripts.boe_api.event_dispatcher.events.ScriptEndedEvent;
import scripts.boe_api.framework.Node;
import scripts.boe_api.paint.BoePaint;
import scripts.boe_api.paint.Paintable;
import scripts.boe_api.utilities.Antiban;
import scripts.dax.tracker.DaxTracker;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;
import scripts.dax_api.walker_engine.WalkingCondition;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public abstract class BoeScript extends Script implements Paintable, Painting, Starting, Ending, Runnable {

    public ArrayList<Node> nodes;

    public final ScriptManifest MANIFEST = this.getClass().getAnnotation(ScriptManifest.class);

    protected ACamera aCamera;

    protected DaxTracker daxTracker;

    @Getter
    public transient BoePaint paint = new BoePaint(this, Color.WHITE);

    protected abstract String[] scriptSpecificPaint();

    public abstract boolean isRunnable();

    protected abstract ArrayList<Node> nodeArrayList();

    @Getter @Setter
    protected boolean isRunning;

    @Getter @Setter
    private boolean isGuiLoaded;

    protected BoeScript() {
        aCamera = new ACamera(this);
        nodes = nodeArrayList();
        isRunning = true;
        isGuiLoaded = false;
        daxTracker = new DaxTracker("27da5657-6095-47d4-8b7d-3269af53b8db", "tvHJA1AntQ7y2dAdaz1pkA==");
    }

    @Override
    public void onStart() {
        if (isRunnable()) {
            setDaxWalker();
        }
    }

    @Override
    public void onEnd() {
        EventDispatcher.get().dispatch(new ScriptEndedEvent());
        EventDispatcher.get().destroy();
        daxTracker.stop();
    }

    private void setDaxWalker() {
        DaxWalker.setCredentials(new DaxCredentialsProvider() {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
            }
        });
        DaxWalker.setGlobalWalkingCondition(() -> {
            Antiban.get().activateRun();
            return WalkingCondition.State.CONTINUE_WALKER;
        });
    }

    @Override
    public void onPaint(final Graphics g) {
        paint.paint(g);
    }

    protected String[] basicPaint() {
        return new String[] {
                MANIFEST.name() + " v" + MANIFEST.version() + " by " + MANIFEST.authors()[0],
                "Time ran: " + paint.getTimeRan()
        };
    }

    @Override
    public String[] getPaintInfo() {
        return Stream.concat(Arrays.stream(basicPaint()), Arrays.stream(scriptSpecificPaint())).toArray(String[]::new);
    }
}
