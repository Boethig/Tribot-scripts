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
import scripts.boe_api.events.ConfigureScriptCompletedEvent;
import scripts.boe_api.events.EventDispatcher;
import scripts.boe_api.events.EventListener;
import scripts.boe_api.events.ScriptEndedEvent;

import javax.swing.*;

public class WebGuiLoader extends Application implements Gui {

    private Stage stage;
    private WebEngine webEngine;
    private WebView webView;

    @Setter
    private String webUrl;

    @Setter
    private Controller controller;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Web GUI");
        stage.setResizable(false);
        initWebView();
        VBox vBox = new VBox(webView);
        Scene scene = new Scene(vBox, 750, 500);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @Override
    public boolean show() {

        EventDispatcher.get().addListener(ScriptEndedEvent.class, new EventListener<ScriptEndedEvent>((event) -> {
            close();
        }));

        Platform.setImplicitExit(false);

        SwingUtilities.invokeLater(() -> {
            new JFXPanel();
            Platform.runLater(() -> {
                try {
                    stage = new Stage();
                    controller.setHostStage(stage);
                    start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        return true;
    }

    @Override
    public boolean close() {
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

        EventDispatcher.get().dispatch(new ConfigureScriptCompletedEvent());

        return true;
    }

    public void initWebView() {
        webView = new WebView();
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36");

        webEngine.getLoadWorker().exceptionProperty().addListener((obs, oldExc, newExc) -> {
            if (newExc != null) {
                newExc.printStackTrace();
            }
        });

        webEngine.getLoadWorker().stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");

                // Expose the following classes into javascript.
                window.setMember("gui", this);
            }
        });

        webEngine.load(webUrl);
    }

    public static class WebGuiBuilder {

        private WebGuiLoader gui = new WebGuiLoader();

        public WebGuiBuilder setController(Controller controller) {
            this.gui.setController(controller);
            return this;
        }

        public WebGuiBuilder setURL(String url) {
            this.gui.setWebUrl(url);
            return this;
        }

        public WebGuiLoader build() {
            return this.gui;
        }
    }

}
