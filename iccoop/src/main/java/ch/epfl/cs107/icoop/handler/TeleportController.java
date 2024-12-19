package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

public class TeleportController {
    private TeleportHandler teleportHandler;

    /**
     *
     * @param teleportHandler sets teleporthandler to prepare for teleportation
     */
    public void setTeleport(TeleportHandler teleportHandler) {
        this.teleportHandler = teleportHandler;
    }

    /**
     *
     * @return String name of destination area
     */
    public String getTargetDestination() {
        return teleportHandler.getDestinationAreaName();
    }

    /**
     *
     * @return Orientation of player in the target area
     */
    public Orientation getTargetOrientation() {
        return teleportHandler.getDestinationOrientation();
    }

    /**
     *
     * @param id
     * @return DiscreteCoordinates of new area spawning positions once teleported
     */

    public DiscreteCoordinates getTeleportPosition(int id) {
        return teleportHandler.getTargetCoords()[id];
    }

    /**
     *
     * @return returns whether teleportation should occur
     */
    public boolean isTeleportRequested() {
        return teleportHandler != null;
    }

    /**
     * deletes previous teleporthandler
     */
    public void resetTeleport() {
        teleportHandler = null;
    }
}
