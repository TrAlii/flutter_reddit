import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

public class Functions implements Serializable {
    HashSet<String> usernames = new HashSet<>();
    ArrayList<User> users = new ArrayList<>();

    public String signUp(String username , String password , String email){
        if(usernames.contains(username)){
            return "this username already exists";
        }
        if(!Pattern.matches("[A-Za-z0-9._-]+@[A-Za-z0-9]+\\.[A-Za-z]{2,4}" , email)){
            return "email is out of format";
        }
        if(!validPassword(password)){
            return "password is invalid";
        }
        User user = new User(username , password , email);
        usernames.add(username);
        users.add(user);
        return "user successfully signed up";
    }

    public String signIn(String username , String password){
        for (int i=0 ; i<users.size() ; i++) {
            if(users.get(i).username.equals(username)){
                if(users.get(i).password.equals(password)){
                    return "signed in";
                }
                else {
                    return "password is not correct";
                }
            }
        }
        return "username dose not exist";
    }

    public User getUser(String username , String password){
        for (int i=0 ; i<users.size() ; i++) {
            if(users.get(i).equals(new User(username , password , ""))){
                return users.get(i);
            }
        }
        return null;
    }

    public boolean validPassword(String password){
        if(password.length() < 8){
            return false;
        }
        boolean num , capital , small;
        num = capital = small = false;
        for (int i=0 ; i<password.length() ; i++) {
            if(password.charAt(i) >= 48 && password.charAt(i) <= 57){
                num = true;
            }
            if(password.charAt(i) >= 65 && password.charAt(i) <= 90){
                capital = true;
            }
            if(password.charAt(i) >= 97 && password.charAt(i) <= 122){
                small = true;
            }
        }
        if(num && capital && small){
            return true;
        }
        return false;
    }

    public void subscribe(Channel channel , User user){
        user.channels.add(channel);
        channel.subscribers.add(user);
    }

    public String deletePost(Channel channel , Post post , User user){
        if(channel.owner.equals(user)){
            channel.posts.remove(post);
            return "deleted";
        }
        return "you dont have this access";
    }

    public String editChannel(Channel channel , User user , String name , String description){
        if(channel.owner.equals(user)){
            channel.name = name;
            channel.description = description;
            return "edited";
        }
        return "you dont have this access";
    }
    public void addPost(Channel channel , Post post){
        channel.posts.add(post);
    }
}
