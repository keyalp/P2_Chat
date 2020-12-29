import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EchoServer {
    public static void main(String[] args) {
        Map<String, MySocket> mySocketMap = new ConcurrentHashMap<String,MySocket>();
        MyServerSocket myServerSocket = new MyServerSocket(40000);
        
        while(true){
            MySocket socket = myServerSocket.accept();
            
            new Thread() {
                public void run() {
                    try{
                    String nick = socket.readLine();
                    mySocketMap.put(nick,socket);
                    System.out.println("\u001b[35mSuccessfully Connected\u001b[0m");

                    String line;
                    while ((line = socket.readLine()) != null) {
                        System.out.println("\u001b[33mThe server has received the message: \u001b[0m" + line);
                        for (String n : mySocketMap.keySet()){
                            if(n!=nick){
                                mySocketMap.get(n).writeLine(nick+": "+line);
                                System.out.println("\u001b[32mMessage resend to: \u001b[0m" + n);
                            }
                        }  
                    }
                    System.out.println("\u001b[35mClosed Connection: \u001b[0m"+nick);
                    mySocketMap.remove(nick);
                    socket.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                }
            }.start(); 
    }
}
}
