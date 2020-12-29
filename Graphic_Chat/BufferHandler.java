import java.nio.ByteBuffer;

public class BufferHandler {

    public static String readBuffer(ByteBuffer buffer){
        return new String(buffer.array()).trim();

    }

    public static void writeBuffer(ByteBuffer buffer, String s){
        buffer.put(s.getBytes());
        buffer.flip();
    }

}
