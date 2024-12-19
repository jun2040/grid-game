package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Represents a key that can only be picked up by players of the same elemental type.
 */
public class Key extends ElementalItem {
    private final Sprite sprite;

    /**
     * Constructs a Key object with the specified parameters.
     *
     * @param area        (Area): The area to which the key belongs. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the key in the area. Not null.
     * @param elementType (ElementType): The elemental type of the key, determining which players can pick it up. Not null.
     */
    public Key(Area area, DiscreteCoordinates position, ElementType elementType) {
        super(area, position, elementType);

        String name = elementType.getName().equals("feu") ? "icoop/key_red" : "icoop/key_blue";
        this.sprite = new Sprite(name, 0.6f, 0.6f, this);
        this.sprite.setAnchor(new Vector(0.2f, 0.2f));
    }

    /**
     * Draws the key's sprite on the canvas.
     *
     * @param canvas (Canvas): The canvas to draw on.
     */
    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }
}
