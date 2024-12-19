package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import static ch.epfl.cs107.play.math.Orientation.UP;

/**
 * Represents an item in the game world that has an elemental property.
 * Elemental items can interact with the environment or other entities based on their elemental type.
 */
public abstract class ElementalItem extends ICoopCollectable implements ElementalEntity, Logic {

    /**
     * The elemental type of this item (e.g., Fire, Water, etc.).
     */
    private final ElementType elementType;

    /**
     * Constructs an ElementalItem with a default orientation of UP.
     *
     * @param area        (Area): The area this item belongs to. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the item in the area. Not null.
     * @param elementType (ElementType): The elemental type of the item. Can be null.
     */
    public ElementalItem(Area area, DiscreteCoordinates position, ElementType elementType) {
        super(area, UP, position);
        this.elementType = elementType;
    }

    /**
     * Constructs an ElementalItem with a specified orientation.
     *
     * @param area        (Area): The area this item belongs to. Not null.
     * @param orientation (Orientation): The orientation of the item. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the item in the area. Not null.
     * @param elementType (ElementType): The elemental type of the item. Can be null.
     * @param itemModel   (ICoopItem): The item model representing the visual appearance or behavior of the item.
     */
    public ElementalItem(Area area, Orientation orientation, DiscreteCoordinates position, ElementType elementType, ICoopItem itemModel) {
        super(area, orientation, position, itemModel);
        this.elementType = elementType;
    }

    /**
     * Retrieves the name of the elemental type associated with this item.
     *
     * @return (String): The name of the item's elemental type.
     */
    @Override
    public String element() {
        return elementType.getName();
    }

    /**
     * Accepts an interaction with this item, delegating to the interaction visitor.
     *
     * @param v                (AreaInteractionVisitor): The visitor handling the interaction.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * Checks whether the item is "on," which corresponds to being collected.
     *
     * @return (boolean): True if the item is collected, false otherwise.
     */
    @Override
    public boolean isOn() {
        return isCollected();
    }

    /**
     * Checks whether the item is "off," which corresponds to not being collected.
     *
     * @return (boolean): True if the item is not collected, false otherwise.
     */
    @Override
    public boolean isOff() {
        return !isCollected();
    }
}
