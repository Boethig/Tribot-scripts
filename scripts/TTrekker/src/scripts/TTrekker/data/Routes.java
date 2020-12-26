package scripts.TTrekker.data;

import lombok.Getter;

public enum Routes {

    EASY("Route One"),
    MEDIUM("Route Two"),
    HARD("Route Three");

    @Getter
    private String name;

    Routes(String name) {
        this.name = name;
    }
}
