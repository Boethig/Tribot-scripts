package scripts.boe_api.skills.magic.spells;

import scripts.boe_api.skills.magic.Runes;

public enum CombatSpell {
    WIND_STRIKE("Wind Strike", 1, 1, 3, false, new SpellRune(Runes.MIND_RUNE, 1), new SpellRune(Runes.AIR_RUNE, 1), new SpellRune(Runes.NONE, 0)),
    WATER_STRIKE("Water Strike", 5, 2, 5, false, new SpellRune(Runes.WATER_RUNE, 1), new SpellRune(Runes.MIND_RUNE,1), new SpellRune(Runes.AIR_RUNE, 1)),
    EARTH_STRIKE("Earth Strike", 9, 3, 7, false, new SpellRune(Runes.MIND_RUNE, 1), new SpellRune(Runes.EARTH_RUNE, 2), new SpellRune(Runes.AIR_RUNE, 1)),
    FIRE_STRIKE("Fire Strike", 13, 4, 9, false, new SpellRune(Runes.MIND_RUNE, 1), new SpellRune(Runes.FIRE_RUNE, 3), new SpellRune(Runes.AIR_RUNE, 2)),
    WIND_BOLT("Wind Bolt", 17, 5, 11, false, new SpellRune(Runes.CHAOS_RUNE, 1), new SpellRune(Runes.AIR_RUNE, 2), new SpellRune(Runes.NONE, 0)),
    WATER_BOLT("Water Bolt", 23, 6, 13, false, new SpellRune(Runes.CHAOS_RUNE, 1), new SpellRune(Runes.WATER_RUNE, 2), new SpellRune(Runes.AIR_RUNE, 2)),
    EARTH_BOLT("Earth Bolt", 29, 7, 15, false, new SpellRune(Runes.CHAOS_RUNE, 1), new SpellRune(Runes.EARTH_RUNE, 3), new SpellRune(Runes.AIR_RUNE, 2)),
    FIRE_BOLT("Fire Bolt", 35, 8, 17, false, new SpellRune(Runes.CHAOS_RUNE, 1), new SpellRune(Runes.FIRE_RUNE, 4), new SpellRune(Runes.AIR_RUNE, 3)),
    WIND_BLAST("Wind Blast", 41, 9, 19, false, new SpellRune(Runes.DEATH_RUNE, 1), new SpellRune(Runes.AIR_RUNE, 3), new SpellRune(Runes.NONE, 0)),
    WATER_BLAST("Water Blast", 47, 10, 21, false, new SpellRune(Runes.DEATH_RUNE, 1), new SpellRune(Runes.WATER_RUNE, 3), new SpellRune(Runes.AIR_RUNE, 3)),
    EARTH_BLAST("Earth Blast", 53, 11, 23, false, new SpellRune(Runes.DEATH_RUNE, 1), new SpellRune(Runes.EARTH_RUNE, 4), new SpellRune(Runes.AIR_RUNE, 3)),
    FIRE_BLAST("Fire Blast", 59, 12, 25, false, new SpellRune(Runes.DEATH_RUNE, 1), new SpellRune(Runes.FIRE_RUNE, 5), new SpellRune(Runes.AIR_RUNE, 4)),
    WIND_WAVE("Wind Wave", 62, 13, 27, true, new SpellRune(Runes.BLOOD_RUNE, 1), new SpellRune(Runes.AIR_RUNE, 5), new SpellRune(Runes.NONE, 0)),
    WATER_WAVE("Water Wave", 65, 14, 29, true, new SpellRune(Runes.BLOOD_RUNE, 1), new SpellRune(Runes.WATER_RUNE, 7), new SpellRune(Runes.AIR_RUNE, 5)),
    EARTH_WAVE("Earth Wave", 70, 15, 31, true, new SpellRune(Runes.BLOOD_RUNE, 1), new SpellRune(Runes.EARTH_RUNE, 7), new SpellRune(Runes.AIR_RUNE, 5)),
    FIRE_WAVE("Fire Wave", 75, 16, 33, true, new SpellRune(Runes.BLOOD_RUNE, 1), new SpellRune(Runes.FIRE_RUNE, 7), new SpellRune(Runes.AIR_RUNE, 5)),
    WIND_SURGE("Wind Surge", 81, 17, 35, true, new SpellRune(Runes.WRATH_RUNE, 1), new SpellRune(Runes.AIR_RUNE, 5), new SpellRune(Runes.NONE, 0)),
    WATER_SURGE("Water Surge", 85, 18, 37, true, new SpellRune(Runes.WRATH_RUNE, 1), new SpellRune(Runes.WATER_RUNE, 10), new SpellRune(Runes.AIR_RUNE, 7)),
    EARTH_SURGE("Earth Surge", 90, 19, 39, true, new SpellRune(Runes.WRATH_RUNE, 1), new SpellRune(Runes.EARTH_RUNE, 10), new SpellRune(Runes.AIR_RUNE, 7)),
    FIRE_SURGE("Fire Surge", 95, 20, 41, true, new SpellRune(Runes.WRATH_RUNE, 1), new SpellRune(Runes.FIRE_RUNE, 10), new SpellRune(Runes.AIR_RUNE, 7));

    private int levelRequired, autocastComponentID, autocastSetting;
    private boolean members;
    private SpellRune[] runesRequired;
    private String spellName;

    CombatSpell(String spellName, int levelRequired, int autocastComponentID, int autocastSetting, boolean members, SpellRune... runesRequired) {
        this.spellName = spellName;
        this.autocastComponentID = autocastComponentID;
        this.autocastSetting = autocastSetting;
        this.levelRequired = levelRequired;
        this.members = members;
        this.runesRequired = runesRequired;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public int getAutocastComponentID() {
        return autocastComponentID;
    }

    public int getAutocastSetting() {
        return autocastSetting;
    }

    public boolean isMembers() {
        return members;
    }

    public SpellRune[] getRunesRequired() {
        return runesRequired;
    }

    public String getSpellName() {
        return spellName;
    }

    @Override
    public String toString() {
        return getSpellName();
    }
}