package scripts.TTrekker.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.tribot.api2007.types.RSVarBit;

@Getter
@AllArgsConstructor
public enum EscortSupply {

    STEW("Stew",2003,3418),
    SALMON("Salmon",329,3417),
    BASS("Bass",365,3424),
    APPLE_PIE("Apple pie",2323,3426),
    REDBERRY_PIE("Redberry pie",2325,3425),
    CAKE("Cake",1891,3429),
    SWEETCORN("Cooked sweetcorn",5988,3431),
    POTATO_WITH_BUTTER("Potato with butter",6703, 3432),
    TUNA("Tuna",361,3421),
    LOBSTER("Lobster",379,3423),
    CHOCOLATE_CAKE("Chocolate cake",1897,3430);

    private String name;
    private int id;
    private int varbitCount;

    public int getSupplyCount() {
        return RSVarBit.get(varbitCount).getValue();
    }
}
