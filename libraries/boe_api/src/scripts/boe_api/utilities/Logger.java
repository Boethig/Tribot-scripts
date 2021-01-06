package scripts.boe_api.utilities;

import org.tribot.api.General;

public class Logger {

    public static void log(String string, String... args) {
        System.out.println(String.format(string,args));
    }

    public static void log(String string) {
        System.out.println(string);
    }

    public static void debug(String string, String... args) {
        General.println(String.format(string,args));
    }

    public static void debug(String string) {
        General.println(string);
    }
}
