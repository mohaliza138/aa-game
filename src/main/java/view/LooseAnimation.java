package view;

import controller.MainController;
import javafx.animation.Transition;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.ShootingBall;

public class LooseAnimation extends Transition {
    private final SinglePlayerGameMenu game;
    private final ShootingBall ball1;
    private final ShootingBall ball2;
    
    public LooseAnimation(SinglePlayerGameMenu game, ShootingBall first, ShootingBall second) {
        this.game = game;
        ball1 = first;
        ball2 = second;
        this.setCycleDuration(Duration.millis(10));
        this.setCycleCount(-1);
    }
    @Override
    protected void interpolate (double v) {
        ball1.setVisible(true);
        ball2.setVisible(true);
        for (Node child : game.getGamePane().getChildren()) {
            if ((child instanceof ShootingBall) && !child.equals(ball1) && !child.equals(ball2)) {
                ((ShootingBall) child).setVisibility(false);
            }
        }
        for (Node child : game.getRotatingBalls().getChildren()) {
            if ((child instanceof ShootingBall) && !child.equals(ball1) && !child.equals(ball2)) {
                ((ShootingBall) child).setVisibility(false);
            }
        }
        if (!ball1.getFill().equals(Color.RED)) {
            ball1.setFill((MainController.userController.getCurrentUser() != null &&
                    MainController.userController.getCurrentUser().isbAndW()) ? Color.BLACK : Color.RED);
            ball2.setFill((MainController.userController.getCurrentUser() != null &&
                    MainController.userController.getCurrentUser().isbAndW()) ? Color.BLACK : Color.RED);
            ball1.getText().setFill((MainController.userController.getCurrentUser() != null &&
                    MainController.userController.getCurrentUser().isbAndW()) ? Color.BLACK : Color.RED);
            ball2.getText().setFill((MainController.userController.getCurrentUser() != null &&
                    MainController.userController.getCurrentUser().isbAndW()) ? Color.BLACK : Color.RED);
        }
        game.hideFreezeText();
        if (ball1.getRadius() > 1500) this.stop();
        ball1.setRadius(ball1.getRadius() + 5);
        ball2.setRadius(ball1.getRadius());
    }
    
    private boolean doesCirclesIntersect(Circle circle1, Circle circle2) {
        return Math.pow(circle1.getCenterX() - circle2.getCenterX(), 2)
                + Math.pow(circle1.getCenterY() - circle2.getCenterY(), 2)
                <= Math.pow(circle1.getRadius() + circle2.getRadius(), 2);
    }
}
