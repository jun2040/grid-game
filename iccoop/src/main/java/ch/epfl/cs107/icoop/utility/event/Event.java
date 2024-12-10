package ch.epfl.cs107.icoop.utility.event;

import java.util.ArrayList;
import java.util.List;

public abstract class Event {
    private List<EventListener> eventListeners = new ArrayList<>();

    public final void addEventListener(EventListener eventListener) {
        eventListeners.add(eventListener);
    }

    public final void emit() {
        for (EventListener listener : eventListeners)
            handleListener(listener, EventArgs.EMPTY);
    }

    public final void emit(EventArgs eventArgs) {
        for (EventListener listener : eventListeners)
            handleListener(listener, eventArgs);
    }

    public abstract void handleListener(EventListener listener, EventArgs eventArgs);
}
