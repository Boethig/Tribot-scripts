package scripts.boe_api.listeners.mule;

import org.tribot.api.General;
import org.tribot.api2007.Login;
import org.tribot.api2007.Players;
import scripts.dax_api.walker.utils.TribotUtil;

import java.util.*;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

public class MuleObserver extends Thread {

    private final Set<MuleListener> listeners;
    private final Supplier<Boolean> shouldNotify;

    private final Set<String> acccountNames;

    private List<String> lastPlayers;


    public MuleObserver(Supplier<Boolean> shouldNotify) {
        this.listeners = new HashSet<>();
        this.shouldNotify = shouldNotify;
        this.acccountNames = new HashSet<>();
        this.lastPlayers = new ArrayList<>();
    }

    @Override
    public void run() {
        while (Login.getLoginState() != Login.STATE.INGAME) {
            General.sleep(500);
        }

        while (true) {
            General.sleep(200);
            if (Login.getLoginState() != Login.STATE.INGAME) continue;

            if (shouldNotify.get()) {
                List<String> nearbyPlayers = Arrays.stream(Players.find(acccountNames.toArray(new String[0])))
                        .map(TribotUtil::getName)
                        .collect(toList());

                nearbyPlayers.stream()
                        .filter(nearby -> !lastPlayers.contains(nearby))
                        .forEach(nearby -> listeners.forEach(e -> e.onMuleNearby(nearby)));

                lastPlayers.stream()
                        .filter(last -> !nearbyPlayers.contains(last))
                        .forEach(last -> listeners.forEach(e -> e.onMuleLeave(last)));

                lastPlayers = nearbyPlayers;
            }
        }
    }

    public void addListener(MuleListener muleListener) {
        listeners.add(muleListener);
    }

    public void addMule(String muleName) {
        acccountNames.add(muleName);
    }
}