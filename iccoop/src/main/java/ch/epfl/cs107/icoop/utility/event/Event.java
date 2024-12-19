package ch.epfl.cs107.icoop.utility.event;

import java.util.ArrayList;
import java.util.List;

public abstract class Event {
    private List<EventListener> eventListeners = new ArrayList<>();

    /**
     * @param eventListener interface of class eventlistener
     *                      allows encapsulated control over triggered events
     */
    public final void addEventListener(EventListener eventListener) {
        eventListeners.add(eventListener);
    }

    /**
     * sends arguments to the event listener to be treated and react appropriately
     */
    public final void emit() {
        for (EventListener listener : eventListeners)
            handleListener(listener, EventArgs.EMPTY);
    }

    /**
     * sends arguments to the event listener to be treated and react appropriately
     */
    public final void emit(EventArgs eventArgs) {
        for (EventListener listener : eventListeners)
            handleListener(listener, eventArgs);
    }

    /**
     * @param listener  (EventListener) interface which allows to monitor the triggering of an event
     * @param eventArgs (EventArgs) event arguments which are passed to ensure all and only necessary information
     *                  is passed on
     */
    public abstract void handleListener(EventListener listener, EventArgs eventArgs);
}
