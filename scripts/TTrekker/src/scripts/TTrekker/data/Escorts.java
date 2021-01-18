package scripts.TTrekker.data;

import lombok.Getter;
import org.tribot.api2007.types.RSNPC;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.NpcEntity;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public enum Escorts {

    EASY(Escort.DALICAN_FANG, Escort.FYIONA_FRAY, Escort.ADVENTURER, Escort.MAGE),
    MEDIUM(Escort.JAYENE_KLIYN, Escort.VALANTAY_EPPEL, Escort.APPRENTICE, Escort.RANGER),
    HARD(Escort.ROYLAYNE_TWICKIT, Escort.SMIDDI_RYAK, Escort.FORESTER, Escort.WOMAN_AT_ARMS);

    @Getter
    private Escort[] escorts;

    Escorts(Escort ...escorts) {
        this.escorts = escorts;
    }

    public RSNPC[] find() {
        return Entities.find(NpcEntity::new)
                .or(true)
                .idEquals(Arrays.stream(escorts).flatMapToInt(escort -> IntStream.of(escort.getId())).toArray())
                .nameEquals(Arrays.stream(escorts).map(Escort::getName).toArray(String[]::new))
                .getResults();
    }

    public RSNPC findInInstance() {
        return Entities.find(NpcEntity::new)
                .or(true)
                .idEquals(Arrays.stream(escorts).flatMapToInt(escort -> IntStream.of(escort.getInstanceId())).toArray())
                .nameEquals(Arrays.stream(escorts).map(Escort::getName).toArray(String[]::new))
                .getFirstResult();
    }

    public boolean isEscortingToBurgDeRott() {
        RSNPC escort = findInInstance();
        if (escort != null) {
            Escort e = Escort.fromInstanceId(escort.getID());
            if (e != null) {
                return e.isBurgDeRottRamble();
            }
        }

        return false;
    }
}
