package scripts.boe_api.client;

import scripts.boe_api.utilities.Logger;

import javax.swing.*;
import java.awt.*;

public class TribotClient {

    private static final String CLIENT_NAME = "tribot";

    public static void minimize() {
        setFrameState(JFrame.ICONIFIED);
        Logger.log("[TribotClient]: Minimizing Tribot client");
    }

    public static void show() {
        setFrameState(JFrame.NORMAL);
        Logger.log("[TribotClient]: Opening Tribot client");
    }

    public static void maximize() {
        setFrameState(JFrame.MAXIMIZED_BOTH);
        Logger.log("[TribotClient]: Maximizing Tribot client");
    }

    private static void setFrameState(int state) {
        Frame tribot = findTribotClientFrame();
        if (tribot != null) {
            tribot.setExtendedState(state);
        }
    }

    private static Frame findTribotClientFrame() {
        Frame[] frames = JFrame.getFrames();
        for (Frame frame : frames) {
            if (frame.getTitle().toLowerCase().contains(CLIENT_NAME))
                return frame;
        }

        return null;
    }
}
