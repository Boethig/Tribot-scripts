package scripts.boe_api.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Setter;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.tribot.api.General;
import scripts.boe_api.event_dispatcher.events.ConfigureScriptCompletedEvent;
import scripts.boe_api.event_dispatcher.EventDispatcher;
import scripts.boe_api.event_dispatcher.EventListener;
import scripts.boe_api.event_dispatcher.events.ScriptEndedEvent;
import scripts.boe_api.profile_manager.BasicScriptSettings;
import scripts.boe_api.profile_manager.ProfileManager;
import scripts.boe_api.scripting.ScriptManager;
import scripts.boe_api.utilities.Logger;

import javax.swing.*;

public class WebGuiLoader extends Application implements Gui {

    private final String USER_AGENT = "\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36\"";
    private final double SHADOW_SIZE = 24;
    private Stage stage;
    private WebEngine webEngine;
    private WebView webView;
    @Setter
    private String webUrl;
    private final JavaBridge bridge = new JavaBridge();

    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        initWebView();
        BorderPane container = new BorderPane();
        if (SHADOW_SIZE > 0) {
            container.setStyle(String.format("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), %1$f, 0, 0, 0); -fx-background-insets: %1$f; -fx-padding: %1$f;", SHADOW_SIZE));
        }

        container.setCenter(webView);

        Scene scene = new Scene(container, 1100 + (SHADOW_SIZE), 600 + (SHADOW_SIZE));
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
    }

    @Override
    public boolean show() {

        EventDispatcher.get().addListener(ScriptEndedEvent.class, new EventListener<ScriptEndedEvent>((event) -> {
            close(null);
        }));

        Platform.setImplicitExit(false);

        SwingUtilities.invokeLater(() -> {
            new JFXPanel();
            Platform.runLater(() -> {
                try {
                    stage = new Stage();
                    start(stage);
                    stage.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.log("[WebGuiLoader]: %s", e.getMessage());
                }
            });
        });

        return true;
    }

    @Override
    public boolean close(String settings) {
        if (stage == null) {
            return true;
        }

        Platform.runLater(() -> {
            if (stage != null) {
                stage.close();
                Logger.log("[WebGuiLoader]: Closing Gui");
                if (webEngine != null) {
                    // This kills the javascript
                    webEngine.load(null);
                }

                stage = null;
            }
        });

        if (settings != null) {
            BasicScriptSettings s = ProfileManager.get().getSettingsFromJSON(settings, ScriptManager.getInstance().getScript().getScriptSettingsType());
            if (s != null) {
                Logger.log("[WebGuiLoader]: Successfully loaded script settings");
                EventDispatcher.get().dispatch(new ConfigureScriptCompletedEvent(s));
            } else {
                Logger.log("[WebGuiLoader]: Failed to load script settings");
            }
        }

        return true;
    }

    public void initWebView() {
        webView = new WebView();
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.setUserAgent(USER_AGENT);
        webEngine.getLoadWorker().stateProperty().addListener(new HyperLinkedRedirectListener(webEngine));
        webEngine.setUserDataDirectory(ProfileManager.get().getFolderPath().resolve("webview").toFile());
        webEngine.setPromptHandler(param -> {
            Logger.log(param.getMessage());
            return "";
        });

        webEngine.setConfirmHandler(param -> {
            Logger.log(param);
            return true;
        });

        webEngine.setOnError(event -> {
            Logger.log(event.toString());
        });

        webEngine.getLoadWorker().exceptionProperty().addListener((obs, oldExc, newExc) -> {
            if (newExc != null) {
                newExc.printStackTrace();
                Logger.log("[WebGuiLoader]: %s", newExc.getMessage());
            }
        });

        webEngine.getLoadWorker().stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                // Expose the following classes into javascript.
                try {
                    window.setMember("gui", this);
                    window.setMember("profileManager", ProfileManager.get());
                    window.setMember("java", bridge);
                } catch (JSException exception) {
                    Logger.log(exception.getMessage());
                }
                webEngine.executeScript("console.log = function(message)\n" +
                        "{\n" +
                        "    java.log(message);\n" +
                        "};");
            }
        });

        webEngine.load(webUrl);
    }

    public static class WebGuiBuilder {

        private WebGuiLoader gui = new WebGuiLoader();

        public WebGuiBuilder setURL(String url) {
            this.gui.setWebUrl(url);
            return this;
        }

        public WebGuiLoader build() {
            return this.gui;
        }
    }

    public class JavaBridge {
        public void log(String text) {
            General.println(text);
        }
    }

}
