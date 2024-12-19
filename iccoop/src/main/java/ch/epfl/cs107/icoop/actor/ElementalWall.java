package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class ElementalWall extends AreaEntity implements ElementalEntity, Interactable, Interactor, Logic {
    private final Sprite[] wallSprites;
    private final ElementType elementType;
    private ElementType currentElementType;

    private final Logic isActive;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param isActive    (Logic) : Inital state of the wall if on or off. Not Null
     * @param spriteName  (String) : Name of the ressource used for the sprite. Not Null
     * @param elementType (ElementType): Elemental Type of the wall. Not Null
     */
    public ElementalWall(
            Area area,
            Orientation orientation,
            DiscreteCoordinates position,
            Logic isActive,
            String spriteName,
            ElementType elementType
    ) {
        super(area, orientation, position);

        this.isActive = isActive;

        this.wallSprites = RPGSprite.extractSprites(
                spriteName, 4, 1, 1,
                this, Vector.ZERO, 256, 256
        );
        this.elementType = elementType;
        this.currentElementType = elementType;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isActive.isOn())
            wallSprites[getOrientation().ordinal()].draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isActive.isOn())
            currentElementType = elementType;
        else
            currentElementType = ElementType.NONE;
    }

    /*
     * ElementalEntity implementation
     */
    @Override
    public String element() {
        return currentElementType.getName();
    }

    /*
     * Interactor implementation
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
    }

    /*
     * Interactable implementation
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of();
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    public void destroy() {
        unregister();
    }

    private void unregister() {
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public boolean isOn() {
        return isActive.isOn();
    }

    @Override
    public boolean isOff() {
        return isActive.isOff();
    }
}
