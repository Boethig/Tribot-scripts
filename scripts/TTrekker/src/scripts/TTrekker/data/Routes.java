package scripts.TTrekker.data;

import lombok.Getter;

public enum Routes {
    EASY("Route One"),
    MEDIUM("Route Two"),
    HARD("Route 3");

    @Getter
    private String name;

    Routes(String name) {
        this.name = name;
    }
}
