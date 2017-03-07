package HTTPServer;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LYY on 2017/3/7.
 */
public class HTTPReader {
    private static Logger logger = Logger.getLogger(HTTPReader.class);

    public static final String GET = "GET";
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private ReadState readState;

    private Channel body;

    private Map<String, String[]> headers;

    private String requestType;
    private String url;
    private String version;

    public synchronized Channel getBody() {
        if(body == null)
            readBody();
        return body;
    }


    public synchronized Map<String, String[]> getHeaders() {
        if(headers == null){
            readHeaderFully();
        }
        return headers;
    }



    public synchronized String getRequestType() {
        if (requestType == null)
            readSignatureFully();
        return requestType;
    }

    public synchronized String getUrl() {
        if (url == null)
            readSignatureFully();
        return url;
    }

    public synchronized String getVersion() {
        if (version == null)
            readSignatureFully();
        return version;
    }

    private void readSignatureFully() {
        if (readState != ReadState.BEGIN)
            return;
        try {
            String firstLine = bufferedReader.readLine();
            int firstIndex = firstLine.indexOf(" ");
            int secondIndex = firstLine.indexOf(" ", firstIndex + 1);
            this.requestType = firstLine.substring(0, firstIndex).trim();
            this.url = firstLine.substring(firstIndex + 1, secondIndex).trim();
            this.version = firstLine.substring(secondIndex + 1).trim();
            this.readState = ReadState.HEADERS;

        } catch (IOException e) {
            logger.info("不能匹配首部");
        }
    }

    private void readHeaderFully() {
        if (readState != ReadState.HEADERS)
            return;
        String temp = null;

        try {
            Map<String, String[]> headers = new HashMap<String, String[]>();
            while ((temp = bufferedReader.readLine()) != null) {
                if (temp.isEmpty())
                    return;
                int index = temp.indexOf(":");
                if (index == -1) {
                    logger.trace("首部格式错误");
                    throw new IOException("首部格式错误");
                }
                headers.put(temp.substring(0, index).trim(), temp.substring(index + 1).split(","));

            }
            this.headers = headers;
            readState = ReadState.BODY;
        } catch (IOException e) {
            logger.trace("不能格式化http请求");
        }
    }

//    private void readBody(){
//        if(readState != ReadState.BODY)
//            return;
//        StringBuilder stringBuilder = new StringBuilder();
//        byte[] buffer = new byte[1024];
//        int length = 0;
//
//        try {
//            while(-1 != (length = inputStream.read(buffer, 0, 1024))){
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void readBody() {
        if (readState != ReadState.BODY)
            return;
        Channel bodyChannel = Channels.newChannel(inputStream);
        this.body = bodyChannel;
        this.readState = ReadState.END;
    }


    HTTPReader(InputStream inputStream) {
        if (inputStream == null)
            throw new IllegalArgumentException();

        this.inputStream = inputStream;
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        readState = ReadState.BEGIN;

    }


}
