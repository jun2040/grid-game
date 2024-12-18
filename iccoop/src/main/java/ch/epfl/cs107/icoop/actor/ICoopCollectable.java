package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

public abstract class ICoopCollectable extends CollectableAreaEntity implements Interactable {
    private final ICoopItem itemModel;

    public ICoopCollectable(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.itemModel = null;
    }

    public ICoopCollectable(Area area, Orientation orientation, DiscreteCoordinates position, ICoopItem itemModel) {
        super(area, orientation, position);

        this.itemModel = itemModel;
    }

    public boolean isItem() {
        return itemModel != null;
    }

    public ICoopItem getItemModel() {
        return itemModel;
    }

    @Override
    public boolean takeCellSpace() { return false; }

    @Override
    public boolean isCellInteractable() { return true; }

    @Override
    public boolean isViewInteractable() { return false; }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isCollected())
            getOwnerArea().unregisterActor(this);
    }
}
