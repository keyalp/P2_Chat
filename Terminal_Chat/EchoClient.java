import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class EchoClient {
    BufferedReader bufferedReader;

    public EchoClient(){
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void threadInput(MySocket sc) throws IOException {
    String str;

    try{
        while((str= bufferedReader.readLine()) != null){
            sc.writeLine(str);
            System.out.println("\u001b[32mMeesage Sent\u001b[0m\n");
        }
    }catch(NullPointerException ex){
           sc.close();   
    }
    System.out.println("\u001b[35mClosed Connection\u001b[0m");
    }

    public void threadOutput(MySocket sc){
        try{
        String lectura = sc.readLine();
        System.out.println("\u001b[31m"+lectura.split(":")[0]+":"+"\u001b[0m"+lectura.split(":")[1]+"\n"); 
        }catch(Exception e){
            e.printStackTrace();
        }
    }

public static void main(String[] args){
    EchoClient echoClient = new EchoClient();
    System.out.print("\u001b[34mIntrocue your nick:\u001b[0m ");
    String nick = "";
    try{
        nick = echoClient.bufferedReader.readLine();
        System.out.println("Welcome! \u001b[31m" + nick + "\u001b[0m\n");
    }catch(Exception e){
        e.printStackTrace();
    }
    MySocket sc = new MySocket(nick, "localhost", 40000);
    sc.writeLine(nick);

    new Thread(){
        public void run(){
            try{
                    echoClient.threadInput(sc);
            }catch(Exception e ){
                e.printStackTrace();
            }
        }
    }.start();

    new Thread(){
        public void run(){
            while(true){
                echoClient.threadOutput(sc);
            }         
        }
    }.start();

    }

}
   




