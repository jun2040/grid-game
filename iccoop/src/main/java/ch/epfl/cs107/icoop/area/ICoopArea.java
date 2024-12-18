package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.handler.Context;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;

public abstract class ICoopArea extends Area implements Logic {
    public final static float DEFAULT_SCALE_FACTOR = 13.f;
    private float cameraScaleFactor = DEFAULT_SCALE_FACTOR;
    private Logic isDirty = Logic.FALSE;

    private ICoopBehavior areaBehavior;
    private boolean completed = false;

    private final Context context;

    protected abstract void createArea();

    public abstract DiscreteCoordinates getPlayerSpawnPosition(int id);
    public abstract Orientation getSpawnOrientation();

    public ICoopArea(Context context) {
        this.context = context;
    }

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            this.areaBehavior = new ICoopBehavior(window, getTitle(), this);
            setBehavior(this.areaBehavior);
            createArea();
            return true;
        }
        return false;
    }

    protected Context getContext() {
        return context;
    }

    @Override
    public void update(float deltaTime) {
        if(!isDirty.isOn()){
            super.update(deltaTime);
        }
    }

    public void setIsDirty(Logic logic){
        this.isDirty = logic;
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

    public void complete() {
        completed = true;
    }

    @Override
    public boolean isOn() {
        return completed;
    }

    @Override
    public boolean isOff() {
        return !completed;
    }
}
