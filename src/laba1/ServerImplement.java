package laba1;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Елена on 15.03.2015.
 */

public class ServerImplement extends Thread {
    private static ArrayList<Book> books = new ArrayList<Book>();
    public Map<Integer,Socket> clients = new HashMap<Integer, Socket>();
    private static DOM dom;
    private ServerSocket server;

    public ServerImplement(ServerSocket server) throws IOException {
        this.server = server;
        dom = new DOM();
        try {
            dom.XMLReader(books);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        start();
    }

    @Override
    public void run() {
        try {
            System.out.println("Waiting for clients request");

            while(true) {
                //ожидание соединения клиентов
                Socket socket = server.accept();
                //создание  объекта ObjectInputStream для чтения
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //создание  объекта ObjectOutputStream для записи
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                try {
                    int res;
                    List<String> listE = (List<String>) ois.readObject();   //+1
                    res = eventClient(listE, oos,ois);

                    if (res == 1)
                    {
                        clients.put(socket.getPort(),socket);
                        continue;
                    }

                    if (res == -1) {
                        Integer port = (Integer) ois.readObject();
                        System.out.println("Client finished: " + clients.toString());
                        oos = new ObjectOutputStream(clients.get(port).getOutputStream());
                        oos.writeObject("Bye Client!");
                        clients.remove(port); //удаляем сокет из map
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
                oos.close();
                ois.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTables() throws RemoteException {
        for(Socket socket: clients.values()){
            try {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject("Update");
                List<String> events = EventBase.codingMessages(EventBase.ADD, books,null);
                out.writeObject(events);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Добавление элемента в конец списка
    public void paste(Book book) throws RemoteException {
        books.add(book);
        try {
            dom.XMLWriter(books);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void edit(int index, Book book) throws RemoteException {
        books.set(index, book);
        try {
            dom.XMLWriter(books);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public int IndexEdit(String article, Book book) throws RemoteException {
        int index = 0;
        for (Book dop : this.books) {
            if (article.equals(dop.getArticle())) {
                edit(index, book);
            }
            index++;
        }
        return index;
    }

    public ArrayList<Book> delTheArticle(String article) throws RemoteException {
        ArrayList<Book> newbooks = new ArrayList<Book>();
        for (Book book : books) {
            if (!article.equals(book.getArticle())) {
                newbooks.add(book);
            }
        }
        this.books = newbooks;
        try {
            dom.XMLWriter(books);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return this.books;
    }

    public int eventClient(List<String> messages, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, XMLStreamException {
        if (messages != null){
           ArrayList<Book> newbooks = EventBase.decodingMessages(messages);
            switch (Integer.parseInt(messages.get(0))) {

                case EventBase.CLIENT_CONNECTION:
                {
                    //конвертируем сообщение от клиента в строку
                    String message = null;
                    try {
                        message = (String) ois.readObject();    //Читаем сообщение Hi server! I'm client... +2
                        System.out.println(message);
                        //пишем сообщение в сокет через ObjectOutputStream
                        oos.writeObject("Hi Client! Sever is ready to work");      //-3
                        List<String> reply = EventBase.codingMessages(EventBase.ADD, this.books,null);
                        oos.writeObject(reply);      //отправляем клиенту список данных -4
                        return 1;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                case EventBase.ADD:
                {
                    paste(newbooks.get(0));
                    updateTables();
                    return 0;
                }

                case EventBase.DELETE:
                {
                    delTheArticle(messages.get(1));
                    updateTables();
                    return 0;
                }
                case EventBase.EDIT:
                {
                    IndexEdit(messages.get(6), newbooks.get(0));
                    updateTables();
                    return 0;
                }

                case EventBase.CLIENT_SHUTDOWN:
                {
                    return -1;
                }
            }
        }
        return 2;
    }
}




















