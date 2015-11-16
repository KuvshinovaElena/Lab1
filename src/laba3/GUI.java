package laba3;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import laba1.Book;
import laba1.EventBase;
import laba1.InputThread;

import java.awt.*;
import java.awt.Label;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Елена on 25.04.2015.
 */

public class GUI extends Application {
    Stage stage;
    private static ObservableList<Book> data = FXCollections.observableArrayList();
    TableView tableView = new TableView();

    static Socket socketInMap = null;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    GUI gui;

    public GUI() throws IOException, ClassNotFoundException {
        gui = this;
        socketInMap = new Socket("localhost", 1098);
        oos = new ObjectOutputStream(socketInMap.getOutputStream());
        ois = new ObjectInputStream(socketInMap.getInputStream());
        System.out.println("Sending request to Socket Server");

        List<String> eventList = EventBase.codingMessages(EventBase.CLIENT_CONNECTION, null, null);
        oos.writeObject(eventList); //Отправляем сообщение на сервер о подключении 1-

        oos.writeObject("Hi server! I'm client - " + socketInMap.toString());       //2-
        String message = (String) ois.readObject();    //Читаем сообщение Hi Client! 3+
        System.out.println(message);
        tableView = new TableView<>();

        //Читаем собщение со списком данных 4+
        data = FXCollections.observableList(EventBase.decodingMessages((List<String>) ois.readObject()));
        new InputThread(socketInMap,this);  //создаем поток для чтения сообщений
    }

    public void init(final Stage primaryStage, final ObservableList<Book> data) throws IOException, NotBoundException, ClassNotFoundException {
        final Group root = new Group();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("BOOK DATABASE 2015");

        TableColumn articleCol = new TableColumn();
        articleCol.setText("Article");
        articleCol.setCellValueFactory(new PropertyValueFactory("article"));

        TableColumn autorCol = new TableColumn();
        autorCol.setText("Autor");
        autorCol.setMinWidth(150);
        autorCol.setCellValueFactory(new PropertyValueFactory("autor"));

        TableColumn titleCol = new TableColumn();
        titleCol.setText("Title");
        titleCol.setMinWidth(300);
        titleCol.setCellValueFactory(new PropertyValueFactory("title"));

            TableColumn quantityCol = new TableColumn();
            quantityCol.setText("Quantity");
            quantityCol.setCellValueFactory(new PropertyValueFactory("quantity"));

            TableColumn priceCol = new TableColumn();
            priceCol.setText("Price");
            priceCol.setCellValueFactory(new PropertyValueFactory("Price"));


            tableView.setItems(data);
            tableView.getColumns().addAll(articleCol, autorCol, titleCol, quantityCol, priceCol);

            root.getChildren().add(tableView);
            HBox hBox = new HBox();


            final Button add = new Button("ADD");
            add.setPrefWidth(248);
            add.setStyle("-fx-text-fill: white;-fx-base: rgb(40,155,220);");
            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(stage != null) {
                        stage.close();
                    }
                    stage = new AddScene();
                    stage.setTitle("ADD IN DATABASE");
                }
            });

            Button remove = new Button("REMOVE");
            remove.setPrefWidth(248);
            remove.setStyle("-fx-text-fill:white; -fx-base: rgb(40,155,220);");
            remove.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Book book = (Book)tableView.getSelectionModel().getSelectedItem();
                    if (book!=null) {
                        List<Book> books = new ArrayList<Book>();
                        books.add(book);
                        List<String> deleteList = EventBase.codingMessages(EventBase.DELETE, books, null);
                        try {
                            connect(EventBase.DELETE, deleteList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            Button edit = new Button("EDIT");
            edit.setPrefWidth(248);
            edit.setStyle("-fx-text-fill:white; -fx-base: rgb(40,155,220);");
            edit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Group root = new Group();
                    try {
                        Book book = (Book)tableView.getSelectionModel().getSelectedItem();
                        stage = new EditScene(root, data, book);
                        stage.setTitle("EDITING DATABASE");
                        tableView.getSelectionModel().clearSelection();
                    }catch (NullPointerException e){
                    }

            }
            });


            final TextField text = new TextField();
            text.setMinWidth(300);
            text.setPromptText("Search");

        Button back = new Button("GO BACK");
        back.setPrefWidth(170);
        back.setStyle("-fx-text-fill:white; -fx-base: rgb(40,155,220);");

        back.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle (ActionEvent event)
            {
                try
                {
                    text.clear();
                    tableView.setItems(data);
                } catch (NullPointerException e)
                {
                }
            }
        });

            final Button search1 = new Button("SEARCH BY ARTICLE");
            search1.setPrefWidth(150);
            search1.setStyle("-fx-text-fill:white; -fx-base: rgb(40,155,220);");
            search1.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle (ActionEvent event)
                {

                    String article = MyScene.checkEnter(text);
                    if (article != null)
                    {
                        ObservableList<Book> find = FXCollections.observableArrayList();
                        try {
                            find.addAll(findByArticle(article));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        tableView.setItems(find);
                        add.setCancelButton(true);
                    }

                }
            });
            search1.setCancelButton(false);
            Button search2 = new Button("SEARCH BY AUTOR");
            search2.setPrefWidth(150);
            search2.setStyle("-fx-text-fill:white; -fx-base: rgb(40,155,220);");
            search2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle (ActionEvent event) {
                    String autor = MyScene.checkEnter(text);
                    if (autor!=null)
                    {
                        ObservableList<Book> find = FXCollections.observableArrayList();
                        try {
                            find.addAll(findByAutor(autor));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        tableView.setItems(find);
                    }
                }
            });


            Button search3 = new Button("SEARCH BY TITLE");
            search3.setPrefWidth(150);
            search3.setStyle("-fx-text-fill:white; -fx-base: rgb(40,155,220);");
            search3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle (ActionEvent event) {
                    String title = MyScene.checkEnter(text);
                    if (title!=null)
                    {
                        ObservableList<Book> findTitle = FXCollections.observableArrayList();
                        try {
                            findTitle.addAll(findByTitle(title));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        tableView.setItems(findTitle);
                    }
                }
            });


            Button search4 = new Button("SEARCH BY QUANTITY");
            search4.setPrefWidth(150);
            search4.setStyle("-fx-text-fill:white; -fx-base: rgb(40,155,220);");
            search4.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle (ActionEvent event) {
                    int quantity = MyScene.checkInputInt(text);
                    if (quantity!=-1)
                    {
                        ObservableList<Book> findQuantity = FXCollections.observableArrayList();

                        try {
                            findQuantity.addAll(findByQuantity(quantity));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        tableView.setItems(findQuantity);
                    }
                }
            });


        Button search5 = new Button("SEARCH BY PRICE");
        search5.setPrefWidth(150);
        search5.setStyle("-fx-text-fill:white; -fx-base: rgb(40,155,220);");
        search5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle (ActionEvent event) {
                int price = MyScene.checkInputInt(text);
                if (price!=-1)
                {
                    ObservableList<Book> findPrice = FXCollections.observableArrayList();
                    try
                    {
                        findPrice.addAll(findByPrice(price));
                    } catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                    tableView.setItems(findPrice);
                }
            }
        });

            hBox.getChildren().add(add);
            hBox.setSpacing(5);
            hBox.getChildren().add(remove);
            hBox.setSpacing(5);
            hBox.getChildren().add(edit);
            hBox.setSpacing(5);
            hBox.getChildren().add(search1);
            hBox.setSpacing(5);

            VBox vBox1 = new VBox(5);
            HBox hBox1 = new HBox(5);
            HBox hBox2 = new HBox(1);
            hBox1.getChildren().add(text);
            hBox2.getChildren().addAll(search1,search2,search3,search4,search5);
            vBox1.getChildren().addAll(hBox,hBox1,hBox2,back);

            VBox vBox = new VBox();
            vBox.getChildren().addAll(tableView,vBox1);
            root.getChildren().add(vBox);
    }

    public static void connect(int num, List <String> eventList) throws IOException {
        Socket socket = new Socket("localhost", 1098);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        oos.writeObject(eventList);
        if (num == EventBase.CLIENT_SHUTDOWN)
            oos.writeObject(socketInMap.getLocalPort());
        oos.close();
        ois.close();
        socket.close();
    }

    public void databaseUpdateRequest (ObservableList<Book> newList) throws RemoteException {
        data.clear();
        data.addAll(newList);
    }

    public void start(Stage primaryStage) throws Exception {
        init(primaryStage,data);
        primaryStage.show();
    }
    @Override
    public void stop() throws Exception {
        List<String> addList = EventBase.codingMessages(EventBase.CLIENT_SHUTDOWN, null,null);
        connect(EventBase.CLIENT_SHUTDOWN,addList);
        InputThread.yield();
        Thread.sleep(1000);
        ois.close();
        oos.close();
        socketInMap.close();
        super.stop();
    }

    public static void main(String[] args) {
            launch(args);
        }

    public static ArrayList<Book> findByAutor(String autor) throws RemoteException {
        ArrayList<Book> newbooks = new ArrayList<Book>();
        for (Book book : data) {
            if (autor.equals(book.getAutor())) {
                newbooks.add(book);
            }
        }
        return newbooks;
    }

    public static ArrayList<Book> findByTitle(String title) throws RemoteException {
        ArrayList<Book> newbooks = new ArrayList<Book>();
        for (Book book : data) {
            if (title.equals(book.getTitle())) {
                newbooks.add(book);
            }
        }
        return newbooks;
    }

    public static ArrayList<Book> findByArticle(String article) throws RemoteException {
        ArrayList<Book> newbooks = new ArrayList<Book>();
        for (Book book : data) {
            if (article.equals(book.getArticle())) {
                newbooks.add(book);
            }
        }
        return newbooks;
    }

    public static ArrayList<Book> findByQuantity(int quantity) throws RemoteException {
        ArrayList<Book> newbooks = new ArrayList<Book>();
        for (Book book : data) {
            if (book.getQuantity() == quantity) {
                newbooks.add(book);
            }
        }
        return newbooks;
    }

    public static ArrayList<Book> findByPrice(int price) throws RemoteException {
        ArrayList<Book> newbooks = new ArrayList<Book>();
        for (Book book : data) {
            if (book.getPrice() == price) {
                newbooks.add(book);
            }
        }
        return newbooks;
    }
}
