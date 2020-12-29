import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashSet;

public class UsersHandler {

    public static String getUsers(HashSet<String> users){
        String userList = "#,";

        for(String user: users){
            userList += (user + ",");
        }

        userList += "#";
        return userList;
    }

    public static void sendUsers(HashSet<String> users, Selector selector){
        try{
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            BufferHandler.writeBuffer(buffer, getUsers(users));
            
            for(SelectionKey sk: selector.keys()){
                if(sk.attachment() != null){

                    SocketChannel c = (SocketChannel) sk.channel();
                    c.write(buffer);
                    System.out.println("Users list sent to: " +sk.attachment());
                    buffer.flip();
                }

            }
        }catch(IOException ex){}
    }
    
}
