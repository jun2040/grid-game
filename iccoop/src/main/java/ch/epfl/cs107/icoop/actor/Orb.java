package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import static ch.epfl.cs107.play.math.Orientation.*;

import java.util.Collections;
import java.util.List;

/**
 * Represents an elemental Orb in the game, which has interactions and a dialog triggered upon collection.
 */
public class Orb extends ElementalItem {
    private static final int ANIMATION_DURATION = 24;
    private static final int ANIMATION_FRAMES = 6;

    private final OrbType orbType;
    private final Animation animation;
    private DialogHandler dialogHandler;

    /**
     * Constructs an Orb entity.
     *
     * @param area        (Area): The area to which the Orb belongs. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the Orb in the area. Not null.
     * @param elementType (ElementType): The elemental type associated with the Orb.
     */
    public Orb(Area area, DiscreteCoordinates position, ElementType elementType) {
        super(area, position, elementType);

        orbType = OrbType.fromElementType(elementType);

        final Sprite[] sprites = new Sprite[ANIMATION_FRAMES];
        for (int i = 0; i < ANIMATION_FRAMES; i++) {
            sprites[i] = new RPGSprite("icoop/orb", 1, 1, this,
                    new RegionOfInterest(i * 32, orbType.getSpriteYDelta(), 32, 32));
        }

        this.animation = new Animation(ANIMATION_DURATION / ANIMATION_FRAMES, sprites);
    }

    /**
     * Draws the Orb on the canvas.
     *
     * @param canvas (Canvas): The canvas to draw on.
     */
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    /**
     * Updates the Orb's animation over time.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        animation.update(deltaTime);
    }

    /**
     * Handles interactions with the Orb.
     *
     * @param v                (AreaInteractionVisitor): The visitor managing the interaction.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
        ((ICoopInteractionVisitor) v).interactWith((ElementalItem) this, isCellInteraction);
    }

    /**
     * Triggers the dialog associated with the Orb. To be called upon collection.
     */
    public void triggerDialog() {
        if (dialogHandler != null) {
            dialogHandler.publish(new Dialog(orbType.getDialogPath()));
        }
    }

    /**
     * Sets the dialog handler for the Orb.
     *
     * @param dialogHandler (DialogHandler): The handler responsible for displaying dialogs.
     */
    public void setDialogHandler(DialogHandler dialogHandler) {
        this.dialogHandler = dialogHandler;
    }

    /**
     * Enum representing the Orb's type, combining elemental type with specific behaviors like dialog messages and sprites.
     */
    private enum OrbType {
        FIRE(ElementType.FIRE, "orb_fire_msg", 64),
        WATER(ElementType.WATER, "orb_water_msg", 0),
        NONE(ElementType.NONE, "", -1);

        private final ElementType elementType;
        private final String dialogPath;
        private final int spriteYDelta;

        /**
         * Constructs an OrbType instance.
         *
         * @param type        (ElementType): The elemental type of the Orb.
         * @param dialogPath  (String): The path to the dialog resource.
         * @param spriteYDelta (int): The vertical offset in the sprite sheet for this OrbType.
         */
        OrbType(ElementType type, String dialogPath, int spriteYDelta) {
            this.elementType = type;
            this.dialogPath = dialogPath;
            this.spriteYDelta = spriteYDelta;
        }

        /**
         * Retrieves the elemental type of the Orb.
         *
         * @return (ElementType): The elemental type.
         */
        public ElementType getElementType() {
            return elementType;
        }

        /**
         * Retrieves the dialog path associated with the Orb.
         *
         * @return (String): The dialog path.
         */
        public String getDialogPath() {
            return dialogPath;
        }

        /**
         * Retrieves the sprite's vertical offset in the sprite sheet.
         *
         * @return (int): The vertical offset for the sprite.
         */
        public int getSpriteYDelta() {
            return spriteYDelta;
        }

        /**
         * Maps an ElementType to its corresponding OrbType.
         *
         * @param elementType (ElementType): The elemental type.
         * @return (OrbType): The corresponding OrbType.
         */
        public static OrbType fromElementType(ElementType elementType) {
            for (OrbType orbType : OrbType.values()) {
                if (orbType.getElementType().equals(elementType)) {
                    return orbType;
                }
            }

            return NONE;
        }
    }
}
