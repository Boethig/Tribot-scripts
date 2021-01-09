package scripts.boe_api.profile_manager;

import com.google.gson.*;
import org.tribot.api.General;
import org.tribot.util.Util;
import scripts.boe_api.scripting.ScriptManager;
import scripts.boe_api.utilities.EnumTypeAdapter;
import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import static scripts.boe_api.utilities.ClientLogger.*;

public class ProfileManager {

    private static ProfileManager profileManager;

    private Properties properties = new Properties();

    private final String SCRIPT_PATH = "Boe13" + File.separator + ScriptManager.getInstance().getScriptName();

    private final String PROFILE_KEY = "GuiSettings";

    public static synchronized ProfileManager get() {
        return profileManager == null ? profileManager = new ProfileManager() : profileManager;
    }

    public String load(String profile) {
        File settingsLocation = new File(Util.getWorkingDirectory().getAbsolutePath(), SCRIPT_PATH + File.separator + String.format("%s_settings.ini", profile));
        try (InputStream input = new FileInputStream(settingsLocation)) {

            properties.clear();
            properties.load(input);

            info("[ProfileManager] Successfully loaded profile.");

            return properties.getProperty(PROFILE_KEY);

        } catch (IOException io) {
            io.printStackTrace();
            error("[ProfileManager] Error loading profile.");
        }

        return null;
    }

    public Profile[] getProfiles() {
        try {
            File filePath = new File(getFolder(), File.separator);
            if (filePath != null) {
                List<Profile> profiles = Arrays.stream(Objects.requireNonNull(filePath.listFiles((file) ->
                        file.isFile())))
                        .map((file -> {
                            try {
                                String profileName = file.getName().replace("_settings.ini", "");
                                return new Profile(profileName, load(profileName));
                            } catch (PatternSyntaxException exception) {
                                exception.printStackTrace();
                                error(exception.getMessage());
                                return null;
                            }
                        }))
                        .filter(profile -> profile != null)
                        .collect(Collectors.toList());

                return profiles.toArray(Profile[]::new);
            }
        } catch (Exception e) {
            General.println(e.getMessage());
        }
        return null;
    }

    public boolean save(String profile, String settings) {
        File settingsLocation = new File(Util.getWorkingDirectory().getAbsolutePath(), SCRIPT_PATH + File.separator + String.format("%s_settings.ini", profile));
        try (OutputStream output = new FileOutputStream(settingsLocation)) {
            properties.clear();
            properties.put(PROFILE_KEY, settings);
            properties.store(output, null);

            info("[ProfileManager] Successfully saved profile.");

            return true;
        } catch (IOException io) {
            io.printStackTrace();
            error("[ProfileManager] Error saving profile.");
            return false;
        }
    }

    public <T extends BasicScriptSettings> T getSettingsFromJSON(String settings, Class<T> scriptSettings) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(EnumTypeAdapter.ENUM_FACTORY)
                    .create();
            if (properties != null) {
                if (!settings.isEmpty() && settings != null) {
                    return gson.fromJson(settings, scriptSettings);
                }
            }
        } catch (JsonSyntaxException exception) {
            exception.printStackTrace();
            error("[ProfileManager] Error parsing script settings");
        }

        return null;
    }

    public File getFolder() {
        return new File(Util.getWorkingDirectory(), SCRIPT_PATH);
    }

    public Path getFolderPath() {
        return getFolder().toPath();
    }

}
