package HTTPServer;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * Created by LYY on 2017/3/7.
 */
public class HTTPServerThread extends Thread{
    private Socket clientSocket;
    private static Logger logger = Logger.getLogger(HTTPServerThread.class);

    private HTTPReader httpReader;
    private HTTPWriter httpWriter;
    HTTPServerThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        httpReader = new HTTPReader(clientSocket.getInputStream());
        httpWriter = new HTTPWriter(clientSocket.getOutputStream());

    }


    @Override
    public void run() {

        String type = httpReader.getRequestType();
        if(type.toUpperCase().equals("GET")){
            logg
        }

    }
}
