package view;

import controller.GameController;
import javafx.animation.Transition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.ShootingBall;

public class PvPShoot extends Transition {
    Pane pane;
    ShootingBall ball;
    double degree;
    Group allBalls;
    PvPGameMenu game;
    boolean upward;
    
    public PvPShoot(Pane pane, ShootingBall shootingBall, double degree, Group group, PvPGameMenu game, boolean upward) {
        this.pane = pane;
        this.ball = shootingBall;
        this.degree = degree;
        this.allBalls = group;
        this.game = game;
        this.upward = upward;
        this.setCycleDuration(Duration.millis(50));
        this.setCycleCount(-1);
    }
    @Override
    protected void interpolate (double v) {
        Circle mainCircle = (Circle) pane.getChildren().get(0);
        if (GameController.doesCirclesIntersect(mainCircle, ball)) this.stop();
        if (upward) ball.setYOfCenter(ball.getCenterY() - 10);
        else ball.setYOfCenter(ball.getCenterY() + 10);
        ball.setXOfCenter(ball.getCenterX() + 10 * Math.tan(Math.toRadians(degree)));
        if (GameController.doesCirclesIntersect(mainCircle, ball)) {
            try {
                allBalls.getChildren().addAll(ball, ball.getText());
                for (Node node : allBalls.getChildren()) {
                    if (!(node instanceof ShootingBall) || ball.equals(node)) continue;
                    if (GameController.doesCirclesIntersect(ball, (ShootingBall) node)) {
//                        ((ShootingBall) node).setFill(Color.RED);
//                        ball.setFill(Color.RED);
                        PvPLoose animation = new PvPLoose(game, ball, (ShootingBall) node);
                        animation.play();
                        this.stop();
                        game.loose();
                        return;
                    }
                }
                if (ball.number == 1) game.win();
            } catch (Exception ignored) {
            }
            this.stop();
        }
        if (ball.getCenterY() < 30 || ball.getCenterY() > 870 || ball.getCenterX() < 30 || ball.getCenterX() > 570) {
            PvPLoose animation = new PvPLoose(game, ball, ball);
            animation.play();
            this.stop();
            game.loose();
        }
    }
    
}
