package scripts.boe_api.gui;

import javafx.fxml.Initializable;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Wastedbro
 */
public abstract class Controller implements Initializable {
    @Getter @Setter
    private Stage hostStage;
    @Getter @Setter
    private boolean done = false;
    @Getter @Setter
    private boolean cancelled = true;
}