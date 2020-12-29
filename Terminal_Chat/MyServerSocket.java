import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

public class MyServerSocket {
    ServerSocket ss;

    public MyServerSocket(final int port) {
        try {
            ss = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MySocket accept() {
        try {
            return new MySocket(ss.accept());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
