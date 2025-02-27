package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.ElementType;
import ch.epfl.cs107.icoop.actor.Key;
import ch.epfl.cs107.icoop.actor.Teleporter;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.Context;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.And;
import ch.epfl.cs107.play.signal.logic.Logic;

import static ch.epfl.cs107.play.math.Orientation.*;

public class Arena extends ICoopArea {
    public static DiscreteCoordinates[] ARRIVAL_POINTS =
            new DiscreteCoordinates[]{new DiscreteCoordinates(4, 5), new DiscreteCoordinates(14, 15)};
    public static Orientation SPAWN_ORIENTATION = Orientation.DOWN;

    private Logic fireKey;
    private Logic waterKey;

    /**
     * @param context, allows the setting of hte dialog handler
     */
    public Arena(Context context) {
        super(context);
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        Key fireKey = new Key(this, new DiscreteCoordinates(9, 16), ElementType.FIRE);
        Key waterKey = new Key(this, new DiscreteCoordinates(9, 4), ElementType.WATER);
        Teleporter teleporter = new Teleporter(this, UP, "Spawn", Spawn.SPAWN_POINTS, new DiscreteCoordinates(10, 10));

        this.fireKey = fireKey;
        this.waterKey = waterKey;

        teleporter.addKey(fireKey);
        teleporter.addKey(waterKey);

        registerActor(fireKey);
        registerActor(waterKey);
        registerActor(teleporter);
    }

    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     *                  checks if both keys were collected, then calls complete();
     *                  assigning this area to be complete to proceed to the next
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (new And(fireKey, waterKey).isOn())
            complete();
    }

    @Override
    public boolean isViewCentered() {
        return true;
    }

    /**
     * @return orientation of the arriving player
     */
    public Orientation getSpawnOrientation() {
        return SPAWN_ORIENTATION;
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition(int id) {
        return ARRIVAL_POINTS[id];
    }

    @Override
    public String getTitle() {
        return "Arena";
    }
}
