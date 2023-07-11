package view;

import javafx.scene.input.MouseEvent;
import model.Audio;
import model.Messages;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class StarterMenuGraphicalController {
    public void clickSound (MouseEvent mouseEvent) {
        Audio.CLICK_ERROR.play();
    }
    
    public void signUpUser (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        try {
            Main.startMenu(Messages.SIGN_UP);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void signInUser (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        try {
            Main.startMenu(Messages.SIGN_IN);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void playAsGuest (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        SinglePlayerGameMenu game = new SinglePlayerGameMenu();
        Main.playGame(game);
    }
    
    public void playPvP (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        PvPGameMenu game = new PvPGameMenu();
        Main.playGame(game);
    }
}
