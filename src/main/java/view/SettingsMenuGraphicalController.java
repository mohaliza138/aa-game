package view;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import model.Audio;
import model.DifficultyLevel;
import model.Messages;

public class SettingsMenuGraphicalController {
    
    public RadioButton easyRadio;
    public RadioButton mediumRadio;
    public RadioButton hardRadio;
    public ToggleButton muteButton;
    public ToggleButton bAndW;
    public ToggleButton persian;
    public Slider ballCountSlider;
    public RadioButton set1;
    public RadioButton set2;
    public RadioButton set3;
    
    @FXML
    public void initialize() {
        switch (MainController.userController.getCurrentUser().getDifficultyLevel()) {
//        switch (DifficultyLevel.HARD) {
            case EASY -> easyRadio.setSelected(true);
            case MEDIUM -> mediumRadio.setSelected(true);
            case HARD -> hardRadio.setSelected(true);
        }
        switch (MainController.userController.getCurrentUser().getBallSet()) {
            case 1 -> set1.setSelected(true);
            case 2 -> set2.setSelected(true);
            case 3 -> set3.setSelected(true);
        }
        ballCountSlider.setValue(MainController.userController.getCurrentUser().getNumberOfBalls());
        muteButton.setSelected(MainController.userController.getCurrentUser().isMute());
        bAndW.setSelected(MainController.userController.getCurrentUser().isbAndW());
        persian.setSelected(MainController.userController.getCurrentUser().isPersian());
    }
    
    public void setDifficulty (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        if (easyRadio.isSelected()) MainController.userController.getCurrentUser().setDifficultyLevel(DifficultyLevel.EASY);
        else if (mediumRadio.isSelected()) MainController.userController.getCurrentUser().setDifficultyLevel(DifficultyLevel.MEDIUM);
        else if (hardRadio.isSelected()) MainController.userController.getCurrentUser().setDifficultyLevel(DifficultyLevel.HARD);
        MainController.userController.saveUsers();
    }
    
    public void muteAction (MouseEvent mouseEvent) {
        MainController.userController.getCurrentUser().setMute(((ToggleButton)mouseEvent.getSource()).isSelected());
        MainController.userController.saveUsers();
    }
    
    public void bAndWAction (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        MainController.userController.getCurrentUser().setbAndW(((ToggleButton)mouseEvent.getSource()).isSelected());
        MainController.userController.saveUsers();
    }
    
    public void persianAction (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        MainController.userController.getCurrentUser().setPersian(((ToggleButton)mouseEvent.getSource()).isSelected());
        MainController.userController.saveUsers();
    }
    
    public void backToMainMenu (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        MainController.userController.getCurrentUser().setNumberOfBalls((int)(ballCountSlider.getValue()));
        MainController.userController.saveUsers();
        try {
            Main.startMenu(Messages.MAIN_MENU);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void setSet (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        if (mouseEvent.getSource().equals(set1)) MainController.userController.getCurrentUser().setBallSet(1);
        else if (mouseEvent.getSource().equals(set2)) MainController.userController.getCurrentUser().setBallSet(2);
        else MainController.userController.getCurrentUser().setBallSet(3);
    }
}
