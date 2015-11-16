package laba3;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import laba1.Book;
import laba1.EventBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Елена on 21.05.2015.
 */
public class EditScene extends MyScene {
    static int flag=0;
    public EditScene(final Group root, final ObservableList<Book> data, final Book book){
        setScene(new Scene(root));
        final String article = book.getArticle();

        final Label labell = new Label("Article");
        final TextField text1 = new TextField(book.getArticle());
        text1.setPrefWidth(300);

        final Label label2 = new Label("Autor");
        final TextField text2 = new TextField(book.getAutor());
        text2.setPrefWidth(300);

        final Label label3 = new Label("Title");
        final TextField text3 = new TextField(book.getTitle());
        text3.setPrefWidth(300);
        final Label label4 = new Label("Quantity");
        final TextField text4 = new TextField(Integer.toString(book.getQuantity()));
        text4.setPrefWidth(300);

        final Label label5 = new Label("Price");
        final TextField text5 = new TextField(Integer.toString(book.getPrice()));
        text5.setPrefWidth(300);

        final Button addbut = new Button("OK");
        addbut.setPrefWidth(300);
        addbut.setStyle("-fx-text-fill: white;-fx-base: rgb(40,155,220);");
        addbut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String newarticle = checkEnter(text1);
                if (newarticle != null) {
                    try {
                        newarticle = checkArticle(article, text1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String autor = checkEnter(text2);
                String title = checkEnter(text3);
                int quantity = checkInputInt(text4);
                int price = checkInputInt(text5);
                if ((newarticle != null) && (autor != null) && (title != null) && (quantity != -1) && (price != -1)) {
                    Book newbook = new Book();
                    newbook.setArticle(newarticle);
                    newbook.setAutor(autor);
                    newbook.setTitle(title);
                    newbook.setQuantity(quantity);
                    newbook.setPrice(price);
                    ArrayList<Book> books = new ArrayList<Book>();
                    books.add(newbook);
                    flag = 0;

                    for (Book i : data) {
                        if (article.equals(i.getArticle())) {
                            flag++;
                        }
                    }
                    if (flag != 0) {
                        List<String> addList = EventBase.codingMessages(EventBase.EDIT, books, article);
                        try {
                            GUI.connect(EventBase.EDIT, addList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int index=0;
                        for (Book i : data) {
                            if (i.getArticle() == article)
                                data.set(index, newbook);
                            index++;
                        }
                    }
                    else
                        new ErrorScene();
                    close();

                }
            }
        });

        VBox vBox1 = new VBox(5);
        vBox1.getChildren().addAll(labell, text1, label2, text2, label3, text3, label4, text4, label5, text5, addbut);
        root.getChildren().add(vBox1);
        this.show();
    }
    public boolean dop (){
        if (flag == 0)
             return true;
        else
             return false;
    }


}
