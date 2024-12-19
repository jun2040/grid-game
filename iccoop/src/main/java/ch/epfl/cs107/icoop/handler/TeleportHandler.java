package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

/**
 * Interface for managing teleportation events, encapsulating the entities involved in teleportation interactions.
 */
public interface TeleportHandler {

    /**
     * Retrieves the name of the destination area for teleportation.
     *
     * @return (String): The name of the destination area.
     */
    String getDestinationAreaName();

    /**
     * Retrieves the coordinates in the destination area where the entity will spawn.
     *
     * @return (DiscreteCoordinates[]): Array of target coordinates for teleportation.
     */
    DiscreteCoordinates[] getTargetCoords();

    /**
     * Retrieves the orientation of the entity in the destination area after teleportation.
     *
     * @return (Orientation): The orientation in the destination area.
     */
    Orientation getDestinationOrientation();
}
