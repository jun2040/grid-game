package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.Context;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.And;
import ch.epfl.cs107.play.signal.logic.Logic;

import static ch.epfl.cs107.play.math.Orientation.*;

public class Maze extends ICoopArea {
    public static DiscreteCoordinates[] ARRIVAL_POINTS =
            new DiscreteCoordinates[] { new DiscreteCoordinates(2, 39), new DiscreteCoordinates(3, 39) };
    public static Orientation SPAWN_ORIENTATION = DOWN;
    /**
     * discrete coordinates arrays for easy iterations
     */
    private static final DiscreteCoordinates[] hellSkullSpawn = new DiscreteCoordinates[] {
            new DiscreteCoordinates(12, 33), new DiscreteCoordinates(12, 31),
            new DiscreteCoordinates(12, 29), new DiscreteCoordinates(12, 27),
            new DiscreteCoordinates(12, 25), new DiscreteCoordinates(10, 33),
            new DiscreteCoordinates(10, 32), new DiscreteCoordinates(10, 30),
            new DiscreteCoordinates(10, 28), new DiscreteCoordinates(10, 26)
    };

    private static final DiscreteCoordinates[] grenadierSpawn = new DiscreteCoordinates[] {
            new DiscreteCoordinates(5, 15), new DiscreteCoordinates(6, 17),
            new DiscreteCoordinates(10, 17), new DiscreteCoordinates(5, 14)
    };
    private static final DiscreteCoordinates[] heartSpawn = new DiscreteCoordinates[] {
            new DiscreteCoordinates(15, 18), new DiscreteCoordinates(16, 19),
            new DiscreteCoordinates(14, 17), new DiscreteCoordinates(14, 19)
    };

    private Logic fireStaff;
    private Logic waterStaff;

    /**
     *
     * @param context, allows the setting of hte dialog handler
     */
    public Maze(Context context) {
        super(context);
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        for (DiscreteCoordinates coord : hellSkullSpawn)
            registerActor(new HellSkull(this, RIGHT, coord));

        for (DiscreteCoordinates coord : grenadierSpawn)
            registerActor(new Grenadier(this, RIGHT, coord));

        for (DiscreteCoordinates coord : heartSpawn)
            registerActor(new Heart(this, LEFT, coord, 1));

        PressurePlate redPressurePlate = new PressurePlate(this, new DiscreteCoordinates(9, 25));
        PressurePlate bluePressurePlate = new PressurePlate(this, new DiscreteCoordinates(7, 33));

        registerActor(redPressurePlate);
        registerActor(bluePressurePlate);

        registerActor(new Explosive(this, UP, new DiscreteCoordinates(6, 25), 100));

        addElementalWall(new ElementalWall(
                this, DOWN, new DiscreteCoordinates(8, 20),
                true/*Logic.TRUE*/, "fire_wall", ElementType.FIRE
        ), redPressurePlate);

        for (int i = 0; i < 2; ++i) {
            addElementalWall(new ElementalWall(
                    this, LEFT, new DiscreteCoordinates(7, 35 + i),
                    true/*Logic.TRUE*/, "fire_wall", ElementType.FIRE
            ), bluePressurePlate);
            registerActor(new ElementalWall(
                    this, LEFT, new DiscreteCoordinates(4, 35 + i),
                    true/*Logic.TRUE*/, "water_wall", ElementType.WATER
            ));
            registerActor(new ElementalWall(
                    this, DOWN, new DiscreteCoordinates(2 + i, 34),
                    true/*Logic.TRUE*/, "fire_wall", ElementType.FIRE
            ));
            registerActor(new ElementalWall(
                    this, DOWN, new DiscreteCoordinates(5 + i, 24),
                    true/*Logic.TRUE*/, "water_wall", ElementType.WATER
            ));
        }

        Staff fireStaff = new Staff(this, new DiscreteCoordinates(13, 2), ElementType.FIRE, "staff_fire");
        Staff waterStaff = new Staff(this, new DiscreteCoordinates(8, 2), ElementType.WATER, "staff_water");

        this.fireStaff = fireStaff;
        this.waterStaff = waterStaff;

        registerActor(fireStaff);
        registerActor(waterStaff);

        registerActor(new Door(
                this, UP, "Arena", true,
                Arena.ARRIVAL_POINTS,
                new DiscreteCoordinates(19, 6),
                new DiscreteCoordinates(19, 7)
        ));

        registerActor(new ElementalWall(
                this, DOWN, new DiscreteCoordinates(13, 4),
                true/*Logic.TRUE*/, "fire_wall", ElementType.FIRE
        ));
        registerActor(new ElementalWall(
                this, DOWN, new DiscreteCoordinates(8, 4),
                true/*Logic.TRUE*/, "water_wall", ElementType.WATER
        ));

        registerActor(new Rock(this, UP, new DiscreteCoordinates(15, 7)));
        registerActor(new Rock(this, UP, new DiscreteCoordinates(15, 6)));
    }

    @Override
    public boolean isViewCentered() {
        return false;
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition(int id) {
        return ARRIVAL_POINTS[id];
    }
    /**
     * associates door with pressure plate for activation/deactiation
     */
    private void addElementalWall(ElementalWall elementalWall, PressurePlate pressurePlate) {
        pressurePlate.linkWall(elementalWall);
        registerActor(elementalWall);
    }
    /**
     *
     * @return orientation of the arriving player
     */
    public Orientation getSpawnOrientation(){
        return SPAWN_ORIENTATION;
    }

    /**
     *
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     *                  checks if both staffs were collected, then calls complete();
     *                  assigning this area to be complete to proceed to the next
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (new And(fireStaff, waterStaff).isOn())
            complete();
    }

    @Override
    public String getTitle() {
        return "Maze";
    }
}
