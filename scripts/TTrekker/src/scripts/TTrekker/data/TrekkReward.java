package scripts.TTrekker.data;

import lombok.Getter;

public enum TrekkReward {

    PURE_ESSENCE("Pure essence"),
    BOWSTRINGS("Bowstrings"),
    SILVER_BARS("Silver bars"),
    HERBS("Herbs"),
    ORES("Coal and Iron ore"),
    WATERMELON_SEEDS("Watermelon seeds"),
    RAW_LOBSTERS("Lobsters"),
    XP_TOME("XP Tome"),
    RANDOM("Random");

    @Getter
    private String name;

    TrekkReward(String name) {
        this.name = name;
    }
}
