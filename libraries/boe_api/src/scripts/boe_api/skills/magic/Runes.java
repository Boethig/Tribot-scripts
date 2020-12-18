package scripts.boe_api.skills.magic;

public enum Runes {
    NONE("None", false, false, false, -1, (Runes) null),
    AIR_RUNE("Air rune", false, false, false, 1, (Runes) null),
    MIND_RUNE("Mind rune", false, false, false, 1, (Runes) null),
    WATER_RUNE("Water rune", false, false, false, 5, (Runes) null),
    MIST_RUNE("Mist rune", true, true, true, 6, AIR_RUNE, WATER_RUNE),
    EARTH_RUNE("Earth rune", false, false, false, 9, (Runes) null),
    DUST_RUNE("Dust rune", true, true, true, 10, AIR_RUNE, EARTH_RUNE),
    MUD_RUNE("Mud rune", true, true, false, 13, WATER_RUNE, EARTH_RUNE),
    FIRE_RUNE("Fire rune", false, false, false, 14, (Runes) null),
    SMOKE_RUNE("Smoke rune", true, true, true, 15, AIR_RUNE, FIRE_RUNE),
    STEAM_RUNE("Steam rune", true, true, true, 19, WATER_RUNE, FIRE_RUNE),
    BODY_RUNE("Body rune", false, false, false, 20, (Runes) null),
    LAVA_RUNE("Lava rune", true, true, true, 23, EARTH_RUNE, FIRE_RUNE),
    COSMIC_RUNE("Cosmic rune", false, true, false, 27, (Runes) null),
    CHAOS_RUNE("Chaos rune", false, true, false, 35, (Runes) null),
    ASTRAL_RUNE("Astral rune", true, true, false, 40, (Runes) null),
    NATURE_RUNE("Nature rune", false, true, false, 44, (Runes) null),
    LAW_RUNE("Law rune", false, true, false, 54, (Runes) null),
    DEATH_RUNE("Death rune", false, true, false, 65, (Runes) null),
    BLOOD_RUNE("Blood rune", true, true, false, 77, (Runes) null),
    SOUL_RUNE("Soul rune", true, true, false, 90, (Runes) null),
    WRATH_RUNE("Wrath rune", true, true, false, 95, (Runes) null);

    private String runeName;
    private boolean members, membersRunecrafting, combination;
    private int runecraftingLevel;
    private Runes[] combinationRunes;

    Runes(String runeName, boolean members, boolean membersRunecrafting, boolean combination, int runecraftingLevel, Runes... combinationRunes) {
        this.runeName = runeName;
        this.members = members;
        this.membersRunecrafting = membersRunecrafting;
        this.combination = combination;
        this.runecraftingLevel = runecraftingLevel;
        this.combinationRunes = combinationRunes;
    }

    public String getRuneName() {
        return runeName;
    }

    public boolean isMembers() {
        return members;
    }

    public boolean isMembersRunecrafting() {
        return membersRunecrafting;
    }

    public boolean isCombination() {
        return combination;
    }

    public int getRunecraftingLevel() {
        return runecraftingLevel;
    }

    public Runes[] getCombinationRunes() {
        return combinationRunes;
    }

    @Override
    public String toString() {
        return getRuneName();
    }

    public Runes[] getAlternativeRunes() {
        switch (this) {
            case AIR_RUNE:
                return new Runes[]{this, DUST_RUNE, MIST_RUNE, SMOKE_RUNE};
            case EARTH_RUNE:
                return new Runes[]{this, DUST_RUNE, LAVA_RUNE, MUD_RUNE};
            case FIRE_RUNE:
                return new Runes[]{this, LAVA_RUNE, SMOKE_RUNE, STEAM_RUNE};
            case WATER_RUNE:
                return new Runes[]{this, MIST_RUNE, MUD_RUNE, STEAM_RUNE};
            default:
                return new Runes[]{this};
        }
    }
}