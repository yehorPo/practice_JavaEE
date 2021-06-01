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
        System.out.println("==Server started==");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Packet packet = null;
        try {
            packet  = new Packet((byte) 1, 1L, new Message(Message.cTypes.getProduct,1,"sender1"));
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        System.out.println("==Packet inited==");
        Sender sender = new Sender(1, packet);
        sender.start();
        System.out.println("==Sender started==");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server.shutdown();
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("==Server closed==");
    }

}