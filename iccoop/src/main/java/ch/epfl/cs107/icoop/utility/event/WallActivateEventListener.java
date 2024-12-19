package ch.epfl.cs107.icoop.utility.event;

public interface WallActivateEventListener extends EventListener {
    /**
     * only necessary functions to activate/deactivate wall provided
     * enables proper encapsulation
     */
    void activate();
    void deactivate();
}
