package ch.epfl.cs107.icoop.utility;

public class Timer {
    private float timer;

    public Timer() {
        this.timer = 0f;
    }

    public Timer(float time) {
        this.timer = time;
    }

    public void update(float deltaTime) {
        this.timer -= deltaTime;
    }

    public boolean isCompleted() {
        return timer <= 0;
    }

    public float getTime() {
        return timer;
    }

    public void setTimer(float time) {
        this.timer = time;
    }
}
