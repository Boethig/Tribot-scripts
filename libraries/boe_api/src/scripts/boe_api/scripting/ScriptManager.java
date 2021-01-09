package scripts.boe_api.scripting;

import lombok.Getter;
import lombok.Setter;

public class ScriptManager {

    private static ScriptManager scriptManager;

    @Getter @Setter
    private BoeScript script;

    public ScriptManager(BoeScript script) {
        this.script = script;
    }

    public static ScriptManager getInstance() {
        return scriptManager;
    }

    public static ScriptManager create(BoeScript script) {
        if (scriptManager != null) {
            throw new AssertionError("Only one scriptManager can be created in the lifetime of a script.");
        }

        synchronized (ScriptManager.class) {
            if (scriptManager == null) {
                scriptManager = new ScriptManager(script);
            }
        }

        return scriptManager;
    }

    public String getScriptName() {
        return script != null ? script.manifest.name() : "";
    }
}
