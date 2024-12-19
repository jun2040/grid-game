package ch.epfl.cs107.icoop.utility.event;

public class WallActivateEventArgs extends EventArgs {
    private boolean isActivated;

    /**
     * @param isActivated the argument in this case is the activation of the door
     */
    public WallActivateEventArgs(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public boolean isActivated() {
        return isActivated;
    }
}
