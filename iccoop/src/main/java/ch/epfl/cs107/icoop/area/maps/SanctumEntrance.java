package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.Context;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

public class SanctumEntrance extends ICoopArea {
    public static DiscreteCoordinates[] ARRIVAL_POINTS =
            new DiscreteCoordinates[] { new DiscreteCoordinates(1, 5), new DiscreteCoordinates(2, 5) };
    public SanctumEntrance(Context context) {
        super(context);
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition(int id) {
        return null;
    }

    @Override
    public Orientation getSpawnOrientation() {
        return null;
    }

    @Override
    public boolean isViewCentered() {
        return true;
    }

    @Override
    public String getTitle() {
        return "SanctumEntrance";
    }
}
