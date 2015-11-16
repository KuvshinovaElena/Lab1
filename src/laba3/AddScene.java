package laba3;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import laba1.Book;
import laba1.EventBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Елена on 09.05.2015.
 */
public class AddScene extends MyScene{

    public AddScene(){
        final Group root = new Group();
        setScene(new Scene(root));

        Label labell = new Label("Article");
        final TextField text1 = new TextField();
        text1.setPrefWidth(300);

        Label label2 = new Label("Autor");
        final TextField text2 = new TextField();
        text2.setPrefWidth(300);


        Label label3 = new Label("Title");
        final TextField text3 = new TextField();
        text3.setPrefWidth(300);

        Label label4 = new Label("Quantity");
        final TextField text4 = new TextField();
        text4.setPrefWidth(300);

        Label label5 = new Label("Price");
        final TextField text5 = new TextField();
        text5.setPrefWidth(300);

        Button addbut = new Button("ADD");
        addbut.setPrefWidth(300);
        addbut.setStyle("-fx-text-fill: white;-fx-base: rgb(40,155,220);");
        addbut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String article = checkEnter(text1);
                if (article!=null) {
                    try {
                        article = checkArticle(null, text1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String autor = checkEnter(text2);
                String title  = checkEnter(text3);
                int quantity = checkInputInt(text4);
                int price = checkInputInt(text5);
                if ((article!=null)&&(autor!=null)&&(title!=null)&&(quantity!=-1)&&(price!=-1)) {
                    Book book = new Book();
                    book.setArticle(article);
                    book.setAutor(autor);
                    book.setTitle(title);
                    book.setQuantity(quantity);
                    book.setPrice(price);
                    ArrayList<Book> books = new ArrayList<Book>();
                    books.add(book);
                    List<String> addList = EventBase.codingMessages(EventBase.ADD, books, null);
                   try {
                        GUI.connect(EventBase.ADD,addList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    close();
                }
            }
        });

        VBox vBox1 = new VBox(5);
        vBox1.getChildren().addAll(labell,text1, label2, text2, label3, text3, label4, text4,label5,text5,addbut);
        root.getChildren().add(vBox1);
        this.show();
    }
}
