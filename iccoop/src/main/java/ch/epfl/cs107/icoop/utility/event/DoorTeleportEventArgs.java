package ch.epfl.cs107.icoop.utility.event;

import ch.epfl.cs107.icoop.actor.Door;

public class DoorTeleportEventArgs extends EventArgs {
    private Door door;

    public DoorTeleportEventArgs(Door door) {
        this.door = door;
    }

    public Door getDoor() { return door; }
}
