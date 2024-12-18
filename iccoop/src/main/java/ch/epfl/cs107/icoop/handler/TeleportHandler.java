package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

public interface TeleportHandler {
    String getDestinationAreaName();
    DiscreteCoordinates[] getTargetCoords();
    Orientation getDestinationOrientation();
}
