import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String username;
    String password;
    String email;
    ArrayList <Channel> channels = new ArrayList<>();
    ArrayList <Channel> favoriteChannels = new ArrayList<>();
    ArrayList <Post> savedPosts = new ArrayList<>();

    public User(String username , String password , String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        return password != null ? password.equals(user.password) : user.password == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
