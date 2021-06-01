import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
public class Main {
    public static void main(String[] args){
        Server server = null;
        try {
            server = new Server(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Packet packet1 = null;
        Packet packet2 = null;
        try {
            packet1  = new Packet((byte) 1, 1L, new Message(Message.cTypes.getProduct,1,"sender1"));
            packet2  = new Packet((byte) 1, 1L, new Message(Message.cTypes.addProduct,1,"sender2"));
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        Sender sender1 = new Sender(1, packet1);
        Sender sender2 = new Sender(1, packet2);
        sender1.start();
        sender2.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server.shutdown();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}