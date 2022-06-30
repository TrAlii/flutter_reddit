import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Client {
    private String ip;
    private int port;

    public Client(String ip, int port ) {
        this.ip = ip;
        this.port = port;
    }

    public void start(){
        try {
            Socket socket = new Socket(ip,port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Scanner input = new Scanner(System.in);


            while (true) {
                System.out.println("-------------------------");
                boolean flag = true;
                System.out.println("1.sign in\n2.sign up\n3.exit");
                String in = input.nextLine();
                if (in.equals("2") || in.equals("sign up")) {
                    while (true) {
                        System.out.println("enter your username :");
                        String username = input.nextLine();
                        System.out.println("enter your password (with at least one digit , one capital and one small letter) :");
                        String password = input.nextLine();
                        System.out.println("enter your email");
                        String email = input.nextLine();
                        HashMap<Object, Object> data = new HashMap<>();
                        data.put("type", "sign up");
                        data.put("username", username);
                        data.put("password", password);
                        data.put("email", email);
                        Packet packet = new Packet(data);
                        out.writeObject(packet);
                        Packet inPacket = (Packet) ois.readObject();
                        String respawn = (String) inPacket.object;
                        if (respawn.equals("user successfully signed up")) {
                            System.out.println("welcome!");
                            flag = false;
                            break;
                        } else {
                            System.out.println(respawn);
                        }
                    }
                } else if (in.equals("1") || in.equals("sign in")) {
                    while (true) {
                        System.out.println("enter your username :");
                        String username = input.nextLine();
                        System.out.println("enter your password :");
                        String password = input.nextLine();
                        HashMap<Object, Object> data = new HashMap<>();
                        data.put("type", "sign in");
                        data.put("username", username);
                        data.put("password", password);
                        Packet packet = new Packet(data);
                        out.writeObject(packet);
                        Packet inPacket = (Packet) ois.readObject();
                        String respawn = (String) inPacket.object;
                        if (respawn.equals("signed in")) {
                            System.out.println("welcome!");
                            flag = false;
                            break;
                        } else {
                            System.out.println(respawn);
                        }
                    }
                } else if (in.equals("3") || in.equals("exit")) {
                    System.out.println("good luck");
                    System.exit(0);
                } else {
                    System.out.println("invalid input");
                }
                if(!flag){
                    break;
                }
            }


            while (true){
                System.out.println("-------------------------");
                System.out.println("1.feed\n2.channels\n3.add post\n4.setting\n5.exit");
                String in = input.nextLine();
                if (in.equals("1") || in.equals("feed")) {
                    while (true) {
                        HashMap<Object, Object> feed = new HashMap<>();
                        feed.put("type", "feed");
                        Packet feedPacket = new Packet(feed);
                        out.writeObject(feedPacket);
                        Packet inFeedPacket = (Packet) ois.readObject();
                        Post[] feedPosts = (Post[]) inFeedPacket.object;
                        System.out.println("choose one of posts (by its index) to see (or type zero to back to  menu) :");
                        for (int i=0 ; i<feedPosts.length ; i++) {
                            Post tmp = feedPosts[i];
                            System.out.println("\t" + (i+1) + ". " + tmp.title);
                            System.out.println("\t\t" + tmp.body);
                        }
                        int index = Integer.parseInt(input.nextLine());
                        while (index < 0 || index > feedPosts.length) {
                            System.out.println("invalid index");
                            index = Integer.parseInt(input.nextLine());
                        }
                        if (index == 0) {
                            break;
                        }
                        Post post = feedPosts[index-1];
                        System.out.println("0.back to feed lists\n1.see post\n2.like\n3.dislike\n4.delete\n5.save");
                        index = Integer.parseInt(input.nextLine());
                        while (index < 0 || index > 5) {
                            System.out.println("invalid index");
                            index = Integer.parseInt(input.nextLine());
                        }
                        if(index == 0){
                            continue;
                        }
                        else if(index == 1){
                            System.out.println(post.channelName + " / " +post.writer + ": " + post.title + "\n" + post.body);
                            System.out.println(post.time + "\tlikes : " + post.likes + "\tdislikes : " + post.dislikes );
                            System.out.println("choose one of comments (by its index) to see (or type zero to back to channel) :");
                            for (int i=0 ; i<post.comments.size() ; i++) {
                                Comment tmp = post.comments.get(i);
                                System.out.println("\t" + (i+1) + ". " + tmp.writer + " : " + tmp.content);
                                System.out.println("\t\tlikes : " + tmp.likes + "\tdislikes : " + tmp.dislikes);
                            }
                            index = Integer.parseInt(input.nextLine());
                            while (index < 0 || index > post.comments.size()) {
                                System.out.println("invalid index");
                                index = Integer.parseInt(input.nextLine());
                            }
                            if (index == 0) {
                                break;
                            }
                            Comment comment = post.comments.get(index-1);
                            System.out.println("0.back to channel\n1.like\n2.dislike");
                            index = Integer.parseInt(input.nextLine());
                            while (index < 0 || index > 2) {
                                System.out.println("invalid index");
                                index = Integer.parseInt(input.nextLine());
                            }
                            if(index == 0){
                                continue;
                            }
                            else if(index == 1){
                                HashMap<Object, Object> like = new HashMap<>();
                                like.put("type", "like comment");
                                like.put("comment" , comment);
                                Packet likePacket = new Packet(like);
                                out.writeObject(likePacket);
                                Packet inDisPacket = (Packet) ois.readObject();
                                String finalRespawn = (String) inDisPacket.object;
                                System.out.println(finalRespawn);
                            }
                            else {
                                HashMap<Object, Object> dislike = new HashMap<>();
                                dislike.put("type", "dislike comment");
                                dislike.put("comment" , comment);
                                Packet dislikePacket = new Packet(dislike);
                                out.writeObject(dislikePacket);
                                Packet inDisPacket = (Packet) ois.readObject();
                                String finalRespawn = (String) inDisPacket.object;
                                System.out.println(finalRespawn);
                            }
                        }
                        else if(index == 2){
                            HashMap<Object, Object> like = new HashMap<>();
                            like.put("type", "like post");
                            like.put("post" , post);
                            Packet likePostPacket = new Packet(like);
                            out.writeObject(likePostPacket);
                            Packet inDisPacket = (Packet) ois.readObject();
                            String finalRespawn = (String) inDisPacket.object;
                            System.out.println(finalRespawn);
                        }
                        else if(index == 3){
                            HashMap<Object, Object> dislike = new HashMap<>();
                            dislike.put("type", "dislike post");
                            dislike.put("post" , post);
                            Packet dislikePostPacket = new Packet(dislike);
                            out.writeObject(dislikePostPacket);
                            Packet inDisPacket = (Packet) ois.readObject();
                            String finalRespawn = (String) inDisPacket.object;
                            System.out.println(finalRespawn);
                        }
                        else if(index == 4){
                            HashMap<Object, Object> delete = new HashMap<>();
                            delete.put("type", "delete post");
                            delete.put("post" , post);
                            Packet deletePostPacket = new Packet(delete);
                            out.writeObject(deletePostPacket);
                            Packet inDeletePacket = (Packet) ois.readObject();
                            String finalRespawn = (String) inDeletePacket.object;
                            System.out.println(finalRespawn);
                        }
                        else {
                            HashMap<Object, Object> save = new HashMap<>();
                            save.put("type", "save");
                            save.put("post" , post);
                            Packet savePostPacket = new Packet(save);
                            out.writeObject(savePostPacket);
                            Packet inSavePacket = (Packet) ois.readObject();
                            String finalRespawn = (String) inSavePacket.object;
                            System.out.println(finalRespawn);
                        }
                    }
                } else if (in.equals("2") || in.equals("channels")) {
                    while (true) {
                        System.out.println("choose one of channels (by its index) to see (or type zero to back to first menu) :");
                        HashMap<Object, Object> data = new HashMap<>();
                        data.put("type", "channels");
                        Packet packet = new Packet(data);
                        out.writeObject(packet);
                        Packet inPacket = (Packet) ois.readObject();
                        HashMap<String, ArrayList<Channel>> respawn = (HashMap<String, ArrayList<Channel>>) inPacket.object;
                        ArrayList<Channel> channels = respawn.get("channels");
                        ArrayList<Channel> favChannels = respawn.get("fav channels");
                        ArrayList<Channel> sorted = new ArrayList<>();
                        for (int i = favChannels.size() - 1; i >= 0; i--) {
                            sorted.add(favChannels.get(i));
                            channels.remove(favChannels.get(i));
                        }
                        sorted.addAll(channels);
                        for (int i = 0; i < sorted.size(); i++) {
                            if (i < favChannels.size()) {
                                System.out.println("\t" + (i + 1) + ". " + sorted.get(i).name + "(favorite channel)");
                            } else {
                                System.out.println("\t" + (i + 1) + ". " + sorted.get(i).name);
                            }
                        }
                        int index = Integer.parseInt(input.nextLine());
                        while (index < 0 || index > sorted.size()) {
                            System.out.println("invalid index");
                            index = Integer.parseInt(input.nextLine());
                        }
                        if (index == 0) {
                            break;
                        }
                        Channel channel = sorted.get(index - 1);
                        System.out.println("0.back to channel lists\n1.see channel\n2.edit channel\n3.add to favorite\n4.subscribe");
                        index = Integer.parseInt(input.nextLine());
                        while (index < 0 || index > 4) {
                            System.out.println("invalid index");
                            index = Integer.parseInt(input.nextLine());
                        }
                        if(index == 0){
                            continue;
                        }
                        else if(index == 1){
                            while (true){
                                System.out.println(channel.name + "\n" + channel.description);
                                System.out.println("choose one of posts (by its index) to see (or type zero to back to menu) :");
                                for (int i=0 ; i<channel.posts.size() ; i++) {
                                    Post tmp = channel.posts.get(i);
                                    System.out.println("\t" + (i+1) + ". " + tmp.title);
                                    System.out.println("\t\t" + tmp.body);
                                }
                                int ind = Integer.parseInt(input.nextLine());
                                while (ind < 0 || ind > channel.posts.size()) {
                                    System.out.println("invalid index");
                                    ind = Integer.parseInt(input.nextLine());
                                }
                                if (ind == 0) {
                                    break;
                                }
                                Post post = channel.posts.get(ind - 1);
                                System.out.println("0.back to channel\n1.see post\n2.like\n3.dislike\n4.delete\n5.save");
                                index = Integer.parseInt(input.nextLine());
                                while (index < 0 || index > 5) {
                                    System.out.println("invalid index");
                                    index = Integer.parseInt(input.nextLine());
                                }
                                if(index == 0){
                                    continue;
                                }
                                else if(index == 1){
                                    System.out.println(post.channelName + " / " +post.writer + ": " + post.title + "\n" + post.body);
                                    System.out.println(post.time + "\tlikes : " + post.likes + "\tdislikes : " + post.dislikes );
                                    System.out.println("choose one of comments (by its index) to see (or type zero to back to channel) :");
                                    for (int i=0 ; i<post.comments.size() ; i++) {
                                        Comment tmp = post.comments.get(i);
                                        System.out.println("\t" + (i+1) + ". " + tmp.writer + " : " + tmp.content);
                                        System.out.println("\t\tlikes : " + tmp.likes + "\tdislikes : " + tmp.dislikes);
                                    }
                                    index = Integer.parseInt(input.nextLine());
                                    while (index < 0 || index > post.comments.size()) {
                                        System.out.println("invalid index");
                                        index = Integer.parseInt(input.nextLine());
                                    }
                                    if (index == 0) {
                                        break;
                                    }
                                    Comment comment = post.comments.get(index-1);
                                    System.out.println("0.back to channel\n1.like\n2.dislike");
                                    index = Integer.parseInt(input.nextLine());
                                    while (index < 0 || index > 2) {
                                        System.out.println("invalid index");
                                        index = Integer.parseInt(input.nextLine());
                                    }
                                    if(index == 0){
                                        continue;
                                    }
                                    else if(index == 1){
                                        HashMap<Object, Object> like = new HashMap<>();
                                        like.put("type", "like comment");
                                        like.put("comment" , comment);
                                        Packet likePacket = new Packet(like);
                                        out.writeObject(likePacket);
                                        Packet inDisPacket = (Packet) ois.readObject();
                                        String finalRespawn = (String) inDisPacket.object;
                                        System.out.println(finalRespawn);
                                    }
                                    else {
                                        HashMap<Object, Object> dislike = new HashMap<>();
                                        dislike.put("type", "dislike comment");
                                        dislike.put("comment" , comment);
                                        Packet dislikePacket = new Packet(dislike);
                                        out.writeObject(dislikePacket);
                                        Packet inDisPacket = (Packet) ois.readObject();
                                        String finalRespawn = (String) inDisPacket.object;
                                        System.out.println(finalRespawn);
                                    }
                                }
                                else if(index == 2){
                                    HashMap<Object, Object> like = new HashMap<>();
                                    like.put("type", "like post");
                                    like.put("post" , post);
                                    Packet likePostPacket = new Packet(like);
                                    out.writeObject(likePostPacket);
                                    Packet inDisPacket = (Packet) ois.readObject();
                                    String finalRespawn = (String) inDisPacket.object;
                                    System.out.println(finalRespawn);
                                }
                                else if(index == 3){
                                    HashMap<Object, Object> dislike = new HashMap<>();
                                    dislike.put("type", "dislike post");
                                    dislike.put("post" , post);
                                    Packet dislikePostPacket = new Packet(dislike);
                                    out.writeObject(dislikePostPacket);
                                    Packet inDisPacket = (Packet) ois.readObject();
                                    String finalRespawn = (String) inDisPacket.object;
                                    System.out.println(finalRespawn);
                                }
                                else if(index == 4){
                                    HashMap<Object, Object> delete = new HashMap<>();
                                    delete.put("type", "delete post");
                                    delete.put("post" , post);
                                    Packet deletePostPacket = new Packet(delete);
                                    out.writeObject(deletePostPacket);
                                    Packet inDeletePacket = (Packet) ois.readObject();
                                    String finalRespawn = (String) inDeletePacket.object;
                                    System.out.println(finalRespawn);
                                }
                                else {
                                    HashMap<Object, Object> save = new HashMap<>();
                                    save.put("type", "save");
                                    save.put("post" , post);
                                    Packet savePostPacket = new Packet(save);
                                    out.writeObject(savePostPacket);
                                    Packet inSavePacket = (Packet) ois.readObject();
                                    String finalRespawn = (String) inSavePacket.object;
                                    System.out.println(finalRespawn);
                                }
                            }
                        }
                        else if(index == 2){
                            System.out.println("type your new channel name :");
                            String channelName = input.nextLine();
                            while (channelName.length() == 0){
                                System.out.println("channelName cant be empty. type it again :");
                                channelName = input.nextLine();
                            }
                            System.out.println("type your new channel description :");
                            String description = input.nextLine();
                            while (description.length() == 0){
                                System.out.println("description cant be empty. type it again :");
                                description = input.nextLine();
                            }
                            HashMap<Object, Object> edit = new HashMap<>();
                            edit.put("type", "edit channel");
                            edit.put("name", channelName);
                            edit.put("description", description);
                            edit.put("channelName" , channel.name);
                            Packet addPostPacket = new Packet(edit);
                            out.writeObject(addPostPacket);
                            Packet inEditPacket = (Packet) ois.readObject();
                            String finalRespawn = (String) inEditPacket.object;
                            System.out.println(finalRespawn);
                        }
                        else if(index == 3){
                            HashMap<Object, Object> add = new HashMap<>();
                            add.put("type", "add to favorite");
                            add.put("channelName" , channel.name);
                            Packet addPostPacket = new Packet(add);
                            out.writeObject(addPostPacket);
                            Packet inAddPacket = (Packet) ois.readObject();
                            String finalRespawn = (String) inAddPacket.object;
                            System.out.println(finalRespawn);
                        }
                        else {
                            HashMap<Object, Object> sub = new HashMap<>();
                            sub.put("type", "subscribe");
                            sub.put("channelName" , channel.name);
                            Packet addPostPacket = new Packet(sub);
                            out.writeObject(addPostPacket);
                            Packet inSubPacket = (Packet) ois.readObject();
                            String finalRespawn = (String) inSubPacket.object;
                            System.out.println(finalRespawn);
                        }
                    }
                } else if (in.equals("3") || in.equals("add post")) {
                    System.out.println("choose on of channels (by its index) to share your post in (or type zero to back to first menu) :");
                    HashMap<Object, Object> data = new HashMap<>();
                    data.put("type", "channels");
                    Packet packet = new Packet(data);
                    out.writeObject(packet);
                    Packet inPacket = (Packet) ois.readObject();
                    HashMap <String , ArrayList<Channel>> respawn = (HashMap<String , ArrayList<Channel>>) inPacket.object;
                    ArrayList <Channel> channels = respawn.get("channels");
                    ArrayList <Channel> favChannels = respawn.get("fav channels");
                    ArrayList <Channel> sorted = new ArrayList<>();
                    for (int i = favChannels.size()-1 ; i>=0 ; i--) {
                        sorted.add(favChannels.get(i));
                        channels.remove(favChannels.get(i));
                    }
                    sorted.addAll(channels);
                    for (int i=0 ; i<sorted.size() ; i++) {
                        if(i < favChannels.size()){
                            System.out.println("\t" + (i + 1) + ". " + sorted.get(i).name + "(favorite channel)");
                        }
                        else {
                            System.out.println("\t" + (i + 1) + ". " + sorted.get(i).name);
                        }
                    }
                    int index = Integer.parseInt(input.nextLine());
                    while (index < 0 || index > sorted.size()){
                        System.out.println("invalid index");
                        index = Integer.parseInt(input.nextLine());
                    }
                    if(index == 0){
                        continue;
                    }
                    Channel channel = sorted.get(index-1);
                    System.out.println("type your post title :");
                    String title = input.nextLine();
                    while (title.length() == 0){
                        System.out.println("title cant be empty. type it again :");
                        title = input.nextLine();
                    }
                    System.out.println("type your post body");
                    String body = input.nextLine();
                    while (body.length() == 0){
                        System.out.println("body cant be empty. type it again :");
                        body = input.nextLine();
                    }
                    HashMap<Object, Object> addPost = new HashMap<>();
                    addPost.put("type", "add post");
                    addPost.put("title", title);
                    addPost.put("body", body);
                    addPost.put("channelName", channel.name);
                    Packet addPostPacket = new Packet(addPost);
                    out.writeObject(addPostPacket);
                    Packet inAddPostPacket = (Packet) ois.readObject();
                    String finalRespawn = (String) inAddPostPacket.object;
                    if(finalRespawn.equals("done")){
                        System.out.println("post successfully added");
                    }
                } else if (in.equals("4") || in.equals("setting")){
                    while (true) {
                        System.out.println("1.edit profile\n2.make channel\n3.saved posts\n4.about us\n5.back");
                        String choice = input.nextLine();
                        if(choice.equals("1") || choice.equals("edit profile")){
                            while (true) {
                                System.out.println("enter your new username :");
                                String username = input.nextLine();
                                System.out.println("enter your new password (with at least one digit , one capital and one small letter) :");
                                String password = input.nextLine();
                                System.out.println("enter your new email :");
                                String email = input.nextLine();
                                HashMap<Object, Object> data = new HashMap<>();
                                data.put("type", "edit profile");
                                data.put("username", username);
                                data.put("password", password);
                                data.put("email", email);
                                Packet packet = new Packet(data);
                                out.writeObject(packet);
                                Packet inPacket = (Packet) ois.readObject();
                                String respawn = (String) inPacket.object;
                                if (respawn.equals("done")) {
                                    System.out.println("successfully changed");
                                    break;
                                } else {
                                    System.out.println(respawn);
                                }
                            }
                        } else if(choice.equals("2") || choice.equals("make channel")){
                            System.out.println("type your channel name :");
                            String channelName = input.nextLine();
                            while (channelName.length() == 0){
                                System.out.println("channelName cant be empty. type it again :");
                                channelName = input.nextLine();
                            }
                            System.out.println("type your channel description :");
                            String description = input.nextLine();
                            while (description.length() == 0){
                                System.out.println("description cant be empty. type it again :");
                                description = input.nextLine();
                            }
                            HashMap<Object, Object> addPost = new HashMap<>();
                            addPost.put("type", "make channel");
                            addPost.put("name", channelName);
                            addPost.put("description", description);
                            Packet addPostPacket = new Packet(addPost);
                            out.writeObject(addPostPacket);
                            Packet inAddPostPacket = (Packet) ois.readObject();
                            String finalRespawn = (String) inAddPostPacket.object;
                            if(finalRespawn.equals("done")){
                                System.out.println("channel successfully made");
                            }
                        } else if(choice.equals("3") || choice.equals("saved posts")){
                            while (true) {
                                HashMap<Object, Object> saved = new HashMap<>();
                                saved.put("type", "saved posts");
                                Packet addPostPacket = new Packet(saved);
                                out.writeObject(addPostPacket);
                                Packet inAddPostPacket = (Packet) ois.readObject();
                                ArrayList<Post> savedPosts = (ArrayList<Post>) inAddPostPacket.object;
                                System.out.println("choose one of posts (by its index) to see (or type zero to back to  menu) :");
                                for (int i=0 ; i<savedPosts.size() ; i++) {
                                    Post tmp = savedPosts.get(i);
                                    System.out.println("\t" + (i+1) + ". " + tmp.title);
                                    System.out.println("\t\t" + tmp.body);
                                }
                                int index = Integer.parseInt(input.nextLine());
                                while (index < 0 || index > savedPosts.size()) {
                                    System.out.println("invalid index");
                                    index = Integer.parseInt(input.nextLine());
                                }
                                if (index == 0) {
                                    break;
                                }
                                Post post = savedPosts.get(index - 1);
                                System.out.println("0.back to saved lists\n1.see post\n2.like\n3.dislike\n4.delete");
                                index = Integer.parseInt(input.nextLine());
                                while (index < 0 || index > 4) {
                                    System.out.println("invalid index");
                                    index = Integer.parseInt(input.nextLine());
                                }
                                if(index == 0){
                                    continue;
                                }
                                else if(index == 1){
                                    System.out.println(post.channelName + " / " +post.writer + ": " + post.title + "\n" + post.body);
                                    System.out.println(post.time + "\tlikes : " + post.likes + "\tdislikes : " + post.dislikes );
                                    System.out.println("choose one of comments (by its index) to see (or type zero to back to channel) :");
                                    for (int i=0 ; i<post.comments.size() ; i++) {
                                        Comment tmp = post.comments.get(i);
                                        System.out.println("\t" + (i+1) + ". " + tmp.writer + " : " + tmp.content);
                                        System.out.println("\t\tlikes : " + tmp.likes + "\tdislikes : " + tmp.dislikes);
                                    }
                                    index = Integer.parseInt(input.nextLine());
                                    while (index < 0 || index > post.comments.size()) {
                                        System.out.println("invalid index");
                                        index = Integer.parseInt(input.nextLine());
                                    }
                                    if (index == 0) {
                                        break;
                                    }
                                    Comment comment = post.comments.get(index-1);
                                    System.out.println("0.back to channel\n1.like\n2.dislike");
                                    index = Integer.parseInt(input.nextLine());
                                    while (index < 0 || index > 2) {
                                        System.out.println("invalid index");
                                        index = Integer.parseInt(input.nextLine());
                                    }
                                    if(index == 0){
                                        continue;
                                    }
                                    else if(index == 1){
                                        HashMap<Object, Object> like = new HashMap<>();
                                        like.put("type", "like comment");
                                        like.put("comment" , comment);
                                        Packet likePacket = new Packet(like);
                                        out.writeObject(likePacket);
                                        Packet inDisPacket = (Packet) ois.readObject();
                                        String finalRespawn = (String) inDisPacket.object;
                                        System.out.println(finalRespawn);
                                    }
                                    else {
                                        HashMap<Object, Object> dislike = new HashMap<>();
                                        dislike.put("type", "dislike comment");
                                        dislike.put("comment" , comment);
                                        Packet dislikePacket = new Packet(dislike);
                                        out.writeObject(dislikePacket);
                                        Packet inDisPacket = (Packet) ois.readObject();
                                        String finalRespawn = (String) inDisPacket.object;
                                        System.out.println(finalRespawn);
                                    }
                                }
                                else if(index == 2){
                                    HashMap<Object, Object> like = new HashMap<>();
                                    like.put("type", "like post");
                                    like.put("post" , post);
                                    Packet likePostPacket = new Packet(like);
                                    out.writeObject(likePostPacket);
                                    Packet inDisPacket = (Packet) ois.readObject();
                                    String finalRespawn = (String) inDisPacket.object;
                                    System.out.println(finalRespawn);
                                }
                                else if(index == 3){
                                    HashMap<Object, Object> dislike = new HashMap<>();
                                    dislike.put("type", "dislike post");
                                    dislike.put("post" , post);
                                    Packet dislikePostPacket = new Packet(dislike);
                                    out.writeObject(dislikePostPacket);
                                    Packet inDisPacket = (Packet) ois.readObject();
                                    String finalRespawn = (String) inDisPacket.object;
                                    System.out.println(finalRespawn);
                                }
                                else {
                                    HashMap<Object, Object> delete = new HashMap<>();
                                    delete.put("type", "delete post");
                                    delete.put("post" , post);
                                    Packet deletePostPacket = new Packet(delete);
                                    out.writeObject(deletePostPacket);
                                    Packet inDeletePacket = (Packet) ois.readObject();
                                    String finalRespawn = (String) inDeletePacket.object;
                                    System.out.println(finalRespawn);
                                }
                            }
                        } else if(choice.equals("4") || choice.equals("about us")){
                            HashMap<Object, Object> about = new HashMap<>();
                            about.put("type", "about us");
                            Packet addPostPacket = new Packet(about);
                            out.writeObject(addPostPacket);
                            Packet inAboutPostPacket = (Packet) ois.readObject();
                            String respawn = (String) inAboutPostPacket.object;
                            System.out.println(respawn);
                        } else if(choice.equals("5") || choice.equals("back")){
                            break;
                        } else {
                            System.out.println("invalid input");
                        }
                    }
                } else if (in.equals("5") || in.equals("exit")){
                    System.out.println("good luck");
                    System.exit(0);
                } else {
                    System.out.println("invalid input");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
