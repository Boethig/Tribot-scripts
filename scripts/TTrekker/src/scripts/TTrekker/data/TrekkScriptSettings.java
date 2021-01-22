package scripts.TTrekker.data;

import scripts.boe_api.profile_manager.BasicScriptSettings;

public class TrekkScriptSettings extends BasicScriptSettings {

    public boolean shouldUseStaminas;
    public boolean shouldLootBeastNails = true;
    public boolean shouldEvadeCombat = true;
    public boolean burgDeRottRamble = true;
    public boolean useProtectionPrayers = false;
    public Routes route = Routes.EASY;
    public Escorts escortDifficulty = Escorts.EASY;
    public EscortSupply escortSupply;
    public TrekkReward reward = TrekkReward.BOWSTRINGS;

    public TrekkScriptSettings() {
        super();
    }
}
