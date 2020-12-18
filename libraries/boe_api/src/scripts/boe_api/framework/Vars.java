package scripts.boe_api.framework;

public class Vars {

    private static Vars vars;

    public static Vars get() {
        return vars == null ? vars = new Vars() : vars;
    }

    public boolean shouldRun = true;

    public long timeRan;

    public String status;

}
