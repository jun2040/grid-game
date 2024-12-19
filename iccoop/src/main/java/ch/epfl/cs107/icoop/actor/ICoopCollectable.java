package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.Collections;
import java.util.List;

public abstract class ICoopCollectable extends CollectableAreaEntity implements Interactable {
    private final ICoopItem itemModel;

    /**
     * /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public ICoopCollectable(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.itemModel = null;
    }

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param itemModel   (ICoopItem) : makes this item collectable by the player
     */

    public ICoopCollectable(Area area, Orientation orientation, DiscreteCoordinates position, ICoopItem itemModel) {
        super(area, orientation, position);

        this.itemModel = itemModel;
    }

    /**
     * @return true if it contains an item to be collected
     */
    public boolean isItem() {
        return itemModel != null;
    }

    /**
     * @return the item it contains
     */

    public ICoopItem getItemModel() {
        return itemModel;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isCollected())
            getOwnerArea().unregisterActor(this);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }
}
