package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

public interface TeleportHandler {
    /**
     * interface used to manage the teleportation events while encapsulating each party taking part in the interaction
     *
     * @return
     */
    String getDestinationAreaName();

    DiscreteCoordinates[] getTargetCoords();

    Orientation getDestinationOrientation();
}
