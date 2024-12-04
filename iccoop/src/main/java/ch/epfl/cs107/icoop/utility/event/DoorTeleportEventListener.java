package ch.epfl.cs107.icoop.utility.event;

import ch.epfl.cs107.icoop.actor.Door;

/* https://stackoverflow.com/questions/6270132/create-a-custom-event-in-java */
public interface DoorTeleportEventListener extends EventListener {
    void teleport(Door door);
}
