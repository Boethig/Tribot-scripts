package scripts.boe_api.gui;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * @author Wastedbro
 */
public abstract class Controller implements Initializable
{
    private Stage hostStage;
    private boolean isDone = false, wasCancelled = true;

    public boolean isDone()
    {
        return isDone;
    }
    public boolean wasCancelled()
    {
        return wasCancelled;
    }

    public Stage getHostStage()
    {
        return hostStage;
    }

    public void setHostStage(Stage hostStage)
    {
        this.hostStage = hostStage;
    }


    public void setDone(boolean done)
    {
        isDone = done;
    }

    public void setWasCancelled(boolean wasCancelled)
    {
        this.wasCancelled = wasCancelled;
    }
}