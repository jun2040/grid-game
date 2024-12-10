package ch.epfl.cs107.icoop.utility.event;

public interface WallActivateEventListener extends EventListener {
    void activate();
    void deactivate();
}
