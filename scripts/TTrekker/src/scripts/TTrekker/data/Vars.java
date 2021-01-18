package scripts.TTrekker.data;

import lombok.Getter;
import lombok.Setter;
import org.tribot.api.General;

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
    public boolean hasSelectedRoute;
    public boolean isEscortDead;
    public int claimCount;
    public int sleepOffset;
    public List<Integer> abc2WaitTimes;

    // GUI Variables
    public TrekkReward reward;
    public EscortSupply foodSupply;
    public int foodSupplyAmount;

    public int restorePrayerAt = General.random(30, 50);

    private Vars() {
        this.status = "";
        this.subStatus = "";
        this.completed = 0;
        this.claimCount = 1;
        this.reward = TrekkReward.XP_TOME;
        this.abc2WaitTimes = new ArrayList<>();
        this.foodSupply = null;
        this.foodSupplyAmount = 0;
    }

    public void reset() {
        this.isEscortDead = false;
        this.hasSelectedRoute = false;
    }
}
