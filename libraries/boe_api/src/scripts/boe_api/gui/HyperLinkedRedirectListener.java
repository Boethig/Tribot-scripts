package scripts.boe_api.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import lombok.AllArgsConstructor;
import netscape.javascript.JSObject;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@AllArgsConstructor
public class HyperLinkedRedirectListener implements ChangeListener<Worker.State> {

    private WebEngine webEngine;

    @Override
    public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
        if (newValue == Worker.State.SUCCEEDED) {
            JSObject win = (JSObject) webEngine.executeScript("window");
            win.setMember("hyperlink", this);
        }
    }

    public void open(String url) {
        String hyperlink = url.trim();
        if (hyperlink.matches("http?s://.*")) {
            webEngine.getLoadWorker().cancel();
            try {
                Desktop.getDesktop().browse(new URL(hyperlink).toURI());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (URISyntaxException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
