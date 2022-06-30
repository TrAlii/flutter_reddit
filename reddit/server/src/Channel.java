import java.io.Serializable;
import java.util.ArrayList;

public class Channel implements Serializable {
    String name;
    User owner;
    ArrayList <User> admins = new ArrayList<>();
    String description;
    ArrayList <User> subscribers = new ArrayList<>();
    ArrayList <Post> posts = new ArrayList<>();

    public Channel(String name , User owner , String description){
        this.name = name;
        this.owner = owner;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Channel channel = (Channel) o;

        return name != null ? name.equals(channel.name) : channel.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
