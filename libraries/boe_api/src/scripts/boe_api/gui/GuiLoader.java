package scripts.boe_api.gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.FileUtils;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.util.Util;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;

/**
 *
 */
public class GuiLoader implements Gui
{
    private String fxmlString,
            frameTitle = "";

    private URL styleSheetUrl = null;

    private Controller controller;
    private Stage parent;

    private boolean isDecorated = false, isModal = false;

    @Override
    public boolean show()
    {
        SwingUtilities.invokeLater(() ->
        {
            new JFXPanel();
            Platform.runLater(() ->
            {
                new JFXPanel();
                try
                {
                    Stage stage = new Stage();

                    if(!isDecorated)
                        stage.initStyle(StageStyle.UNDECORATED);

                    stage.setTitle(frameTitle);
                    stage.setResizable(false);

                    if(isModal)
                    {
                        stage.initOwner(parent);
                        stage.initModality(Modality.APPLICATION_MODAL);
                    }

                    controller.setHostStage(stage);
                    Platform.setImplicitExit(false);

                    FXMLLoader loader = new FXMLLoader();
                    loader.setController(controller);
                    Scene scene = new Scene(loader.load(new ByteArrayInputStream(fxmlString.getBytes())));

                    if(styleSheetUrl != null)
                        scene.getStylesheets().add(styleSheetUrl.toExternalForm());

                    stage.setScene(scene);
                    stage.showAndWait();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        });

        if(!isModal)
        {
            while (!controller.isDone())
                General.sleep(500, 1000);
        }

        return !controller.isCancelled();
    }

    //region Getters and Setters

    public String getFxmlString()
    {
        return fxmlString;
    }

    public void setFxmlString(String fxmlString)
    {
        this.fxmlString = fxmlString;
    }

    public String getFrameTitle()
    {
        return frameTitle;
    }

    public void setFrameTitle(String frameTitle)
    {
        this.frameTitle = frameTitle;
    }

    public URL getStyleSheetUrl()
    {
        return styleSheetUrl;
    }

    public void setStyleSheetUrl(URL styleSheetUrl)
    {
        this.styleSheetUrl = styleSheetUrl;
    }

    public Controller getController()
    {
        return controller;
    }

    public void setController(Controller controller)
    {
        this.controller = controller;
    }

    public Stage getParent()
    {
        return parent;
    }

    public void setParent(Stage parent)
    {
        this.parent = parent;
    }

    public boolean isDecorated()
    {
        return isDecorated;
    }

    public void setDecorated(boolean decorated)
    {
        isDecorated = decorated;
    }

    public boolean isModal()
    {
        return isModal;
    }

    public void setModal(boolean modal)
    {
        isModal = modal;
    }

    @Override
    public boolean close() {
        return false;
    }

    //endregion


    public static class Builder
    {
        private GuiLoader guiLoader = new GuiLoader();

        public Builder setFxmlString(String fxmlString)
        {
            guiLoader.setFxmlString(fxmlString);
            return this;
        }

//        public Builder setFxmlURL(String url)
//        {
//            guiLoader.setFxmlString(HostedFxmlLoader.getFxml(url));
//            return this;
//        }

        public Builder setFrameTitle(String frameTitle)
        {
            guiLoader.setFrameTitle(frameTitle);
            guiLoader.setDecorated(true);
            return this;
        }

        public Builder setStyleSheetString(String cssString)
        {
            try
            {
                File themeFile = new File(Util.getWorkingDirectory() + File.separator + "wbTemp" + File.separator + "theme.css");
                themeFile.delete();

                FileUtils.writeStringToFile(themeFile, cssString, Charset.forName("UTF-8"));
                Timing.waitCondition(()-> themeFile.exists(), 10000);

                guiLoader.setStyleSheetUrl(themeFile.toURI().toURL());

                return this;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        public Builder setStyleSheetUrl(URL styleSheetUrl)
        {
            guiLoader.setStyleSheetUrl(styleSheetUrl);
            return this;
        }

        public Builder setController(Controller controller)
        {
            guiLoader.setController(controller);
            return this;
        }

        public Builder setParent(Stage parent)
        {
            guiLoader.setParent(parent);
            guiLoader.setModal(true);
            return this;
        }

        public GuiLoader build()
        {
            return this.guiLoader;
        }
    }
}