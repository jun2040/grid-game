package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.Collections;
import java.util.List;

/**
 * Represents a collectable item in the game that can optionally contain an ICoopItem.
 */
public abstract class ICoopCollectable extends CollectableAreaEntity implements Interactable {

    /**
     * The item model associated with this collectable, if any.
     */
    private final ICoopItem itemModel;

    /**
     * Constructs an ICoopCollectable without an associated item model.
     *
     * @param area        (Area): The area to which the collectable belongs. Not null.
     * @param orientation (Orientation): The orientation of the collectable. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the collectable. Not null.
     */
    public ICoopCollectable(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.itemModel = null;
    }

    /**
     * Constructs an ICoopCollectable with an associated item model.
     *
     * @param area        (Area): The area to which the collectable belongs. Not null.
     * @param orientation (Orientation): The orientation of the collectable. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the collectable. Not null.
     * @param itemModel   (ICoopItem): The item model that makes this collectable interactable by the player.
     */
    public ICoopCollectable(Area area, Orientation orientation, DiscreteCoordinates position, ICoopItem itemModel) {
        super(area, orientation, position);
        this.itemModel = itemModel;
    }

    /**
     * Checks if this collectable contains an item.
     *
     * @return (boolean): True if the collectable contains an item, false otherwise.
     */
    public boolean isItem() {
        return itemModel != null;
    }

    /**
     * Retrieves the item model associated with this collectable.
     *
     * @return (ICoopItem): The item model, or null if no item is associated.
     */
    public ICoopItem getItemModel() {
        return itemModel;
    }

    /**
     * Indicates that the collectable does not occupy cell space.
     *
     * @return (boolean): Always false.
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * Indicates that the collectable is interactable at the cell level.
     *
     * @return (boolean): Always true.
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * Indicates that the collectable is not interactable at the view level.
     *
     * @return (boolean): Always false.
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * Updates the state of the collectable, unregistering it from the area if it is collected.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isCollected()) {
            getOwnerArea().unregisterActor(this);
        }
    }

    /**
     * Retrieves the current cells occupied by the collectable.
     *
     * @return (List < DiscreteCoordinates >): A singleton list containing the main cell coordinates.
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
}
