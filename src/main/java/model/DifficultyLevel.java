package model;

import java.util.Random;

public enum DifficultyLevel {
    EASY(5, 1.2, 7),
    MEDIUM(10, 1.5, 5),
    HARD(15, 1.8, 3);
    public final int rotationSpeed;
    public final double windSpeed;
    public final int freezeTimer;
    private DifficultyLevel (int rotationSpeed, double windSpeed, int freezeTimer) {
        this.rotationSpeed = rotationSpeed;
        this.windSpeed = windSpeed;
        this.freezeTimer = freezeTimer;
    }
}
