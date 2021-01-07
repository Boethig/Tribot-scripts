package scripts.boe_api.event_dispatcher.events;

import lombok.Getter;
import scripts.boe_api.event_dispatcher.Event;
import scripts.boe_api.profile_manager.BasicScriptSettings;

public class ConfigureScriptCompletedEvent extends Event {

    @Getter
    private BasicScriptSettings settings;

    public ConfigureScriptCompletedEvent(BasicScriptSettings settings) {
        super();
        this.settings = settings;
    }

    public ConfigureScriptCompletedEvent() {
        super();
    }
}
