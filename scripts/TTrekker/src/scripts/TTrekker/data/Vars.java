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
    public boolean walked;
    public int claimCount;
    public boolean shouldRun;
    public boolean shouldPray;
    public int sleepOffset;
    public List<Integer> abc2WaitTimes;

    // GUI Variables
    public Escorts escorts;
    public Routes route;
    public boolean useStaminas;
    public boolean lootNails;

    private Vars() {
        this.status = "";
        this.subStatus = "";
        this.completed = 0;
        this.bogAttempts = 0;
        this.inTrekk = false;
        this.walked = false;
        this.claimCount = 1;
        this.shouldRun = true;
        this.shouldPray = false;
        this.useStaminas = false;
        this.escorts = Escorts.EASY;
        this.abc2WaitTimes = new ArrayList<Integer>();
        this.lootNails = false;
        this.route = Routes.EASY;
    }

    public void reset() {
        this.inTrekk = false;
        this.walked = false;
    }
}
