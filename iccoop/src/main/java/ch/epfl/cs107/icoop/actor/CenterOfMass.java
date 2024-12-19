package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.engine.actor.Actor;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;

/**
 * Represents the center of mass of a group of actors.
 * Computes the average position and velocity of the provided actors.
 */
public class CenterOfMass implements Actor {

    /**
     * Array of actors contributing to the center of mass
     */
    private final Actor[] actors;

    /**
     * Constructs a CenterOfMass object with one or more actors.
     *
     * @param actor        (Actor) The first actor. Not null.
     * @param restOfActors (Actor...) The remaining actors. Can be empty.
     */
    public CenterOfMass(Actor actor, Actor... restOfActors) {
        this.actors = new Actor[restOfActors.length + 1];
        actors[0] = actor;
        System.arraycopy(restOfActors, 0, this.actors, 1, restOfActors.length);
    }

    /**
     * Computes the average position of all actors.
     *
     * @return (Vector) The center of mass position.
     */
    @Override
    public Vector getPosition() {
        Vector position = Vector.ZERO;
        for (Actor actor : actors) {
            position = position.add(actor.getPosition());
        }
        return position.mul(1f / actors.length);
    }

    /**
     * Computes the transform of the center of mass, based on its position.
     *
     * @return (Transform) The transform representing the center of mass.
     */
    @Override
    public Transform getTransform() {
        return Transform.I.translated(getPosition());
    }

    /**
     * Computes the average velocity of all actors.
     *
     * @return (Vector) The average velocity of the actors.
     */
    @Override
    public Vector getVelocity() {
        Vector velocity = Vector.ZERO;
        for (Actor actor : actors) {
            velocity = velocity.add(actor.getVelocity());
        }
        return velocity.mul(1f / actors.length);
    }
}
