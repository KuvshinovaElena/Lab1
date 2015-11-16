package laba3;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


/**
 * Created by HP on 08.11.2015.
 */
public class ErrorScene extends MyScene{
    public ErrorScene() {
        final Group root = new Group();
        setScene(new Scene(root));
        setTitle("ERROR");
        Label errorLabel = new Label("ERROR!!! This record was deleted other client!");
        Button but = new Button("OK");
        but.setPrefWidth(300);
        but.setStyle("-fx-text-fill: white;-fx-base: rgb(40,155,220);");
        but.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                close();
            }
        });
        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(errorLabel,but);
        root.getChildren().add(vBox);
        this.show();
    }
}
