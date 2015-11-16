package laba1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Елена on 22.09.2015.
 */
public class EventBase {

    public static final int CLIENT_CONNECTION = 1;
    public static final int ADD = 2;
    public static final int DELETE = 3;
    public static final int EDIT = 4;
    public static final int CLIENT_SHUTDOWN = 0;

    public static List<String> codingMessages (int event, List<Book> books, String article) {
        List<String> message = new ArrayList<>();
        if (books == null) {
            message.add(Integer.toString(event));
            return message;
        }
        for(int i = 0; i < books.size(); i++) {
            message.add(Integer.toString(event));
            message.add(books.get(i).getArticle());
            message.add(books.get(i).getAutor());
            message.add(books.get(i).getTitle());
            message.add(Integer.toString(books.get(i).getQuantity()));
            message.add(Integer.toString(books.get(i).getPrice()));
            if (article != null){       //Для изменения записи
                message.add(article);
            }
        }
        return message;
    }

    public static ArrayList<Book> decodingMessages (List<String> message){
        ArrayList<Book> books = new ArrayList<Book>();
        if (message.size() > 1) {
            for (int i = 0; i < message.size()/6; i++) {
                    Book book = new Book();
                    book.setArticle(message.get(i * 6 + 1));
                    book.setAutor(message.get(i * 6 + 2));
                    book.setTitle(message.get(i * 6 + 3));
                    book.setQuantity(Integer.parseInt(message.get(i * 6 + 4)));
                    book.setPrice(Integer.parseInt(message.get(i * 6 + 5)));
                    books.add(book);
                }
            }
        return books;
    }
}
