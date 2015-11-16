package laba1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mvideo on 20.09.2015.
 */
public class DOM {
    private static final String FIlE_PATH = "src/BooksDatabase.xml";
    private static final String ROOT = "BooksDatabase";
    private static final String ELEMENT = "Book";
    private static final String ARTICLE = "Article";
    private static final String AUTOR = "Autor";
    private static final String TITLE = "Title";
    private static final String QUANTITY = "Quantity";
    private static final String PRICE = "Price";

    public void XMLWriter (ArrayList<Book> books) throws ParserConfigurationException, TransformerException {
        if (books.isEmpty()) return;
        File fxml=new File(FIlE_PATH);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.newDocument();

        Element rootElement = document.createElement(ROOT);
        document.appendChild(rootElement);
        int i = 0;
        for (Book book: books){
            Element elem = document.createElement(ELEMENT);
            rootElement.appendChild(elem);

            Element article = document.createElement(ARTICLE);
            article.appendChild(document.createTextNode(book.getArticle()));
            elem.appendChild(article);

            Element autor = document.createElement(AUTOR);
            autor.appendChild(document.createTextNode(book.getAutor()));
            elem.appendChild(autor);

            Element title = document.createElement(TITLE);
            title.appendChild(document.createTextNode(book.getTitle()));
            elem.appendChild(title);

            Element quantity = document.createElement(QUANTITY);
            quantity.appendChild(document.createTextNode(Integer.toString(book.getQuantity())));
            elem.appendChild(quantity);

            Element price = document.createElement(PRICE);
            price.appendChild(document.createTextNode(Integer.toString(book.getPrice())));
            elem.appendChild(price);

        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(fxml);
        transformer.transform(source,result);
    }

    public void XMLReader (ArrayList<Book> books) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        File fxml=new File(FIlE_PATH);
        if (fxml.length() == 0) return ;

        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.parse(fxml);
        NodeList readerList = document.getDocumentElement().getChildNodes();
        for (int i = 0; i<readerList.getLength();i++){
            Node node = readerList.item(i);
            if (node.getNodeName() == ELEMENT){
                Book book = new Book();
                NodeList childNodes = node.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node cNode = childNodes.item(j);
                    String childNoteContent;
                    switch (cNode.getNodeName()) {
                        case ARTICLE:
                            childNoteContent = cNode.getLastChild().getTextContent().trim();
                            book.setArticle(childNoteContent);
                        case AUTOR:
                            childNoteContent = cNode.getLastChild().getTextContent().trim();
                            book.setAutor(childNoteContent);
                            break;
                        case TITLE:
                            childNoteContent = cNode.getLastChild().getTextContent().trim();
                            book.setTitle(childNoteContent);
                            break;
                        case QUANTITY:
                            childNoteContent = cNode.getLastChild().getTextContent().trim();
                            book.setQuantity(Integer.parseInt(childNoteContent));
                        case PRICE:
                            childNoteContent = cNode.getLastChild().getTextContent().trim();
                            book.setPrice(Integer.parseInt(childNoteContent));
                            break;
                    }
                }
                books.add(book);
            }
        }
    }
}
