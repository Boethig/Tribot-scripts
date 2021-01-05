package scripts.boe_api.event_dispatcher;

import java.util.function.Consumer;

public class EventListener<T extends Event> {

    private Consumer<T> consumer;
    private int priority;

    public EventListener() {
        this(null);
    }

    public EventListener(Consumer<T> consumer) {
        this(consumer, 0);
    }

    public EventListener(Consumer<T> consumer, int priority) {
        this.consumer = consumer;
        this.priority = priority;
    }

    /**
     * Returns a Consumer that is trigger when the event is raised.
     * @return
     */
    protected Consumer<T> onEvent() {
        return null;
    }

    public void invoke(T event) {

        if (this.consumer == null)
            this.consumer = onEvent();

        if (this.consumer == null)
            throw new RuntimeException("No consumer was specified for the event listener. Supply one in the contructor or override the #onEvent method.");

        this.consumer.accept(event);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int value) {
        priority = value;
    }
}
