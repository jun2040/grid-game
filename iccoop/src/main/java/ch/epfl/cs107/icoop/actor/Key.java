package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Key extends ElementalItem {
    private final Sprite sprite;

    // TODO: Consider defaulting the orientation to UP for items

    /**
     *
     *  Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param elementType (ElementType) : Elemental type of the key, can only be picked up by same elemental types. Not Null
     */
    public Key(Area area, Orientation orientation, DiscreteCoordinates position, ElementType elementType) {
        super(area, orientation, position, elementType);

        String name = elementType.getName().equals("feu") ? "icoop/key_red" : "icoop/key_blue";
        this.sprite = new Sprite(name, 0.6f, 0.6f, this);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
        ((ICoopInteractionVisitor) v).interactWith((ElementalItem) this, isCellInteraction);
    }

    @Override
    public void draw(Canvas canvas) { sprite.draw(canvas); }
}
