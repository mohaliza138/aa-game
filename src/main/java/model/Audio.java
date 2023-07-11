package model;

import controller.MainController;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import view.StarterMenuGraphicalController;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public enum Audio {
    CLICK_ERROR("/Audios/mixkit-click-error-1110.wav"),
    POSITIVE_CLICK_SOUND("/Audios/mixkit-quick-positive-video-game-notification-interface-265.wav"),
    GAME_CLICK("/Audios/mixkit-game-click-1114.wav"),
//    OPEN_SOFTWARE("/Audios/mixkit-opening-software-interface-2578.wav"),
//    POSITIVE_SOUND("/Audios/mixkit-positive-interface-beep-221.wav"),
    WIN("/Audios/mixkit-quick-win-video-game-notification-269.wav"),
    FREEZE("/Audios/ice-cracking-field-recording-06-139709.mp3")
    ;
    private final MediaPlayer mediaPlayer;
    
    private Audio (String path) {
        Media media = new Media(getClass().getResource(path).toString());
        mediaPlayer = new MediaPlayer(media);
//        try {
//            file = new File(StarterMenuGraphicalController.class.getResource(path).toURI());
//        } catch (Exception ignored) {
//        }
    }
    
    public void play () {
        if (MainController.userController.getCurrentUser() == null ||
                !MainController.userController.getCurrentUser().isMute()){
            mediaPlayer.seek(Duration.millis(0));
            mediaPlayer.play();
        }
    }
    
    public void play (int repeatCount) {
        for (int i = 0; i < repeatCount; i++) {
            play();
        }
    }
}
