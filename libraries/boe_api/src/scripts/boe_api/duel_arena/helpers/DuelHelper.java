package scripts.boe_api.duel_arena.helpers;

public class DuelHelper {

    public static int parseOffer(String offer) {
        try {
            offer = offer.replaceAll(",", "");
            if (offer.endsWith("m")) {
                return 1000000 * Integer.parseInt(offer.substring(0, offer.length() - 1));
            } else if (offer.endsWith("k")) {
                return 1000 * Integer.parseInt(offer.substring(0, offer.length() - 1));
            } else if (offer.endsWith(" gp")) {
                return Integer.parseInt(offer.substring(0, offer.length() - 3));
            } else {
                return Integer.parseInt(offer);
            }
        }
        catch (NumberFormatException exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static int getOfferInterface(int amount) {
        if (amount == 1)
            return 1;
        else if (amount == 100000)
            return 2;
        else if (amount == 1000000)
            return 3;
        else if (amount == 10000000)
            return 4;
        else
            return 5;
    }
}
