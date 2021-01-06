package scripts.boe_api.profile_manager;

import com.google.gson.*;
import org.tribot.util.Util;
import scripts.boe_api.utilities.Logger;

import java.io.*;
import java.util.Properties;

public class ProfileManager {

    private static ProfileManager profileManager;

    private Properties properties = new Properties();

    private final String SCRIPT_PATH = "Boe13" + "TTrekker";

    private final String PROFILE_KEY = "GuiSettings";

    public static synchronized ProfileManager get() {
        return profileManager == null ? profileManager = new ProfileManager() : profileManager;
    }

    public String load(String profile) {
        File settingsLocation = new File(Util.getWorkingDirectory().getAbsolutePath(), SCRIPT_PATH + profile + "settings.ini");
        try (InputStream input = new FileInputStream(settingsLocation)) {

            properties.clear();
            properties.load(input);

            Logger.log("[ProfileManager] Successfully loaded profile.");

            return properties.getProperty(PROFILE_KEY);

        } catch (IOException io) {
            io.printStackTrace();
            Logger.log("[ProfileManager] Error loading profile.");
        }

        return null;
    }

    public void save(String profile, String settings) {
        File settingsLocation = new File(Util.getWorkingDirectory().getAbsolutePath(), SCRIPT_PATH + profile + "settings.ini");
        try (OutputStream output = new FileOutputStream(settingsLocation)) {
            properties.clear();
            properties.put(PROFILE_KEY, settings);
            properties.store(output, null);

            Logger.log("[ProfileManager] Successfully saved profile.");

        } catch (IOException io) {
            io.printStackTrace();
            Logger.log("[ProfileManager] Error saving profile.");
        }
    }

    public BasicScriptSettings getSettingsFromJSON(BasicScriptSettings scriptSettings) {
        try {
            Gson gson = new GsonBuilder().create();
            if (properties != null) {
                String settings = properties.getProperty(PROFILE_KEY);
                if (!settings.isEmpty() && settings != null) {
                    return gson.fromJson(settings,BasicScriptSettings.class);
                }
            }
        } catch (JsonSyntaxException exception) {
            exception.printStackTrace();
            Logger.log("[ProfileManager] Error parsing script settings");
        }

        return null;
    }

}
