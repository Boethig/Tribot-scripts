package scripts.TTrekker.data;

import scripts.boe_api.profile_manager.BasicScriptSettings;

public class TrekkScriptSettings extends BasicScriptSettings {

    public boolean shouldUseStaminas;
    public boolean shouldLootBeastNails;
    public boolean shouldEvadeCombat;
    public boolean burgDeRottRamble;
    public Routes route = Routes.EASY;
    public Escorts escortDifficulty = Escorts.EASY;
    public EscortSupply escortSupply;

    public TrekkScriptSettings() {
        super();
    }
}
