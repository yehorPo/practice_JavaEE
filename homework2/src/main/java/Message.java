import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
public class Message {
    Integer cType;
    Integer bUserId;
    String message;
    private byte[] bytes;
    public void setCType(int cType) {
        this.cType = cType;
    }
    public Message() { }
    public Message(cTypes cType, Integer bUserId, String message) throws BadPaddingException, IllegalBlockSizeException {
        this.cType = cType.ordinal();
        this.bUserId = bUserId;
        this.message = message;
        encode();
    }
    public void setBUserId(int bUserId) {
        this.bUserId = bUserId;
    }
    public String getMessage() {
        return message;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    public byte[] toPacketPart() {
        return ByteBuffer.allocate(mLength())
                .putInt(cType)
                .putInt(bUserId)
                .put(bytes).array();
    }
    public int mLength() {return bytes.length + Integer.BYTES + Integer.BYTES; }
    public void encode() throws BadPaddingException, IllegalBlockSizeException {

        byte[] myMes = message.getBytes(StandardCharsets.UTF_8);
        bytes = Encryptor.code(myMes);
    }
    public void decode() throws BadPaddingException, IllegalBlockSizeException{
        byte[] decryptedMessage = Encryptor.decode(bytes);
        message = new String(decryptedMessage);
    }
    public enum cTypes {
        amount,
        getProduct,
        addProduct,
        getGrope,
        addGrope,
        setProduct,
        errors,
        ok
    }

}