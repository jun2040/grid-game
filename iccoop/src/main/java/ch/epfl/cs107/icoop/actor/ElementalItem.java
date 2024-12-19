package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import static ch.epfl.cs107.play.math.Orientation.UP;

public abstract class ElementalItem extends ICoopCollectable implements ElementalEntity, Logic {
    private final ElementType elementType;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param elementType (ElementType): Elemental type of item. Can be NULL
     */
    public ElementalItem(Area area, DiscreteCoordinates position, ElementType elementType) {
        super(area, UP, position);

        this.elementType = elementType;
    }

    public ElementalItem(Area area, Orientation orientation, DiscreteCoordinates position, ElementType elementType, ICoopItem itemModel) {
        super(area, orientation, position, itemModel);

        this.elementType = elementType;
    }

    @Override
    public String element() {
        return elementType.getName();
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public boolean isOn() {
        return isCollected();
    }

    @Override
    public boolean isOff() {
        return !isCollected();
    }
}
