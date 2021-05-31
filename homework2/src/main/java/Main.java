import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;


public class Main {

    public static void main(String[] args){
        int port;
        port = 12345;
        Server server = null;
        try {
            server = new Server(port);
        } catch (IOException e) {
        }
        server.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        Packet packet1 = null;
        Packet packet2 = null;
        try {
            packet1  = new Packet((byte) 1, 1L, new Message(Message.cTypes.ADD_PRODUCT_GROUP,1,"client1"));
            packet2  = new Packet((byte) 1, 1L, new Message(Message.cTypes.ADD_PRODUCT,1,"client2"));
        } catch (BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        }
        Client client1 = new Client(port, packet1);
        Client client2 = new Client(port, packet2);
        client1.start();
        client2.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server.shutdown();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
    }

}