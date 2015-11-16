package laba3;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Елена on 16.05.2015.
 */
public class MyScene extends Stage {
    public static int checkInputInt(TextField text){
        if (text.getText().isEmpty()){
            text.setStyle("-fx-opacity: 0.5; -fx-background-color: red;");
            text.setPromptText("Field must be filled");
            return -1;
        }
        int num;
        try {
            num = Integer.parseInt(text.getText());
        } catch (NumberFormatException ex) {
            text.clear();
            text.setStyle("-fx-opacity: 0.5; -fx-background-color: red");
            text.setPromptText("Error! Enter an integer!");
            return -1;
        }
        if (num < 0){
            text.clear();
            text.setStyle("-fx-opacity: 0.5; -fx-background-color: red");
            text.setPromptText("Error! Enter a positive integer");
            return -1;
        }
        return num;
    }
    public String checkArticle (String article,TextField newarticle) throws IOException {
       if (!newarticle.getText().equals(article)) {
            if (!GUI.findByArticle(newarticle.getText()).isEmpty()) {
                newarticle.clear();
                newarticle.setStyle("-fx-opacity: 0.5; -fx-background-color: red");
                newarticle.setPromptText("This article is already in the database.");
                return null;
            }
        }
        return newarticle.getText();
    }
    public static String checkEnter (TextField text){
        String str;
        if (!(str = text.getText()).isEmpty())
            return str;
        else {
            text.setStyle("-fx-opacity: 0.5; -fx-background-color: red");
            text.setPromptText("Field must be filled");
            return null;
        }
    }
}
