package scripts.boe_api.duel_arena.models;

import lombok.Getter;
import lombok.Setter;
import scripts.boe_api.duel_arena.models.enums.DuelOption;

import java.util.Arrays;

public class DuelOptions {

    @Getter @Setter
    private DuelOption[] options;

    public DuelOptions(DuelOption ...options) {
        this.options = options;
    }

    public boolean areSelected() {
        return Arrays.stream(options).allMatch(duelOption -> duelOption.isSelected());
    }
}
