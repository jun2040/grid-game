package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class TeleportController {
    private TeleportHandler teleportHandler;

    public void setTeleport(TeleportHandler teleportHandler) {
        this.teleportHandler = teleportHandler;
    }

    public String getTargetDestination() {
        return teleportHandler.getDestinationAreaName();
    }

    public DiscreteCoordinates getTeleportPosition(int id) {
        return teleportHandler.getTargetCoords()[id];
    }

    public boolean isTeleportRequested() {
        return teleportHandler != null;
    }

    public void resetTeleport() {
        teleportHandler = null;
    }
}
