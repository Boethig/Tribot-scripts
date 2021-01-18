package scripts.TTrekker.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Escort {
    // Temple Trekking
    DALICAN_FANG("Dalican Fang",2938,1566, false),
    FYIONA_FRAY("Fyiona Fray",2939,1567, false),
    JAYENE_KLIYN("Jayene Kliyn",2936,1564, false),
    VALANTAY_EPPEL("Valantay Eppel",2937,1565, false),
    ROYLAYNE_TWICKIT("Rolayne Twickit",2935,1563, false),
    SMIDDI_RYAK("Smiddi Ryak",2934,1562, false),
    // Burgh de Rott Ramble
    ADVENTURER("Adventurer",2944,1577, true),
    MAGE("Mage",2945,1578,true),
    APPRENTICE("Apprentice",2942,1575, true),
    RANGER("Ranger",2943,1576, true),
    FORESTER("Forester",2940,1573,true),
    WOMAN_AT_ARMS("Woman-at-arms",2941,1574, true);

    private String name;
    private int id;
    private int instanceId;
    private boolean burgDeRottRamble;

    public static Escort fromInstanceId(int id) {
        for (Escort escort : Escort.values()) {
            if (escort.instanceId == id) {
                return escort;
            }
        }

        return null;
    }
}
