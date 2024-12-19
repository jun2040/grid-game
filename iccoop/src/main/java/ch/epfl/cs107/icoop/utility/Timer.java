package ch.epfl.cs107.icoop.utility;

/**
 * Utility class representing a timer, useful for managing timed events and countdowns.
 */
public class Timer {

    /** The current time of the timer. */
    private float timer;

    /**
     * Constructs a timer with an initial value of zero.
     */
    public Timer() {
        this.timer = 0f;
    }

    /**
     * Constructs a timer with a specified initial time.
     *
     * @param time (float): Initial time for the timer in seconds.
     */
    public Timer(float time) {
        this.timer = time;
    }

    /**
     * Updates the timer by decrementing it with the elapsed time.
     *
     * @param deltaTime (float): The time elapsed since the last update, in seconds.
     */
    public void update(float deltaTime) {
        this.timer -= deltaTime;
    }

    /**
     * Checks if the timer has completed (reached zero or below).
     *
     * @return (boolean): True if the timer has completed; false otherwise.
     */
    public boolean isCompleted() {
        return timer <= 0;
    }

    /**
     * Retrieves the current time of the timer.
     *
     * @return (float): The remaining time on the timer, in seconds.
     */
    public float getTime() {
        return timer;
    }

    /**
     * Sets the timer to a specified value.
     *
     * @param time (float): The new time for the timer, in seconds.
     */
    public void setTimer(float time) {
        this.timer = time;
    }
}
