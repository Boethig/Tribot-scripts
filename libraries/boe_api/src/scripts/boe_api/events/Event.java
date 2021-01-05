package scripts.boe_api.events;

public class Event {

    private boolean isPropagationStopped = false;

    /**
     * Returns whether further event listeners should be triggered.
     * See {@link this#stopPropagation} for more info.
     *
     * @return Whether propagation was already stopped for this event
     */
    public boolean isPropagationStopped() {
        return isPropagationStopped;
    }

    /**
     * Stops the propagation of the event to further event listeners.
     *
     * If multiple event listeners are connected to the same event, no
     * further event listener will be triggered once any trigger calls
     * stopPropagation().
     */
    public void stopPropagation() {
        isPropagationStopped = true;
    }
}
