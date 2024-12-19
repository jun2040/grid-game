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

public class Orb extends ElementalItem {
    private static final int ANIMATION_DURATION = 24;
    private static final int ANIMATION_FRAMES = 6;

    private final OrbType orbType;
    private final Animation animation;
    private DialogHandler dialogHandler;

    public Orb(Area area, DiscreteCoordinates position, ElementType elementType) {
        super(area, UP, position, elementType);

        orbType = OrbType.fromElementType(elementType);

        final Sprite[] sprites = new Sprite[ANIMATION_FRAMES];
        for (int i = 0; i < ANIMATION_FRAMES; i++) {
            sprites[i] = new RPGSprite("icoop/orb", 1, 1, this ,
                    new RegionOfInterest(i * 32, orbType.getSpriteYDelta(), 32, 32));
        }

        this.animation = new Animation(ANIMATION_DURATION / ANIMATION_FRAMES , sprites);
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        animation.update(deltaTime);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
        ((ICoopInteractionVisitor) v).interactWith((ElementalItem) this, isCellInteraction);
    }

    public void triggerDialog() {
        if (dialogHandler != null)
            dialogHandler.publish(new Dialog(orbType.getDialogPath()));
    }

    public void setDialogHandler(DialogHandler dialogHandler) {
        this.dialogHandler = dialogHandler;
    }

    private enum OrbType {
        FIRE(ElementType.FIRE, "orb_fire_msg", 64),
        WATER(ElementType.WATER, "orb_water_msg", 0),
        NONE(ElementType.NONE, "", -1)
        ;

        private final ElementType elementType;
        private final String dialogPath;
        private final int spriteYDelta;
        OrbType(ElementType type, String dialogPath, int spriteYDelta) {
            this.elementType = type;
            this.dialogPath = dialogPath;
            this.spriteYDelta = spriteYDelta;
        }

        public ElementType getElementType() {
            return elementType;
        }

        public String getDialogPath() {
            return dialogPath;
        }

        public int getSpriteYDelta() {
            return spriteYDelta;
        }

        public static OrbType fromElementType(ElementType elementType) {
            for (OrbType orbType : OrbType.values()) {
                if (orbType.getElementType().equals(elementType))
                    return orbType;
            }

            return NONE;
        }
    }

    public void destroy() {
        getOwnerArea().unregisterActor(this);
    }
}
