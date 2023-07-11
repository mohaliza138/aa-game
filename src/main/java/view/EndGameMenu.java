package view;

import javafx.application.Application;
import javafx.stage.Stage;

public class EndGameMenu extends Application {
    private static boolean wonFlag;
    
    @Override
    public void start (Stage stage) throws Exception {
    
    }
    
    public EndGameMenu (boolean won) {
        wonFlag = won;
    }
}
