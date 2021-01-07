package scripts.boe_api.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.Setter;
import netscape.javascript.JSObject;
import scripts.boe_api.event_dispatcher.events.ConfigureScriptCompletedEvent;
import scripts.boe_api.event_dispatcher.EventDispatcher;
import scripts.boe_api.event_dispatcher.EventListener;
import scripts.boe_api.event_dispatcher.events.ScriptEndedEvent;
import scripts.boe_api.profile_manager.BasicScriptSettings;
import scripts.boe_api.profile_manager.ProfileManager;
import scripts.boe_api.utilities.Logger;

import javax.swing.*;

public class WebGuiLoader extends Application implements Gui {

    private final String USER_AGENT = "\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36\"";
    private Stage stage;
    private WebEngine webEngine;
    private WebView webView;
    @Setter
    private String webUrl;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Web GUI");
        stage.setResizable(false);
        initWebView();
        VBox vBox = new VBox(webView);
        Scene scene = new Scene(vBox, 700, 500);
        stage.setScene(scene);
        stage.showAndWait();
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

        Logger.log("[WebGuiLoader]: Closing Gui");

        if (stage == null) {
            return true;
        }

        Platform.runLater(() -> {
            if (stage != null) {
                stage.close();

                if (webEngine != null) {
                    // This kills the javascript
                    webEngine.load(null);
                }

                stage = null;
            }
        });

        if (settings != null) {
            //TODO: insert script settings here
            BasicScriptSettings s = ProfileManager.get().getSettingsFromJSON(new BasicScriptSettings());
            EventDispatcher.get().dispatch(new ConfigureScriptCompletedEvent(s));
        }

        return true;
    }

    public void initWebView() {
        webView = new WebView();
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.setUserAgent(USER_AGENT);

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
                window.setMember("gui", this);
                window.setMember("profileManager", ProfileManager.get());
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

}
