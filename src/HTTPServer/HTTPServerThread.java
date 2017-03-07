package HTTPServer;

import java.io.*;
import java.net.Socket;

/**
 * Created by LYY on 2017/3/7.
 */
public class HTTPServerThread extends Thread{
    private Socket clientSocket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String


    public HTTPServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;

    }


    @Override
    public void run() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            String firstLineOfRequest;
            firstLineOfRequest = bufferedReader.readLine();

            String url = firstLineOfRequest.split(" ")[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
