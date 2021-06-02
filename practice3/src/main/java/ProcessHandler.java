import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
public class ProcessHandler {
    public static Packet process(Packet inputPacket)throws BadPaddingException, IllegalBlockSizeException {
        Message test = inputPacket.getBMsq();
        Message answer;
        String out;
        out = test.getMessage() + " Ok";
        answer = new Message(Message.cTypes.ok, 0, out);
        Packet send = new Packet(inputPacket.bSrc, inputPacket.bPktId, answer);
        return send;
    }
}