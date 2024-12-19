package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

/**
 * The TeleportController handles teleportation requests and coordinates player movement between areas.
 */
public class TeleportController {

    /** The current teleport handler managing the teleportation process. */
    private TeleportHandler teleportHandler;

    /**
     * Sets the teleport handler to prepare for teleportation.
     *
     * @param teleportHandler (TeleportHandler): The handler that manages the teleportation. Not null.
     */
    public void setTeleport(TeleportHandler teleportHandler) {
        this.teleportHandler = teleportHandler;
    }

    /**
     * Retrieves the name of the destination area.
     *
     * @return (String): The name of the target destination area.
     */
    public String getTargetDestination() {
        return teleportHandler.getDestinationAreaName();
    }

    /**
     * Retrieves the orientation of the player in the target area.
     *
     * @return (Orientation): The orientation of the player after teleportation.
     */
    public Orientation getTargetOrientation() {
        return teleportHandler.getDestinationOrientation();
    }

    /**
     * Retrieves the spawning position in the new area for a specific player.
     *
     * @param id (int): The ID of the player.
     * @return (DiscreteCoordinates): The coordinates where the player will spawn in the target area.
     */
    public DiscreteCoordinates getTeleportPosition(int id) {
        return teleportHandler.getTargetCoords()[id];
    }

    /**
     * Checks if teleportation has been requested.
     *
     * @return (boolean): True if teleportation is requested; false otherwise.
     */
    public boolean isTeleportRequested() {
        return teleportHandler != null;
    }

    /**
     * Resets the teleport handler, clearing the current teleportation request.
     */
    public void resetTeleport() {
        teleportHandler = null;
    }
}
