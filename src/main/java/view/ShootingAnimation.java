package view;

import controller.GameController;
import javafx.animation.Transition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.ShootingBall;

public class ShootingAnimation extends Transition {
    Pane pane;
    ShootingBall ball;
    double degree;
    Group allBalls;
    SinglePlayerGameMenu game;
    
    public ShootingAnimation(Pane pane, ShootingBall shootingBall, double degree, Group group, SinglePlayerGameMenu game) {
        this.pane = pane;
        this.ball = shootingBall;
        this.degree = degree;
        this.allBalls = group;
        this.game = game;
        this.setCycleDuration(Duration.millis(50));
        this.setCycleCount(-1);
    }
    @Override
    protected void interpolate (double v) {
        Circle mainCircle = (Circle) pane.getChildren().get(0);
        if (GameController.doesCirclesIntersect(mainCircle, ball)) this.stop();
        ball.setYOfCenter(ball.getCenterY() - 10);
        ball.setXOfCenter(ball.getCenterX() + 10 * Math.tan(Math.toRadians(degree)));
        if (GameController.doesCirclesIntersect(mainCircle, ball)) {
            try {
                allBalls.getChildren().addAll(ball, ball.getText());
                for (Node node : allBalls.getChildren()) {
                    if (!(node instanceof ShootingBall) || ball.equals(node)) continue;
                    if (GameController.doesCirclesIntersect(ball, (ShootingBall) node)) {
//                        ((ShootingBall) node).setFill(Color.RED);
//                        ball.setFill(Color.RED);
                        LooseAnimation animation = new LooseAnimation(game, ball, (ShootingBall) node);
                        animation.play();
                        this.stop();
                        game.loose();
                        return;
                    }
                }
                game.setScoreBoard((game.getTotalNumberOfBalls() - ball.number + 1) * game.getDifficultyLevel().rotationSpeed / 5);
                if (ball.number == 1) game.win();
            } catch (Exception ignored) {
            }
            this.stop();
        }
        if (ball.getCenterY() < 30 || ball.getCenterX() < 30 || ball.getCenterX() > 570) {
            LooseAnimation animation = new LooseAnimation(game, ball, ball);
            animation.play();
            this.stop();
            game.loose();
        }
    }
    
}
