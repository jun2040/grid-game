package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public abstract class ICoopArea extends Area {
    public final static float DEFAULT_SCALE_FACTOR = 13.f;
    private float cameraScaleFactor = DEFAULT_SCALE_FACTOR;

    private ICoopBehavior areaBehavior;

    protected abstract void createArea();

    public abstract DiscreteCoordinates getPlayerSpawnPosition(int id);

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            this.areaBehavior = new ICoopBehavior(window, getTitle(), this);
            setBehavior(this.areaBehavior);
            createArea();
            return true;
        }
        return false;
    }

    @Override
    public float getCameraScaleFactor() { return cameraScaleFactor; }

    // TODO: Review needed, seemingly sketchy methods used
    public boolean isCellFree(DiscreteCoordinates coordinates) {
        return areaBehavior.isCellFree(coordinates);
    }

    public void setCameraScaleFactor(float cameraScaleFactor) {
        this.cameraScaleFactor = cameraScaleFactor;
    }
}
