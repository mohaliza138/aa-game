package view;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Audio;
import model.Messages;

public class PauseMenu extends Application {
    private Stage stage;
    private final SinglePlayerGameMenu game;
    public PauseMenu (SinglePlayerGameMenu game, Stage stage) {
        this.game = game;
        this.stage = stage;
    }
    @Override
    public void start (Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(PauseMenu.class.getResource("/FXML/Pause.fxml"));
        loader.setControllerFactory(c -> new PauseMenu(game, stage));
        Pane pausePane = loader.load();
        stage.setScene(new Scene(pausePane));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Pause");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    
    public void resume (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        game.resume();
        stage.close();
    }
    
    public void restartGame (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        SinglePlayerGameMenu game = new SinglePlayerGameMenu();
        Main.playGame(game);
        game.stopTrack();
        stage.close();
    }
    
    public void quitGame () {
        Audio.POSITIVE_CLICK_SOUND.play();
        game.stopTrack();
        try {
            if (MainController.userController.getCurrentUser() != null) Main.startMenu(Messages.MAIN_MENU);
            else Main.startMenu(Messages.START);
            stage.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void nextTrack (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        game.nextTrack();
    }
    
    public void saveGame (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        game.save();
    }
    
    public void quitGame (MouseEvent mouseEvent) {
        try {
            Main.startMenu(Messages.MAIN_MENU);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
