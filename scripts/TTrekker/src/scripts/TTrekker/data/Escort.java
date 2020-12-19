package scripts.TTrekker.data;

import lombok.Getter;

public enum Escort {
    // Temple Trekking
    DALICAN_FANG("Dalican Fang",2938,1566),
    FYIONA_FRAY("Fyiona Fray",2939,1567),
    JAYENE_KLIYN("Jayene Kliyn",2936,1564),
    VALANTAY_EPPEL("Valantay Eppel",2937,1565),
    ROYLAYNE_TWICKIT("Rolayne Twickit",2935,1563),
    SMIDDI_RYAK("Smiddi Ryak",2934,1562),
    // Burgh de Rott Ramble
    ADVENTURER("Adventurer",2944,1577),
    MAGE("Mage",2945,1578),
    APPRENTICE("Apprentice",2942,1575),
    RANGER("Ranger",2943,1576),
    FORESTER("Forester",2940,1573),
    WOMAN_AT_ARMS("Woman-at-arms",2941,1574);

    @Getter
    private int id;
    @Getter
    private int instanceId;
    @Getter
    private String name;

    Escort(String name, int id, int instanceId) {
        this.name = name;
        this.id = id;
        this.instanceId = instanceId;
    }
}
