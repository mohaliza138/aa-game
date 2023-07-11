package controller;

import javafx.scene.shape.Circle;

public class GameController {
    public static boolean doesCirclesIntersect(Circle circle1, Circle circle2) {
        return Math.pow(circle1.getCenterX() - circle2.getCenterX(), 2)
                + Math.pow(circle1.getCenterY() - circle2.getCenterY(), 2)
                <= Math.pow(circle1.getRadius() + circle2.getRadius(), 2);
    }
}
