import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class MySocket{
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    String nick;

    public MySocket(Socket sock){
        try{
            socket = sock;
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public MySocket(String name, String host, int port){
        try{
            nick = name;
            socket = new Socket(host,port);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String readLine() {
        String str = "";
        try {
            str = bufferedReader.readLine();
        } catch (Exception e) {
            return null;
        }
        return str;
    }

    public void writeLine(String line) {
        if(line!=null){
            printWriter.println(line);
            printWriter.flush();
        }
    }

    public void close(){
        try{
            bufferedReader.close();
            printWriter.close();
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getNick(){
        return nick;
    }
}


