package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.Context;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.signal.logic.Not;

import static ch.epfl.cs107.play.math.Orientation.*;
import static ch.epfl.cs107.icoop.actor.ElementType.*;

public class OrbWay extends ICoopArea {
    public static DiscreteCoordinates[] ARRIVAL_POINTS =
            new DiscreteCoordinates[]{new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 5)};
    public static Orientation SPAWN_ORIENTATION = RIGHT;

    /**
     * @param context, allows the setting of hte dialog handler
     */
    public OrbWay(Context context) {
        super(context);
    }

    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        registerActor(new Door(
                this, RIGHT, "Spawn", true,
                Spawn.ARRIVAL_POINTS,
                new DiscreteCoordinates(0, 14),
                new DiscreteCoordinates(0, 13),
                new DiscreteCoordinates(0, 12),
                new DiscreteCoordinates(0, 11),
                new DiscreteCoordinates(0, 10)
        ));
        registerActor(new Door(
                this, RIGHT, "Spawn", true,
                Spawn.ARRIVAL_POINTS,
                new DiscreteCoordinates(0, 8),
                new DiscreteCoordinates(0, 7),
                new DiscreteCoordinates(0, 6),
                new DiscreteCoordinates(0, 5),
                new DiscreteCoordinates(0, 4)
        ));

        /*
         * Create & register elemental orbs
         */
        Orb fireOrb = new Orb(this, new DiscreteCoordinates(17, 12), FIRE);
        Orb waterOrb = new Orb(this, new DiscreteCoordinates(17, 6), WATER);

        fireOrb.setDialogHandler(getContext().getDialogHandler());
        waterOrb.setDialogHandler(getContext().getDialogHandler());

        registerActor(fireOrb);
        registerActor(waterOrb);

        /*
         * Create & register pressure plates
         */
        PressurePlate redPressurePlate = new PressurePlate(this, new DiscreteCoordinates(5, 10));
        PressurePlate bluePressurePlate = new PressurePlate(this, new DiscreteCoordinates(5, 7));

        registerActor(redPressurePlate);
        registerActor(bluePressurePlate);

        /*
         * Create & register elemental walls
         */
        for (int i = 0; i < 5; ++i) {
            registerActor(new ElementalWall(
                    this, LEFT, new DiscreteCoordinates(12, 10 + i),
                    new Not(bluePressurePlate), "fire_wall", FIRE
            ));
            registerActor(new ElementalWall(
                    this, LEFT, new DiscreteCoordinates(12, 4 + i),
                    new Not(redPressurePlate), "water_wall", WATER
            ));
        }

        registerActor(new ElementalWall(
                this, LEFT, new DiscreteCoordinates(7, 12),
                Logic.TRUE, "water_wall", WATER
        ));
        registerActor(new ElementalWall(
                this, LEFT, new DiscreteCoordinates(7, 6),
                Logic.TRUE, "fire_wall", FIRE
        ));

        /*
         * Create & register hearts
         */
        registerActor(new Heart(this, LEFT, new DiscreteCoordinates(8, 4), 1));
        registerActor(new Heart(this, LEFT, new DiscreteCoordinates(10, 6), 1));
        registerActor(new Heart(this, LEFT, new DiscreteCoordinates(5, 13), 1));
        registerActor(new Heart(this, LEFT, new DiscreteCoordinates(10, 11), 1));

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
        return Spawn.TOP_RIGHT_ORIENTATION;
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition(int id) {
        return ARRIVAL_POINTS[id];
    }

    @Override
    public String getTitle() {
        return "OrbWay";
    }
}
