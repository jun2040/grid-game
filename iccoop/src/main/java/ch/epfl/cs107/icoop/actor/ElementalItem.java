package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

public abstract class ElementalItem extends ICoopCollectable implements ElementalEntity, Logic {
    private final ElementType elementType;
    private boolean isCollected = false;

    public ElementalItem(Area area, Orientation orientation, DiscreteCoordinates position, ElementType elementType) {
        super(area, orientation, position);

        this.elementType = elementType;
    }

    @Override
    public String element() {
        return elementType.getName();
    }

    @Override
    public boolean isOn() {
        return isCollected;
    }

    @Override
    public boolean isOff() {
        return !isCollected;
    }
}
