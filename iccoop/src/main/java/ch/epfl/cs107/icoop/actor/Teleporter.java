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

public class Teleporter extends Door {
    private final Queue<Key> keys;
    private final Sprite sprite;

    public Teleporter(Area area, Orientation orientation, String destinationAreaName, DiscreteCoordinates[] targetCoords, DiscreteCoordinates mainPosition) {
        super(area, orientation, destinationAreaName, false, targetCoords, mainPosition);

        this.keys = new LinkedList<>();
        this.sprite = new RPGSprite("shadow", 1, 1, this,
                new RegionOfInterest(0, 0, 32, 32));
    }

    @Override
    public void draw(Canvas canvas) {
        if (isOn())
            sprite.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Logic logic = Logic.TRUE;
        Queue<Key> newKeys = new LinkedList<>();
        while (!keys.isEmpty()) {
            Key currentKey = keys.remove();

            logic = new And(logic, currentKey);

            if (!currentKey.isCollected())
                newKeys.add(currentKey);
        }
        keys.addAll(newKeys);

        if (logic.isOn())
            open();
        else
            close();
    }

    public void addKey(Key key) { keys.add(key); }
}
