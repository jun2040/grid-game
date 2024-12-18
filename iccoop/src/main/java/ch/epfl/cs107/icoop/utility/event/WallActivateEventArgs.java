package ch.epfl.cs107.icoop.utility.event;

public class WallActivateEventArgs extends EventArgs {
    private boolean isActivated;

    public WallActivateEventArgs(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public boolean isActivated() {
        return isActivated;
    }
}
