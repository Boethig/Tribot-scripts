package scripts.boe_api.utilities;

public class Logger {

    public static void log(String string, String... args) {
        System.out.println(String.format(string,args));
    }

    public static void log(String string) {
        System.out.println(string);
    }

}
