package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import model.Audio;
import model.Messages;

public class MainMenuGraphicalController {
    public Button continueButton;
    
    @FXML
    public void initialize() {
        continueButton.setDisable(Main.getCurrentGame() == null);
    }
    public void enterProfileMenu (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        try {
            Main.startMenu(Messages.PROFILE_MENU);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void enterLeadeboardMenu (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        try {
            Main.startMenu(Messages.LEADERBOARD);
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
    public void exitGame (MouseEvent mouseEvent) {
        Main.endProgram();
    }
    
    public void enterSettings (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        try {
            Main.startMenu(Messages.SETTINGS_MENU);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void newGame (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        SinglePlayerGameMenu game = new SinglePlayerGameMenu();
        Main.playGame(game);
    }
    
    public void pvpGame (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        PvPGameMenu game = new PvPGameMenu();
        Main.playGame(game);
    }
    
    public void continueGame (MouseEvent mouseEvent) {
        Main.playGame(Main.getCurrentGame());
        Main.setCurrentGame(null);
    }
}
