package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.ElementalEntity;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.window.Window;

public class ICoopBehavior extends AreaBehavior {
    public ICoopBehavior(Window window, String title) {
        super(window, title);
        int height = getHeight();
        int width = getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ICoopCellType color = ICoopCellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new ICoopCell(x, y, color));
            }
        }
    }
    public enum ICoopCellType {
        //https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        NULL(0, false , false),
        WALL(-16777216, false , false),
        IMPASSABLE (-8750470, false , true),
        INTERACT(-256, true , true),
        DOOR(-195580, true , true),
        WALKABLE(-1, true , true),
        ROCK(-16777204, true , true),
        OBSTACLE (-16723187, true , true)
        ;

        final int type;
        final boolean isWalkable;

        ICoopCellType(int type, boolean isWalkable, boolean canFly) {
            this.type = type;
            this.isWalkable = isWalkable;
        }

        public static ICoopCellType toType(int type) {
            for (ICoopCellType ict : ICoopCellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type
            // System.out.println(type);
            return NULL;
        }
    }

    public class ICoopCell extends Cell {
        /// Type of the cell following the enum
        private final ICoopCellType type;

        /**
         * Default Tuto2Cell Constructor
         *
         * @param x    (int): x coordinate of the cell
         * @param y    (int): y coordinate of the cell
         * @param type (EnigmeCellType), not null
         */
        public ICoopCell(int x, int y, ICoopCellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) { return true; }

        @Override
        protected boolean canEnter(Interactable entity) {
            // FIXME: Very rudimentary implementation of elemental category role
            // FIXME: Allows player to phase through another player
            String element = null;
            for (Interactable e : entities) {
                if (e instanceof ElementalEntity)
                    element = ((ElementalEntity) e).element();
            }

            if (element != null && entity instanceof ElementalEntity)
                return element.equals(((ElementalEntity) entity).element());
            else
                return type.isWalkable && !(this.takeCellSpace());
        }

        @Override
        public boolean takeCellSpace() {
            for(Interactable entity : entities)
                return entity.takeCellSpace();

            return false;
        }

        @Override
        public boolean isCellInteractable() { return true; }

        @Override
        public boolean isViewInteractable() { return false; }

        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {}
    }
}
