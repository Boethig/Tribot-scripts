package scripts.TTrekker.data;

import lombok.Getter;

public enum Escort {
    // Temple Trekking
    DALICAN_FANG("Dalican Fang",2938,1566),
    FYIONA_FRAY("Fyiona Fray",2939,1567),
    JAYENE_KLIYN("Jayene Kliyn",2936,1),
    VALANTAY_EPPEL("Valantay Eppel",2937,1),
    ROYLAYNE_TWICKIT("Rolayne Twickit",1,1),
    SMIDDI_RYAK("Smiddi Ryak",1,1),
    // Burgh de Rott Ramble
    ADVENTURER("Adventurer",1,1),
    MAGE("Mage",1,1),
    APPRENTICE("Apprentice",1,1),
    RANGER("Ranger",1,1),
    FORESTER("Forester",1,1),
    WOMAN_AT_ARMS("Woman-at-arms",1,1);

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
