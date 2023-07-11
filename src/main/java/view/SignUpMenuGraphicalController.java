package view;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Audio;
import model.Messages;

public class SignUpMenuGraphicalController {
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;
    
    public void backToStarterMenu (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        try {
            Main.startMenu(Messages.START);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void signUpUser (MouseEvent mouseEvent) {
        Messages response;
        if (username.getText().isEmpty() || password.getText().isEmpty()) response = Messages.EMPTY_FIELD;
        else response = MainController.userController.SignUp(username.getText(), password.getText());
        Main.alert(Alert.AlertType.NONE, response);
        if (response.equals(Messages.SUCCESS)){
            try {
                Audio.POSITIVE_CLICK_SOUND.play();
                Main.startMenu(Messages.MAIN_MENU);
            } catch (Exception e) {
                System.out.println(e);
            }
            Audio.POSITIVE_CLICK_SOUND.play();
        }
        else {
            if (response.equals(Messages.USED_USERNAME)) username.setText("");
            else if (response.equals(Messages.INVALID_PASSWORD)) password.setText("");
            Audio.CLICK_ERROR.play();
        }
    }
}
