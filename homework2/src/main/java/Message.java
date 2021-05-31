import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
public class Message {
    Integer cType;
    Integer bUserId;
    String message;
    private byte[] mBytes;
    public void setMBytes(byte[] encryptedMessageInBytes) {
        this.mBytes = encryptedMessageInBytes;
    }
    public byte[] getMBytes() {
        return mBytes;
    }
    public String getMessage() {
        return message;
    }
    public int getBUserId() {
        return bUserId;
    }
    public void setBUserId(int bUserId) {
        this.bUserId = bUserId;
    }
    public Message() { }
    public Message(cTypes cType, Integer bUserId, String message) throws BadPaddingException, IllegalBlockSizeException {
        this.cType = cType.ordinal();
        this.bUserId = bUserId;
        this.message = message;
        encode();
    }
    public byte[] inPacket() {
        return ByteBuffer.allocate(getLength())
                .putInt(cType)
                .putInt(bUserId)
                .put(mBytes).array();
    }
    public int getLength() {return mBytes.length + Integer.BYTES + Integer.BYTES; }
    public void encode() throws BadPaddingException, IllegalBlockSizeException {
        byte[] myMes = message.getBytes(StandardCharsets.UTF_8);
        mBytes = Encryptor.encryptMessage(myMes);
    }
    public void decode() throws BadPaddingException, IllegalBlockSizeException{
        byte[] decryptedMessage = Encryptor.decryptMessage(mBytes);
        message = new String(decryptedMessage);
    }
    public enum cTypes {
        getNumber,
        getProduct,
        addProduct,
        addProductInStore,
        setPrice,
        errors,
        ok
    }
    public void setCType(int cType) {
        this.cType = cType;
    }
    public int getCType() {
        return cType;
    }
}