package view;

import controller.MainController;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Audio;
import model.Messages;

import java.util.Random;

public class Main extends Application {
    private static Stage stage;
    private static final StarterMenu starterMenu = new StarterMenu();
    private static final SignInMenu signInMenu = new SignInMenu();
    private static final SignUpMenu signUpMenu = new SignUpMenu();
    private static final MainMenu mainMenu = new MainMenu();
    private static final ProfileMenu profileMenu = new ProfileMenu();
    private static final SettingsMenu settingsMenu = new SettingsMenu();
    private static final LeaderboardMenu leaderboardMenu = new LeaderboardMenu();
    public static final Random random = new Random();
    private static SinglePlayerGameMenu currentGame;
    
    public static void main (String[] args) {
        launch(args);
    }
    
    @Override
    public void start (Stage stage) throws Exception {
        Main.stage = stage;
//        Audio.POSITIVE_SOUND.play();
        stage.getIcons().add(new Image(Main.class.getResource("/Images/mainIcon.png").toString()));
        if (MainController.userController.getCurrentUser() != null) mainMenu.start(stage);
        else starterMenu.start(stage);
    }
    
    public static void startMenu (Messages message) throws Exception {
        switch (message) {
            case START -> starterMenu.start(stage);
            case SIGN_IN -> signInMenu.start(stage);
            case SIGN_UP -> signUpMenu.start(stage);
            case MAIN_MENU -> mainMenu.start(stage);
            case PROFILE_MENU -> profileMenu.start(stage);
            case LEADERBOARD -> leaderboardMenu.start(stage);
            case SETTINGS_MENU -> settingsMenu.start(stage);
        }
    }
    
    public static void alert (Alert.AlertType type, Messages message) {
        Alert alert = new Alert(type);
        if (message.equals(Messages.SUCCESS)) alert.setHeaderText("Success!");
        else alert.setHeaderText("Failed!");
        alert.setContentText(message.text);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.initStyle(StageStyle.DECORATED);
        alert.show();
    }
    
    public static void playGame(SinglePlayerGameMenu game) {
        try {
            game.start(stage);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void playGame(PvPGameMenu game) {
        try {
            game.start(stage);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static SinglePlayerGameMenu getCurrentGame () {
        return currentGame;
    }
    
    public static void setCurrentGame (SinglePlayerGameMenu currentGame) {
        Main.currentGame = currentGame;
    }
    
    public static void endProgram () {
        stage.close();
    }
}
