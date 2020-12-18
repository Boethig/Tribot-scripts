package scripts.TTrekker.data;

import java.util.ArrayList;
import java.util.List;

public class Vars {

    private static Vars vars;

    public static Vars get() {
        return vars == null ? vars = new Vars() : vars;
    }

    public String status;
    public String subStatus;
    public int completed;
    public double profit;
    public int bogAttempts;
    public long runTime;
    public long breakTimes;
    public boolean inTrekk;
    public boolean hasSelectedRoute;
    public boolean walked;
    public boolean talkedWithAbidor;
    public boolean bogTraversed;
    public int claimCount;
    public boolean shouldRun;
    public boolean shouldPray;
    public boolean useStaminas;
    public Escorts escorts;
    public int sleepOffset;
    public List<Integer> abc2WaitTimes;
    public boolean lootNails;

    private Vars() {
        this.status = "";
        this.subStatus = "";
        this.completed = 0;
        this.bogAttempts = 0;
        this.inTrekk = false;
        this.hasSelectedRoute = false;
        this.walked = false;
        this.talkedWithAbidor = false;
        this.bogTraversed = false;
        this.claimCount = 1;
        this.shouldRun = true;
        this.shouldPray = false;
        this.useStaminas = false;
        this.escorts = Escorts.EASY;
        this.abc2WaitTimes = new ArrayList<Integer>();
        this.lootNails = false;
    }

    public void reset() {
        this.hasSelectedRoute = false;
        this.inTrekk = false;
        this.walked = false;
        this.talkedWithAbidor = false;
        this.bogTraversed = false;
    }
}
