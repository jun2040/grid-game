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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Teleporter extends Door implements Logic {
    private final Queue<Key> keys;
    private final Sprite sprite;

    public Teleporter(Area area, Orientation orientation, String destinationAreaName, DiscreteCoordinates[] targetCoords, DiscreteCoordinates mainPosition) {
        super(area, orientation, destinationAreaName, Logic.FALSE, targetCoords, mainPosition);

        this.keys = new LinkedList<>();
        this.sprite = new RPGSprite("shadow", 1, 1, this,
                new RegionOfInterest(0, 0, 32, 32));
    }

    @Override
    public void draw(Canvas canvas) {
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

        setSignal(logic);
    }

    public void addKey(Key key) { keys.add(key); }

    // TODO: Refactor
    @Override
    public boolean isOn() {
        Logic logic = Logic.TRUE;
        for (Key key : keys)
            logic = new And(logic, key);
        return logic.isOn();
    }

    @Override
    public boolean isOff() {
        return !isOn();
    }
}
