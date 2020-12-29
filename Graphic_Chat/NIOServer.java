import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class NIOServer {

    private static Selector selector = null;
    private static HashSet<String> users = new HashSet<>();


    public static void main(String[] args) {

        try {

            selector = Selector.open();

            ServerSocketChannel socket = ServerSocketChannel.open();
            ServerSocket serverSocket = socket.socket();

            serverSocket.bind(new InetSocketAddress("localhost", 8089));

            socket.configureBlocking(false);
            int ops = socket.validOps();
            socket.register(selector, ops, null);

            while (true) {

                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> i = selectedKeys.iterator();

                while (i.hasNext()) {
                    SelectionKey key = i.next();

                    if (key.isAcceptable()) {

                        ServerHandler.handleAccept(socket, key, users, selector);

                    } else if (key.isReadable()) {

                        ServerHandler.handleRead(key, users, selector);
                    }
                    i.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
