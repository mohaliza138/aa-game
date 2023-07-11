package view;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

public class LeaderboardMenu extends Application {
    
    
    @Override
    public void start (Stage stage) throws Exception {
        stage.close();
        URL url = LeaderboardMenu.class.getResource("/FXML/LeaderboardMenu.fxml");
        Pane pane = FXMLLoader.load(url);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }
}
