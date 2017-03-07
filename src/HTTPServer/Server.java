package HTTPServer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by LYY on 2017/3/7.
 */
public class Server
{
    private static Logger logger = Logger.getLogger(Server.class);

    private static int serverPort = 80;
    private static ServerSocket serverSocket;

    Server(){

    }
    Server(int serverPort){
        this.serverPort = serverPort;
    }

    private static void start() {

        try {
            serverSocket = new ServerSocket(serverPort);

        } catch (IOException e) {

            logger.trace("serversocket:",e);
        }

        while (true){
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                HTTPServerThread httpServerThread = new HTTPServerThread(clientSocket);
                httpServerThread.start();
            } catch (IOException e) {

                logger.trace("clientSocket接受成功:",e);
            }


        }
    }
    public static void main(String[] args){
        PropertyConfigurator.configure("src\\log4j.properties");
        logger.info("运行成功");
        Server.start();
    }
}
