package scripts.boe_api.listeners.varbit;

import lombok.Getter;
import lombok.Setter;
import org.tribot.api.General;
import org.tribot.api2007.Login;
import org.tribot.api2007.types.RSVarBit;

import java.util.concurrent.ConcurrentLinkedDeque;

public class VarBitObserver extends Thread {
    private ConcurrentLinkedDeque<VarBitListener> listeners;

    private int id;

    @Getter @Setter
    private boolean running;

    public VarBitObserver(int id) {
        this.listeners = new ConcurrentLinkedDeque<>();
        this.id = id;
        this.running = true;
    }

    @Override
    public void run() {

       int oldVarBit = RSVarBit.get(this.id).getValue();

        while (running) {
            while (Login.getLoginState() != Login.STATE.INGAME)
                General.sleep(500);

            int newVarBit = RSVarBit.get(this.id).getValue();

            if (oldVarBit != newVarBit) {
                varBitChanged(this.id, newVarBit);
            }

            oldVarBit = newVarBit;

            try {
                sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void varBitChanged(int id, int varBit) {
        for (VarBitListener listener : listeners) {
            listener.varBitChanged(id, varBit);
        }
    }


    public void addListener(VarBitListener varBitListener) {
        listeners.add(varBitListener);
    }
}
