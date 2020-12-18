package scripts.boe_api.duel_arena.models.enums;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSItem;
import scripts.boe_api.duel_arena.helpers.DuelHelper;
import scripts.boe_api.entities.Entities;
import scripts.boe_api.entities.finders.prefabs.ItemEntity;
import scripts.dax_api.walker.utils.AccurateMouse;

public enum DuelCurrency {

    COINS(DuelInterfaces.DUEL_STAKE_COINS, 995),
    PLATINUM_TOKENS(DuelInterfaces.DUEL_STAKE_TOKENS, 13204);

    private DuelInterfaces container;
    private int id;

    DuelCurrency(DuelInterfaces container, int id) {
        this.container = container;
        this.id = id;
    }

    public int getAmount() {
        RSItem currency = Entities.find(ItemEntity::new).idEquals(id).getFirstResult();
        return currency != null ? currency.getStack() : 0;
    }

    public boolean offer(int amount) {
        if (DuelInterfaces.ENTER_QUANTITY.isSubstantiated())
            return enterQuantity(amount);

        int offerAmountInterface = DuelHelper.getOfferInterface(amount);

        RSInterface offer = Interfaces.get(container.getMaster(), container.getChild(), offerAmountInterface);

        if (offer != null) {
            return AccurateMouse.click(offer) && Timing.waitCondition(() -> {
                General.sleep(100,300);
                return hasOffered(amount);
            }, General.random(2000, 4000));
        }

        return false;
    }

    public static int getPlayerOffer() {
        return getOffer(DuelInterfaces.DUEL_STAKE_PLAYER_OFFERED.get());
    }

    public static int getOpponentOffer() {
        return getOffer(DuelInterfaces.DUEL_STAKE_OPPONENT_OFFERED.get());
    }

    private boolean hasOffered(int coins) {
        return getPlayerOffer() == coins;
    }

    public static int getOffer(RSInterface offer) {
        if (offer != null) {
            return DuelHelper.parseOffer(offer.getText());
        }
        return 0;
    }

    private boolean enterQuantity(int amount) {
        General.randomSD(70, 10);
        Keyboard.typeSend(getIdentifier(amount));
        return Timing.waitCondition(() -> {
            General.random(100,300);
            return hasOffered(amount);
        }, General.random(4000,6000));
    }

    private String getIdentifier(int amount) {
        try {
            if (amount % 1000000 == 0) {
                return amount / 1000000 + "m";
            } else if (amount % 1000 == 0) {
                return amount / 10000 + "k";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
