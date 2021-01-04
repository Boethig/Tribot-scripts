package scripts.TTrekker.data;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Constants {
    public static final int IN_TREKK = 6719;
    public static final int TREKK_PUZZLE = 3;
    public static final int IN_INSTANCE = 2;
    public static final int OUT_OF_INSTANCE = 0;
    public static final RSArea BURG;
    public static final RSArea BURG_ENT;
    public static final RSArea SALVE;
    public static final RSArea BURG_BANK;
    public static RSTile[] BURG_TO_BANK;
    public static final int ABIDOR = 1568;
    public static final int[] NPCS;
    public static final String[] NPC_NAMES;
    public static final int ZOMBIE = 5647;
    public static final int BURGGATE = 12817;
    public static final int WELL = 12897;
    public static final int SIGN = 23278;
    public static final int SIGNPOST = 13873;
    public static final int BOG = 13838;
    public static final int BACKPACK = 13872;
    public static final int[] REWARDS_TOKEN;
    public static final int BOWSTRING = 1777;
    public static final int HAMMER = 2347;
    public static final int PLANK = 960;
    public static final int AXE = 1351;
    public static final int LOGS = 1511;
    public static final int KNIFE = 946;
    public static final int VINE = 7778;
    public static final int ROPE = 7777;
    public static final int MORTON_TELEPORT = 12406;
    public static final int SALVE_GRAVEYARD_TELEPORT = 19619;
    public static final int BARROWS_TELEPORT = 19629;
    public static final int ROTTEN_FOOD = 2959;
    public static final int RING_OF_LIFE = 2570;
    public static int[] STAMINA_IDS;
    public static final String CHOOSE = "Ok, which route should we take?";
    public static final String TRAVERSED = "Whahay! That was great!";
    public static final String[] OPTIONS;
    public static final String ROPED = "You throw the vine up to the branch; it wraps around the branch.";
    public static final String SLOW_ROUTE = "You choose the slow route through Mort Myre Swamp.";
    public static final String SOFT = "Erk, this ground feels quite soft.";
    public static final String FIRM = "This ground seems quite firm.";
    public static final String MOVED = "The ground seems firm enough.";
    public static final String SUCK = "Your stick gets sucked into the bog...";
    public static final String END = "You reach your destination.";
    public static final String TALKED = "There you go... Right, I must be off now. Good luck with your travels.";
    public static final int REWARDS = 274;
    public static final int CLAIMCHILD = 12;
    public static final int IN_AID_OF_MYQYREQUE_VARBIT = 1990;
    public static final int IN_AID_OF_MYQYREQUE_COMPLETE = 430;
    public static final int DARKNESS_OF_HALLOWVALE_VARBIT = 2573;
    public static final int DARKNESS_OF_HALLOWVALE_COMPLETE = 320;
    public static final int MORT_MYRE_FUNGI = 2970;
    public static final int BLESSED_SILVER_SICKLE = 2963;
    public static final int FILLED_DRUID_POUCH = 2958;
    public static final int EMPTY_DRUID_POUCH = 2957;
    public static final int NAIL_BEAST_NAILS = 10937;
    public static final int ESCORT_FOOD_LEFT = 3433;
    public static final int SUPPLY_MASTER = 518;

    static {
        BURG = new RSArea(new RSTile(3486, 3243, 0), new RSTile(3479, 3237, 0));
        BURG_ENT = new RSArea(new RSTile(3486, 3237, 0), new RSTile(3487, 3244, 0));
        SALVE = new RSArea(new RSTile(3431, 3482, 0), new RSTile(3438, 3492, 0));
        BURG_BANK = new RSArea(new RSTile(3498, 3213, 0), new RSTile(3495, 3211, 0));
        BURG_TO_BANK = new RSTile[]{new RSTile(3497, 3211, 0), new RSTile(3497, 3211, 0), new RSTile(3497, 3218, 0), new RSTile(3491, 3225, 0), new RSTile(3484, 3229, 0), new RSTile(3480, 3236, 0), new RSTile(3479, 3237, 0)};
        NPCS = new int[]{5615, 2946, 5622, 5631, 5634, 5628};
        NPC_NAMES = new String[] {"ghast", "vampyre", "shade", "shadow", "snake"," snail", "tentacle", "nail beast", "tentacle", "head"};
        REWARDS_TOKEN = new int[]{10936, 10944, 10942};
        STAMINA_IDS = new int[]{12625, 12627, 12629, 12631};
        OPTIONS = new String[]{"I can take you to Burgh de Rott.", "I can take you to Paterdomus."};
    }
}