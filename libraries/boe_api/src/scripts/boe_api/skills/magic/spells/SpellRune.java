package scripts.boe_api.skills.magic.spells;

import scripts.boe_api.skills.magic.Runes;

public class SpellRune {

    private Runes rune;
    private int runeCount;

    public SpellRune(Runes rune, int runeCount) {
        this.rune = rune;
        this.runeCount = runeCount;
    }

    public int getRuneCount() {
        return runeCount;
    }

    public Runes getRune() {
        return rune;
    }
}

