package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.Door;

public class TeleportController {
    private String targetDestination = null;

    public void setTargetDestination(String targetDestination) {
        this.targetDestination = targetDestination;
    }

    public String getTargetDestination() {
        return targetDestination;
    }

    public boolean isTeleportRequested() {
        return targetDestination != null;
    }
}
