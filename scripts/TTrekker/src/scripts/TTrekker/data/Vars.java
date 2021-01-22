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
    public int completed;
    public boolean hasSelectedRoute;
    public boolean isEscortDead;
    public Escort currentEscort;
    public int claimCount;
    public int sleepOffset;
    public List<Integer> abc2WaitTimes;

    // GUI Variables
    public EscortSupply foodSupply;
    public int foodSupplyAmount;

    public int restorePrayerAt = General.random(30, 50);

    private Vars() {
        this.status = "";
        this.completed = 0;
        this.claimCount = 1;
        this.abc2WaitTimes = new ArrayList<>();
        this.foodSupply = null;
        this.foodSupplyAmount = 0;
    }

    public void reset() {
        this.currentEscort = null;
        this.isEscortDead = false;
        this.hasSelectedRoute = false;
    }
}
