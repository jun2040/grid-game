package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.Collections;
import java.util.List;

public abstract class ElementalItem extends ICoopCollectable implements ElementalEntity, Logic {
    private final ElementType elementType;

    public ElementalItem(Area area, Orientation orientation, DiscreteCoordinates position, ElementType elementType) {
        super(area, orientation, position);

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
    public boolean isOn() {
        return isCollected();
    }

    @Override
    public boolean isOff() {
        return !isCollected();
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
