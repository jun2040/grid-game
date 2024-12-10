package ch.epfl.cs107.icoop.utility.event;

/* https://stackoverflow.com/questions/6270132/create-a-custom-event-in-java */
public class DoorTeleportEvent extends Event {
    @Override
    public void handleListener(EventListener listener, EventArgs eventArgs) {
        ((DoorTeleportEventListener) listener).teleport(((DoorTeleportEventArgs) eventArgs).getDoor());
    }
}
