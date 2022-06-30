import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Main {

    public static void main(String[] args) {
        Server server = null;
        try (FileInputStream fis = new FileInputStream("DB\\Server.bin"); ObjectInputStream ois = new ObjectInputStream(fis)){
            server = (Server) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(server != null) {
            server.startServer();
        }
    }
}
