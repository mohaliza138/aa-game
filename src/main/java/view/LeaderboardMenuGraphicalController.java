package view;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import model.Audio;
import model.Messages;
import model.User;

import java.util.ArrayList;
import java.util.Comparator;

public class LeaderboardMenuGraphicalController {
    
    public Label player1;
    public Label player2;
    public Label player3;
    public Label otherPlayers;
    @FXML
    public void initialize() {
        ArrayList<User> users = MainController.userController.getUsers();
        users.sort(new Comparator<User>() {
            @Override
            public int compare (User o1, User o2) {
                if (o1.getScore() != o2.getScore()) return o2.getScore() - o1.getScore();
                return (int)(o1.getFinishTime() - o2.getFinishTime());
            }
        });
        initializeLabels(users);
    }
    
    public void backToMain (MouseEvent mouseEvent) {
        Audio.POSITIVE_CLICK_SOUND.play();
        try {
            Main.startMenu(Messages.MAIN_MENU);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void basedOnHardship (MouseEvent mouseEvent) {
        ArrayList<User> users = MainController.userController.getUsers();
        users.sort(new Comparator<User>() {
            @Override
            public int compare (User o1, User o2) {
                int firstLevel = 0;
                switch (o1.getDifficultyLevel()) {
                    case EASY -> firstLevel = 1;
                    case MEDIUM -> firstLevel = 2;
                    case HARD -> firstLevel = 3;
                }
                int secondLevel = 0;
                switch (o2.getDifficultyLevel()) {
                    case EASY -> secondLevel = 1;
                    case MEDIUM -> secondLevel = 2;
                    case HARD -> secondLevel = 3;
                }
                return secondLevel - firstLevel;
            }
        });
        initializeLabels(users);
    }
    
    public void initializeLabels (ArrayList<User> users) {
        int size = users.size();
        player1.setText(users.get(0).getUsername() + " | " +
                users.get(0).getDifficultyLevel().toString() + " | " +
                users.get(0).getScore() + " | " +
                users.get(0).getFinishTime() / 100 + ":" +
                users.get(0).getFinishTime() % 100);
        if (size > 1) player2.setText(users.get(1).getUsername() + " | " + users.get(1).getDifficultyLevel().toString() + " | " + users.get(1).getScore() + " | " +
                users.get(1).getFinishTime() / 100 + ":" +
                users.get(1).getFinishTime() % 100);
        if (size > 2) player3.setText(users.get(2).getUsername() + " | " + users.get(2).getDifficultyLevel().toString() + " | " + users.get(2).getScore() + " | " +
                users.get(2).getFinishTime() / 100 + ":" +
                users.get(2).getFinishTime() % 100);
        StringBuilder labelOthers = new StringBuilder();
        for (int i = 3; i < Math.min(size, 10); i++) {
            labelOthers.append(users.get(i).getUsername())
                    .append(" | ")
                    .append(users.get(i).getDifficultyLevel().toString())
                    .append(" | ").append(users.get(i).getScore())
                    .append(" | ").append(users.get(i).getFinishTime() / 100)
                    .append(":").append(users.get(i).getFinishTime() % 100)
                    .append('\n');
        }
        otherPlayers.setText(labelOthers.toString());
    }
}
