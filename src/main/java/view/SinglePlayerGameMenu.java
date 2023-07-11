package view;

import controller.GameController;
import controller.MainController;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SinglePlayerGameMenu extends Application {
    
    public final double centerX = 300;
    public final double mainCircleCenterY = 180;
    private final int shootingHeight = 800;
    public double degree = 0;
    private DifficultyLevel difficultyLevel;
    private final VBox hBox = new VBox();
    private final Text windsDegreeText = new Text(centerX, 50, "");
    private final ProgressBar progressBar = new ProgressBar();
    private Group rotatingBalls = new Group();
    private ShootingBall currentBall;
    private double rotationSpeed;
    private int totalNumberOfBalls;
    private int freezeProcess;
    private Circle freezeProccessCircle;
    private Pane gamePane;
    private Timeline freeze;
    private final Text readyToFreeze = new Text();
    private Timeline role;
    private Timeline phase2;
    private Timeline phase3;
    private Timeline phase4;
    private Timeline timer;
    private final ArrayList<ShootingAnimation> shoots = new ArrayList<>();
    private Text scoreBoard;
    private Text timerText;
    private int currentPhase = 1;
    private boolean lost = false;
    private MediaPlayer mediaPlayer;
    private int trackIndex = 0;
    private boolean isVisible = true;
    private boolean bigBalls = false;
    private boolean saved = false;
    private long time = 0;
    private Scene scene;
    public MediaPlayer getMediaPlayer () {
        return mediaPlayer;
    }
    
    @Override
    public void start (Stage stage) throws Exception {
        playTracks();
        if (!saved) {
            if (MainController.userController.getCurrentUser() != null)
                this.difficultyLevel = MainController.userController.getCurrentUser().getDifficultyLevel();
            else this.difficultyLevel = DifficultyLevel.MEDIUM;
            rotationSpeed = difficultyLevel.rotationSpeed;
            if (MainController.userController.getCurrentUser() != null)
                totalNumberOfBalls = MainController.userController.getCurrentUser().getNumberOfBalls();
            else totalNumberOfBalls = 8;
            gamePane = FXMLLoader.load(SinglePlayerGameMenu.class.getResource("/FXML/GameMenu.fxml"));
            initializeMainCircle(gamePane);
            initializetimer();
            startClock();
            initializeFreezeCircle(gamePane);
            initializeHeadBar(gamePane);
            initializeScoreBoard();
            gamePane.getChildren().add(rotatingBalls);
            currentBall = addShootingBall(gamePane, totalNumberOfBalls, centerX);
            initializeFreezeText();
            loadDefaultSet(StartingSets.values()[((MainController.userController.getCurrentUser() != null) ?
                    MainController.userController.getCurrentUser().getBallSet() : 1) - 1]);
            scene = new Scene(gamePane);
        }
        timer.play();
        stage.setScene(scene);
        stage.centerOnScreen();
        currentBall.requestFocus();
        startRotation();
        stage.show();
    }
    
    private void shoot (Pane pane, ShootingBall ball) {
        Audio.GAME_CLICK.play();
        double x = ball.getCenterX();
        ShootingAnimation shootingAnimation = new ShootingAnimation(pane, ball, degree, rotatingBalls, this);
        shootingAnimation.play();
        shoots.add(shootingAnimation);
        if (ball.number > 1) currentBall = addShootingBall(pane, ball.number - 1, x);
        else pane.requestFocus();
        if (ball.number - 1 == totalNumberOfBalls * 3 / 4 && currentPhase == 1) {
            startPhase2();
            currentPhase = 2;
        }
        if (ball.number - 1 == totalNumberOfBalls / 2 && currentPhase < 3) {
            startPhase3();
            currentPhase = 3;
        }
        else if (ball.number - 1 == totalNumberOfBalls / 4 && currentPhase < 4) {
            currentPhase = 4;
            startPhase4();
            if (MainController.userController.getCurrentUser() == null ||
                    !MainController.userController.getCurrentUser().isbAndW())
                pane.setStyle("-fx-background-color : honeydew;");
        }
        if (freezeProcess == 3) showFreezeText();
        freezeProcess = Math.min(freezeProcess + 1, 4);
        freezeProccessCircle.setRadius((freezeProcess + 1) * 19.4);
    }
    
    public void showFreezeText () {
        gamePane.getChildren().add(readyToFreeze);
    }
    
    public void hideFreezeText () {
        gamePane.getChildren().remove(readyToFreeze);
    }
    
    private void initializeFreezeText () {
        if (MainController.userController.getCurrentUser() != null &&
                MainController.userController.getCurrentUser().isPersian())
            readyToFreeze.setText("آماده\nیخ زن");
        else readyToFreeze.setText("Ready to\nFREEZE!");
        readyToFreeze.setFont(new Font("Comic Sans MS", 20));
        if (MainController.userController.getCurrentUser() != null &&
                MainController.userController.getCurrentUser().isPersian())
            readyToFreeze.setLayoutX(centerX - 25);
        else readyToFreeze.setLayoutX(centerX - 40);
        readyToFreeze.setLayoutY(mainCircleCenterY - 6);
        readyToFreeze.setFill((MainController.userController.getCurrentUser() != null &&
                MainController.userController.getCurrentUser().isbAndW()) ? Color.BLACK : Color.AQUA);
    }
    
    private void freeze () {
        if (freezeProcess != 4) return;
        Audio.FREEZE.play();
        hideFreezeText();
        freezeProcess = 0;
        freezeProccessCircle.setRadius(19.4);
        double temp = rotationSpeed;
        rotationSpeed = 3 * rotationSpeed / Math.abs(rotationSpeed);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run () {
                rotationSpeed = temp;
            }
        }, difficultyLevel.freezeTimer * 1000L);
    }
    
    private void loadByLocation (double x, double y) {
        ShootingBall ball = new ShootingBall(gamePane, 0, (MainController.userController.getCurrentUser() != null &&
                MainController.userController.getCurrentUser().isbAndW()) ? Color.BLACK : Color.CYAN);
        ball.setXOfCenter(x);
        ball.setYOfCenter(y);
        rotatingBalls.getChildren().add(ball);
    }
    
    private void loadDefaultSet (StartingSets set) {
        for (int i = 0; i < 5; i++) {
            loadByLocation(set.getX().get(i), set.getY().get(i));
        }
    }
    
    private void startRotation () {
        role = new Timeline(new KeyFrame(Duration.millis(20), actionEvent -> rotateBasedOnSpeed()));
        role.setCycleCount(-1);
        role.play();
    }
    
    private void rotateBasedOnSpeed () {
        for (Node node : rotatingBalls.getChildren()) {
            if (!(node instanceof ShootingBall)) continue;
            rotateObject(centerX, mainCircleCenterY, (ShootingBall) node, rotationSpeed / 4);
        }
    }
    
    private void initializeMainCircle (Pane gamePane) {
        Circle mainCircle = new Circle();
        mainCircle.setRadius(100);
        mainCircle.setFill((MainController.userController.getCurrentUser() != null &&
                MainController.userController.getCurrentUser().isbAndW()) ? Color.BLACK : Color.OLIVE);
        gamePane.getChildren().add(mainCircle);
        mainCircle.setCenterX(centerX);
        mainCircle.setCenterY(mainCircleCenterY);
    }
    
    private void initializeFreezeCircle (Pane gamePane) {
        freezeProccessCircle = new Circle();
        freezeProccessCircle.setRadius(19.4);
        freezeProccessCircle.setFill((MainController.userController.getCurrentUser() != null &&
                MainController.userController.getCurrentUser().isbAndW()) ? Color.WHITE : Color.ALICEBLUE);
        gamePane.getChildren().add(freezeProccessCircle);
        freezeProccessCircle.setCenterX(centerX);
        freezeProccessCircle.setCenterY(mainCircleCenterY);
    }
    
    private ShootingBall addShootingBall (Pane gamePane, int number, double x) {
        ShootingBall ball = new ShootingBall(gamePane, number, (MainController.userController.getCurrentUser() != null &&
                MainController.userController.getCurrentUser().isbAndW()) ? Color.BLACK : Color.CYAN);
        ball.setXOfCenter(x);
        ball.setYOfCenter(shootingHeight);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), ball);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        ball.requestFocus();
        ball.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case RIGHT: {
                    FadeTransition moveTransition = new FadeTransition(Duration.millis(100), ball);
                    moveTransition.setFromValue(1);
                    moveTransition.setToValue(0);
                    FadeTransition reappear = new FadeTransition(Duration.millis(100), ball);
                    reappear.setFromValue(0);
                    reappear.setToValue(1);
                    moveTransition.setOnFinished(actionEvent -> {
                        currentBall.setXOfCenter(Math.min(currentBall.getCenterX() + 30, 2 * centerX - 30));
                        reappear.play();
                    });
                    moveTransition.play();
                    break;
                }
                case LEFT: {
                    FadeTransition moveTransition = new FadeTransition(Duration.millis(100), ball);
                    moveTransition.setFromValue(1);
                    moveTransition.setToValue(0);
                    FadeTransition reappear = new FadeTransition(Duration.millis(100), ball);
                    reappear.setFromValue(0);
                    reappear.setToValue(1);
                    moveTransition.setOnFinished(actionEvent -> {
                        currentBall.setXOfCenter(Math.max(currentBall.getCenterX() - 30, 30));
                        reappear.play();
                    });
                    moveTransition.play();
                    break;
                }
                case SPACE: {
                    shoot(gamePane, currentBall);
                    break;
                }
                case TAB: {
                    freeze();
                    break;
                }
                case ESCAPE: {
                    pause();
                    break;
                }
            }
            
        });
        return ball;
    }
    
    public void playTracks () {
        Media media = new Media(Objects.requireNonNull(SinglePlayerGameMenu.class.
                getResource("/Audios/SoundTracks/02 A Proper Story.mp3")).toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.seconds(1));
            mediaPlayer.play();
        });
        if (MainController.userController.getCurrentUser() == null ||
                !MainController.userController.getCurrentUser().isMute())mediaPlayer.play();
    }
    
    public void nextTrack () {
        String[] tracks = {"02 A Proper Story.mp3", "06 Twisted Streets.mp3", "20 From Wharf to Wilds.mp3"};
        trackIndex = (trackIndex + 1) % 3;
        Media media = new Media(Objects.requireNonNull(SinglePlayerGameMenu.class.
                getResource("/Audios/SoundTracks/" + tracks[trackIndex])).toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.seconds(1));
            if (MainController.userController.getCurrentUser() == null ||
                    !MainController.userController.getCurrentUser().isMute())mediaPlayer.play();
        });
    }
    
    private void initializeHeadBar (Pane gamePane) {
        windsDegreeText.setFill((MainController.userController.getCurrentUser() != null &&
                MainController.userController.getCurrentUser().isbAndW()) ? Color.BLACK : Color.OLIVE);
        if (MainController.userController.getCurrentUser() != null && MainController.userController.getCurrentUser().isPersian())
            windsDegreeText.setText("جهت باد : " + degree);
        else windsDegreeText.setText("Wind's angle : " + degree);
        windsDegreeText.setFont(new Font("Comic Sans MS", 12));
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(windsDegreeText);
        hBox.setSpacing(3);
        hBox.setLayoutX(centerX - 200);
        progressBar.setProgress(0.5);
        progressBar.setPrefWidth(400);
        if (MainController.userController.getCurrentUser() == null ||
                !MainController.userController.getCurrentUser().isbAndW())hBox.getChildren().add(progressBar);
        gamePane.getChildren().add(hBox);
    }
    
    private void startPhase2 () {
        phase2 = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> {
            if (!lost) {
                growBalls();
                if (checkIntersections()) loose();
            }
        }), new KeyFrame(Duration.seconds(4), actionEvent -> {
            rotationSpeed = -rotationSpeed;
        }));
        phase2.setCycleCount(-1);
        phase2.play();
    }
    
    private void growBalls () {
        for (Node child : rotatingBalls.getChildren()) {
            if (child instanceof ShootingBall) {
                ((ShootingBall) child).changeSize(bigBalls);
            }
        }
        bigBalls = !bigBalls;
    }
    
    public Pane getGamePane () {
        return gamePane;
    }
    
    public boolean checkIntersections () {
        for (Node child : rotatingBalls.getChildren()) {
            if (!(child instanceof ShootingBall)) continue;
            for (Node rotatingBallsChild : rotatingBalls.getChildren()) {
                if (child.equals(rotatingBallsChild) || !(rotatingBallsChild instanceof ShootingBall)) continue;
                if (GameController.doesCirclesIntersect((Circle) child, (Circle) rotatingBallsChild)) {
                    LooseAnimation looseAnimation = new LooseAnimation(this, (ShootingBall) child,
                            (ShootingBall) rotatingBallsChild);
                    looseAnimation.play();
                    return true;
                }
            }
        }
        return false;
    }
    
    private void startPhase3 () {
        phase3 = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> changeVisibility()));
        phase3.setCycleCount(-1);
        phase3.play();
    }
    
    private void changeVisibility () {
        isVisible = !isVisible;
        for (Node child : rotatingBalls.getChildren()) {
            child.setVisible(isVisible);
        }
    }
    
    private void startPhase4 () {
        phase4 = new Timeline(new KeyFrame(Duration.seconds(5), actionEvent -> changeDegreeRandomly()));
        phase4.setCycleCount(-1);
        phase4.play();
    }
    
    private void pause () {
        for (ShootingAnimation shoot : shoots) {
            shoot.pause();
        }
        timer.pause();
        mediaPlayer.pause();
        gamePane.requestFocus();
        role.pause();
        if (phase2 != null) phase2.pause();
        if (phase3 != null) phase3.pause();
        if (phase4 != null) phase4.pause();
        Stage pause = new Stage();
        PauseMenu pauseMenu = new PauseMenu(this, pause);
        try {
            pauseMenu.start(pause);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void resume () {
        for (ShootingAnimation shoot : shoots) {
            shoot.play();
        }
        timer.play();
        if (MainController.userController.getCurrentUser() == null ||
                !MainController.userController.getCurrentUser().isMute()) mediaPlayer.play();
        currentBall.requestFocus();
        role.play();
        if (phase2 != null) phase2.play();
        if (phase3 != null) phase3.play();
        if (phase4 != null) phase4.play();
    }
    
    private void changeDegreeRandomly () {
        if (Main.random.nextInt(2) == 1) {
            degree = (degree + difficultyLevel.windSpeed > 15) ? 15 : degree + difficultyLevel.windSpeed;
        } else {
            degree = (degree - difficultyLevel.windSpeed < -15) ? -15 : degree - difficultyLevel.windSpeed;
        }
        degree = ((double) Math.round(degree * 10)) / 10;
        if (MainController.userController.getCurrentUser() != null && MainController.userController.getCurrentUser().isPersian())
            windsDegreeText.setText("جهت باد : " + degree);
        else windsDegreeText.setText("Wind's degree : " + degree);
        progressBar.setProgress((degree + 15) / 30);
    }
    
    public Group getRotatingBalls () {
        return rotatingBalls;
    }
    
    public void setRotatingBalls (Group rotatingBalls) {
        this.rotatingBalls = rotatingBalls;
    }
    
    public void rotateObject (double x, double y, ShootingBall ball, double degree) {
        double xFromCenter = ball.getCenterX() - x;
        double yFromCenter = y - ball.getCenterY();
        double distance = 110;
        double currentAngle = Math.atan(yFromCenter / xFromCenter);
        if (xFromCenter == 0) currentAngle = (yFromCenter > 0) ? Math.PI / 2 : -Math.PI / 2;
        if (xFromCenter < 0) currentAngle += Math.PI;
        currentAngle += Math.toRadians(degree);
        ball.setXOfCenter(Math.cos(currentAngle) * distance + x);
        ball.setYOfCenter(y - Math.sin(currentAngle) * distance);
    }
    
    public void loose () {
        lost = true;
        if (phase3 != null) phase3.stop();
        Audio.CLICK_ERROR.play();
        Text looseText = new Text();
        if (MainController.userController.getCurrentUser() != null && MainController.userController.getCurrentUser().isPersian())
            looseText.setText("باختی");
        else looseText.setText("Game over!");
        looseText.setFont(new Font("Comic Sans MS", 50));
        looseText.setFill(Color.WHITE);
        if (MainController.userController.getCurrentUser() != null && MainController.userController.getCurrentUser().isPersian())
            looseText.setLayoutX(centerX - 60);
        else looseText.setLayoutX(centerX - 130);
        looseText.setLayoutY(400);
        gamePane.getChildren().add(looseText);
        looseText.requestFocus();
        createLeaderboardButton();
    }
    
    public void win () {
        if (!isVisible) changeVisibility();
        Audio.WIN.play();
        winAnimation();
        if (phase3 != null) phase3.stop();
        Text winText = new Text();
        if (MainController.userController.getCurrentUser() != null && MainController.userController.getCurrentUser().isPersian())
            winText.setText("پیروزی");
        else winText.setText("You won!");
        winText.setFont(new Font("Comic Sans MS", 50));
        winText.setFill((MainController.userController.getCurrentUser() != null &&
                MainController.userController.getCurrentUser().isbAndW()) ? Color.BLACK : Color.FORESTGREEN);
        if (MainController.userController.getCurrentUser() != null && MainController.userController.getCurrentUser().isPersian())
            winText.setLayoutX(centerX - 70);
        else winText.setLayoutX(centerX - 90);
        winText.setLayoutY(400);
        gamePane.getChildren().add(winText);
        winText.requestFocus();
        createLeaderboardButton();
        scoring();
    }
    
    private void scoring () {
        int increment = totalNumberOfBalls * difficultyLevel.rotationSpeed / 5;
        if (MainController.userController.getCurrentUser() != null) {
            MainController.userController.getCurrentUser().setScore(MainController.userController.getCurrentUser()
                    .getScore() + increment);
            MainController.userController.getCurrentUser().setFinishTime(time);
        }
    }
    
    private void initializeScoreBoard () {
        scoreBoard = new Text();
        scoreBoard.setText("0");
        scoreBoard.setFont(new Font("Comic Sans MS", 50));
        scoreBoard.setFill((MainController.userController.getCurrentUser() != null &&
                MainController.userController.getCurrentUser().isbAndW()) ? Color.BLACK : Color.OLIVE);
        scoreBoard.setLayoutX(centerX - 15);
        scoreBoard.setLayoutY(600);
        gamePane.getChildren().add(scoreBoard);
    }
    
    private void initializetimer () {
        timerText = new Text();
        timerText.setText("00:00");
        timerText.setFont(new Font("Comic Sans MS", 50));
        timerText.setFill((MainController.userController.getCurrentUser() != null &&
                MainController.userController.getCurrentUser().isbAndW()) ? Color.BLACK : Color.OLIVE);
        timerText.setLayoutX(centerX - 60);
        timerText.setLayoutY(700);
        gamePane.getChildren().add(timerText);
    }
    
    private void startClock () {
        timer = new Timeline(new KeyFrame(Duration.millis(10), actionEvent -> {
            time += 1;
            timerText.setText(time / 100 + ":" + time % 100);
        }));
        timer.setCycleCount(-1);
    }
    
    private void terminate () {
        MainController.userController.saveUsers();
        mediaPlayer.stop();
        role.stop();
        timer.stop();
        phase4.stop();
    }
    
    private void winAnimation () {
        rotationSpeed = 100;
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(800), freezeProccessCircle);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setFromZ(1);
        scaleTransition.setToX(0);
        scaleTransition.setToY(0);
        scaleTransition.setToZ(0);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(400), readyToFreeze);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        FadeTransition windTextFade = new FadeTransition(Duration.millis(400), windsDegreeText);
        windTextFade.setFromValue(1);
        windTextFade.setToValue(0);
        FadeTransition windBar = new FadeTransition(Duration.millis(400), progressBar);
        windBar.setFromValue(1);
        windBar.setToValue(0);
        scaleTransition.play();
        fadeTransition.play();
        windTextFade.play();
        windBar.play();
        role.stop();
        timer.stop();
        startRotation();
    }
    
    private void createLeaderboardButton () {
        javafx.scene.control.Button toLeaderBoard;
        if (MainController.userController.getCurrentUser() != null && MainController.userController.getCurrentUser().isPersian())
            toLeaderBoard = new Button("رفتن به جدول امتیازات");
        else if (MainController.userController.getCurrentUser() == null)
            toLeaderBoard = new Button("Proceed to start menu");
        else toLeaderBoard = new Button("Proceed to leaderboard");
        if (MainController.userController.getCurrentUser() != null &&
                MainController.userController.getCurrentUser().isbAndW()) toLeaderBoard.setStyle("-fx-text-fill: black;");
        gamePane.getChildren().add(toLeaderBoard);
        toLeaderBoard.setLayoutX(centerX - 150);
        toLeaderBoard.setLayoutY(450);
        toLeaderBoard.setPrefWidth(300);
        toLeaderBoard.setOnMouseClicked(mouseEvent -> {
            Audio.POSITIVE_CLICK_SOUND.play();
            try {
                if (MainController.userController.getCurrentUser() != null) Main.startMenu(Messages.LEADERBOARD);
                else Main.startMenu(Messages.START);
                terminate();
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }
    
    public void save() {
        saved = true;
        Main.setCurrentGame(this);
    }
    
    public int getTotalNumberOfBalls () {
        return totalNumberOfBalls;
    }
    
    public DifficultyLevel getDifficultyLevel () {
        return difficultyLevel;
    }
    
    public void setScoreBoard (int score) {
        scoreBoard.setText("" + score);
        scoreBoard.setLayoutX(centerX - scoreBoard.getText().length() * 15);
    }
    
    public void stopTrack () {
        mediaPlayer.stop();
    }
}