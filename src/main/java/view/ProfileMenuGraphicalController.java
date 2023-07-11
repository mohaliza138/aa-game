package view;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import model.Audio;
import model.Messages;
import model.Validations;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileMenuGraphicalController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private ImageView currentProfile;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentProfile.setImage(MainController.userController.getCurrentUser().getProfileImage());
        username.setText(MainController.userController.getCurrentUser().getUsername());
    }
    
    
    public void setDefaultMaleImage (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        MainController.userController.getCurrentUser().
                setProfileImageAddress(ProfileMenuGraphicalController.class.
                getResource("/Images/Default-male.jpg").toString());
        currentProfile.setImage(((ImageView) mouseEvent.getSource()).getImage());
        MainController.userController.saveUsers();
    }
    
    public void setDefaultFemaleImage (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        MainController.userController.getCurrentUser().
                setProfileImageAddress(ProfileMenuGraphicalController.class.
                getResource("/Images/Default-female.jpg").toString());
        currentProfile.setImage(((ImageView) mouseEvent.getSource()).getImage());
        MainController.userController.saveUsers();
    }
    
    public void setCustomImage (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(currentProfile.getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            if (image.getWidth() != image.getHeight()) {
                Main.alert(Alert.AlertType.NONE, Messages.INVALID_IMAGE_SCALE);
            } else {
                currentProfile.setImage(image);
                MainController.userController.getCurrentUser().setProfileImageAddress(selectedFile.toURI().toString());
                MainController.userController.saveUsers();
            }
        }
    }
    
    public void saveButton (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        if (!MainController.userController.getCurrentUser().getUsername().equals(username.getText()) &&
                MainController.userController.getUserByUsername(username.getText()) != null) {
            Main.alert(Alert.AlertType.NONE, Messages.USED_USERNAME);
            return;
        }
        if (!password.getText().isEmpty() &&
                !Validations.PASSWORD.check(password.getText())) {
            Main.alert(Alert.AlertType.NONE, Messages.INVALID_PASSWORD);
            return;
        }
        if (!username.getText().isEmpty()) MainController.userController.getCurrentUser().setUsername(username.getText());
        if (!password.getText().isEmpty()) MainController.userController.getCurrentUser().setPassword(password.getText());
        MainController.userController.saveUsers();
        try {
            Main.startMenu(Messages.MAIN_MENU);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void logout (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        MainController.userController.setCurrentUser(null);
        MainController.userController.saveCurrentUser();
        try {
            Main.startMenu(Messages.START);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void deleteAccount (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        MainController.userController.removeCurrentUser();
        try {
            Main.startMenu(Messages.START);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
