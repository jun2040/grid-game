package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.And;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a teleporter entity that activates when specific keys are collected.
 */
public class Teleporter extends Door {

    /** A queue of keys required to activate the teleporter. */
    private final Queue<Key> keys;

    /** The sprite representing the teleporter visually. */
    private final Sprite sprite;

    /**
     * Constructs a Teleporter entity.
     *
     * @param area                (Area): The area to which the teleporter belongs. Not null.
     * @param orientation         (Orientation): The initial orientation of the teleporter. Not null.
     * @param destinationAreaName (String): The name of the destination area. Not null.
     * @param targetCoords        (DiscreteCoordinates[]): The coordinates where the player spawns in the destination area.
     * @param mainPosition        (DiscreteCoordinates): The initial position of the teleporter in the area. Not null.
     */
    public Teleporter(Area area, Orientation orientation, String destinationAreaName, DiscreteCoordinates[] targetCoords, DiscreteCoordinates mainPosition) {
        super(area, orientation, destinationAreaName, false, targetCoords, mainPosition);

        this.keys = new LinkedList<>();
        this.sprite = new RPGSprite("shadow", 1, 1, this,
                new RegionOfInterest(0, 0, 32, 32));
    }

    /**
     * Draws the teleporter on the canvas if it is active.
     *
     * @param canvas (Canvas): The canvas to draw on.
     */
    @Override
    public void draw(Canvas canvas) {
        if (isOn()) {
            sprite.draw(canvas);
        }
    }

    /**
     * Updates the teleporter's activation state based on the collected keys.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Check the state of all keys and update the teleporter's activation status.
        Logic logic = Logic.TRUE;
        Queue<Key> newKeys = new LinkedList<>();

        while (!keys.isEmpty()) {
            Key currentKey = keys.remove();

            logic = new And(logic, currentKey);

            if (!currentKey.isCollected()) {
                newKeys.add(currentKey);
            }
        }

        keys.addAll(newKeys);

        if (logic.isOn()) {
            open();
        } else {
            close();
        }
    }

    /**
     * Adds a key to the teleporter's activation requirement.
     *
     * @param key (Key): The key required to activate the teleporter.
     */
    public void addKey(Key key) {
        keys.add(key);
    }
}
