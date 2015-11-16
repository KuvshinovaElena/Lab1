package laba1;

import laba1.ServerImplement;

import java.io.IOException;
import java.net.ServerSocket;


/**
 * Created by Елена on 14.03.2015.
 */

public class Server {

    public static void main (String [] args) throws IOException, ClassNotFoundException
    {
        ServerSocket server = new ServerSocket(1098);
        new ServerImplement(server);
    }

}
