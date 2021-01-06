package scripts.TTrekker.combat;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Prayer;
import org.tribot.api2007.types.RSNPC;
import scripts.boe_api.camera.ACamera;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.NpcEntity;
import scripts.boe_api.utilities.Antiban;
import scripts.dax_api.walker.utils.AccurateMouse;

public class Tentacles extends CombatStrategy {

    public Tentacles(ACamera aCamera) {
        super(aCamera);
    }

    @Override
    public boolean handle() {
        return super.handle() && killHead();
    }

    @Override
    public String[] npcNames() {
        return new String[] {"tentacle"};
    }

    @Override
    public Prayer.PRAYERS useProtectionPrayer() {
        return Prayer.PRAYERS.PROTECT_FROM_MELEE;
    }

    public boolean killHead() {
        RSNPC head = Entities.find(NpcEntity::new).nameEquals("Head").actionsContains("Attack").getFirstResult();
        if (head != null) {
            if (AccurateMouse.click(head, "Attack") &&
                    Timing.waitCondition(() -> {
                        General.sleep(100,300);
                        return Combat.getTargetEntity() != null || (head.isInteractingWithMe() && head.isInCombat());
                    },General.random(3000,5000))) {
                waitForKill();
            }
        }
        return true;
    }
}
