package ch.epfl.cs107.icoop.utility.event;

public class WallActivateEvent extends Event {
    @Override
    public void handleListener(EventListener listener, EventArgs eventArgs) {
        if (((WallActivateEventArgs) eventArgs).isActivated())
            ((WallActivateEventListener) listener).activate();
        else
            ((WallActivateEventListener) listener).deactivate();
    }
}
