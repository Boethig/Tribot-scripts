package scripts.TTrekker.data;

import scripts.boe_api.profile_manager.BasicScriptSettings;

public class TrekkScriptSettings extends BasicScriptSettings {

    public boolean shouldUseStaminas;
    public boolean shouldLootBeastNails = true;
    public boolean shouldEvadeCombat = false;
    public boolean burgDeRottRamble;
    public boolean useProtectionPrayers = true;
    public Routes route = Routes.HARD;
    public Escorts escortDifficulty = Escorts.EASY;
    public EscortSupply escortSupply;
    public TrekkReward reward = TrekkReward.BOWSTRINGS;

    public TrekkScriptSettings() {
        super();
    }
}
