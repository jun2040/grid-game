package ch.epfl.cs107.icoop.utility.event;

public class WallActivateEvent extends Event {
    /**
     * @param listener  (EventListener) interface which allows to monitor the triggering of an event
     * @param eventArgs (EventArgs) event arguments which are passed to ensure all and only necessary information
     *                  is passed on
     *                  will listen to activation/deactivation of wall and serve as an intermediate between 2 entities during an event
     */
    @Override
    public void handleListener(EventListener listener, EventArgs eventArgs) {
        if (((WallActivateEventArgs) eventArgs).isActivated())
            ((WallActivateEventListener) listener).activate();
        else
            ((WallActivateEventListener) listener).deactivate();
    }
}
