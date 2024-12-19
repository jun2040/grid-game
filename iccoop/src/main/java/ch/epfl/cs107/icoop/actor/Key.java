package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Key extends ElementalItem {
    private final Sprite sprite;

    // TODO: Consider defaulting the orientation to UP for items
    public Key(Area area, Orientation orientation, DiscreteCoordinates position, ElementType elementType) {
        super(area, orientation, position, elementType);

        String name = elementType.getName().equals("feu") ? "icoop/key_red" : "icoop/key_blue";
        this.sprite = new Sprite(name, 0.6f, 0.6f, this);
        this.sprite.setAnchor(new Vector(0.2f, 0.2f));
    }

    @Override
    public void draw(Canvas canvas) { sprite.draw(canvas); }
}
