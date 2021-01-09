package scripts.TTrekker.data;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Vars  {

    private static Vars vars;

    public static synchronized Vars get() {
        return vars == null ? vars = new Vars() : vars;
    }

    @Getter @Setter
    private TrekkScriptSettings settings = new TrekkScriptSettings();

    public String status;
    public String subStatus;
    public int completed;
    public int bogAttempts;
    public boolean inTrekk;
    public boolean hasSelectedRoute;
    public int claimCount;
    public int sleepOffset;
    public List<Integer> abc2WaitTimes;

    // GUI Variables
    public TrekkReward reward;
    public EscortSupply foodSupply;
    public int foodSupplyAmount;
    public int restorePrayerAt = 40;

    private Vars() {
        this.status = "";
        this.subStatus = "";
        this.completed = 0;
        this.bogAttempts = 0;
        this.inTrekk = false;
        this.claimCount = 1;
        this.reward = TrekkReward.BOWSTRINGS;
        this.abc2WaitTimes = new ArrayList<>();
        this.foodSupply = null;
        this.foodSupplyAmount = 0;
    }

    public void reset() {
        this.inTrekk = false;
        this.hasSelectedRoute = false;
    }
}
