package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.Context;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

public class Sanctum extends ICoopArea {
    /**
     *
     * @param context, allows the setting of hte dialog handler
     */
    public Sanctum(Context context) {
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
    public String getTitle() {
        return "Sanctum";
    }
}
