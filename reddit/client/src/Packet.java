import java.io.Serializable;

public class Packet implements Serializable {
    Object object;

    public Packet(Object object) {
        this.object = object;
    }
}
