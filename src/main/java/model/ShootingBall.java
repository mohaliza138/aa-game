package model;

import controller.MainController;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;

public class ShootingBall extends Circle {
    public final int number;
    private Text text;
    
    public Text getText () {
        return text;
    }
    
    public ShootingBall (Pane pane, int number, Color color) {
        super();
        this.number = number;
        this.setRadius(15);
        this.setFill(color);
        if (number > 0) text = new Text("" + number);
        else text = new Text("");
        if (MainController.userController.getCurrentUser() != null && MainController.userController.getCurrentUser().isbAndW())text.setFill(Color.WHITE);
        else text.setFill(Color.BLACK);
        text.setFont(new Font("Comic Sans MS",15));
        pane.getChildren().addAll(this, text);
    }
    
    public void setXOfCenter (double x) {
        super.setCenterX(x);
        text.setLayoutX(x - text.getText().length() * 4);
    }
    
    public void setYOfCenter (double y) {
        super.setCenterY(y);
        text.setLayoutY(y + 4);
    }
    
    public void changeSize (boolean makeBigger) {
        if (makeBigger) setRadius(17);
        else setRadius(15);
    }
    
    public void setVisibility (boolean value) {
        super.setVisible(value);
        text.setVisible(value);
    }
}
