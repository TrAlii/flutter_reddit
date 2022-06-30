import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Server implements Serializable{
    Functions functions;
    ArrayList <Channel> channels = new ArrayList<>();
    transient ArrayList <ClientHandler> clients = new ArrayList<>();
    int port;

    public Server(int port){
        this.port = port;
        try (FileInputStream fis = new FileInputStream("DB\\Func.bin") ; ObjectInputStream ois = new ObjectInputStream(fis)){
            functions = (Functions) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        clients = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("server is running on port :" + port);
            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectOutput out;
        private ObjectInput in;
        private User user = null;

        public ClientHandler(Socket socket) {
            try {
                this.socket = socket;
                this.in = new ObjectInputStream(socket.getInputStream());
                this.out = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void run() {
            clients.add(this);
            try {
                while (true) {
                    Packet packet = (Packet) in.readObject();
                    HashMap data = (HashMap) packet.object;
                    String type = (String) data.get("type");
                    if(type.equals("sign up")){
                        String username = (String) data.get("username");
                        String password = (String) data.get("password");
                        String email = (String) data.get("email");
                        String respond = functions.signUp(username , password , email);
                        if(respond.equals("user successfully signed up")){
                            user = functions.getUser(username , password);
                        }
                        Packet send = new Packet(respond);
                        out.writeObject(send);
                    }
                    else if (type.equals("sign in")){
                        String username = (String) data.get("username");
                        String password = (String) data.get("password");
                        String respond = functions.signIn(username , password);
                        if(respond.equals("signed in")){
                            user = functions.getUser(username , password);
                        }
                        Packet send = new Packet(respond);
                        out.writeObject(send);
                    }
                    else if (type.equals("subscribe")){
                        String channelName = (String) data.get("channelName");
                        for (int i=0 ; i<channels.size() ; i++) {
                            if(channels.get(i).name.equals(channelName)){
                                functions.subscribe(channels.get(i) , user);
                                break;
                            }
                        }
                        Packet send = new Packet("done");
                        out.writeObject(send);
                    }
                    else if (type.equals("feed")){
                        ArrayList <Post> posts = new ArrayList<>();
                        for (int i=0 ; i<user.channels.size() ; i++) {
                            posts.addAll(user.channels.get(i).posts);
                        }
                        Post[] sort = new Post[posts.size()];
                        for (int i=0 ; i<posts.size() ; i++) {
                            sort[i] = posts.get(i);
                        }
                        for (int i=0 ; i<sort.length-1 ; i++) {
                            for (int j=0 ; j<sort.length ; j++) {
                                if(sort[i].time.compareTo(sort[j].time) < 0){
                                    Post temp = sort[i];
                                    sort[i] = sort[j];
                                    sort[j] = temp;
                                }
                            }
                        }
                        Packet send = new Packet(sort);
                        out.writeObject(send);
                    }
                    else if (type.equals("channels")){
                        HashMap <String , ArrayList<Channel>> hashMap = new HashMap<>();
                        hashMap.put("channels" , channels);
                        hashMap.put("fav channels" , user.favoriteChannels);
                        Packet send = new Packet(hashMap);
                        out.writeObject(send);
                    }
                    else if (type.equals("delete post")){
                        Post post = (Post) data.get("post");
                        Channel channel = null;
                        for (int i=0 ; i<channels.size() ; i++) {
                            if(channels.get(i).name.equals(post.channelName)){
                                channel = channels.get(i);
                                break;
                            }
                        }
                        String respond = functions.deletePost(channel , post , user);
                        Packet send = new Packet(respond);
                        out.writeObject(send);
                    }
                    else if (type.equals("edit channel")){
                        String name = (String) data.get("name");
                        String description = (String) data.get("description");
                        String channelName = (String) data.get("channelName");
                        Channel channel = null;
                        for (int i=0 ; i<channels.size() ; i++) {
                            if(channels.get(i).name.equals(channelName)){
                                channel = channels.get(i);
                                break;
                            }
                        }
                        String respond = functions.editChannel(channel , user , name , description);
                        Packet send = new Packet(respond);
                        out.writeObject(send);
                    }
                    else if (type.equals("add post")){
                        String title = (String) data.get("title");
                        String body = (String) data.get("body");
                        String channelName = (String) data.get("channelName");
                        Channel channel = null;
                        for (int i=0 ; i<channels.size() ; i++) {
                            if(channels.get(i).name.equals(channelName)){
                                channel = channels.get(i);
                                break;
                            }
                        }
                        functions.addPost(channel , new Post(channel.name , user.username, title , body));
                        Packet send = new Packet("done");
                        out.writeObject(send);
                    }
                    else if (type.equals("edit profile")){
                        String username = (String) data.get("username");
                        String password = (String) data.get("password");
                        String email = (String) data.get("email");
                        Packet send;
                        if(functions.validPassword(password) && Pattern.matches("[A-Za-z0-9._-]+@[A-Za-z0-9]+\\.[A-Za-z]{2,4}" , email)) {
                            user.username = username;
                            user.password = password;
                            user.email = email;
                            send = new Packet("done");
                        }
                        else {
                            send = new Packet("invalid email or password type. try again");
                        }
                        out.writeObject(send);
                    }
                    else if (type.equals("make channel")){
                        String name = (String) data.get("name");
                        String description = (String) data.get("description");
                        Channel channel = new Channel(name , user , description);
                        channels.add(channel);
                        Packet send = new Packet("done");
                        out.writeObject(send);
                    }
                    else if (type.equals("save")){
                        Post post = (Post) data.get("post");
                        user.savedPosts.add(post);
                        Packet send = new Packet("done");
                        out.writeObject(send);
                    }
                    else if (type.equals("saved posts")){
                        Packet send = new Packet(user.savedPosts);
                        out.writeObject(send);
                    }
                    else if(type.equals("add to favorite")){
                        String channelName = (String) data.get("channelName");
                        Channel channel = null;
                        for (int i=0 ; i<channels.size() ; i++) {
                            if(channels.get(i).name.equals(channelName)){
                                channel = channels.get(i);
                                break;
                            }
                        }
                        user.favoriteChannels.add(channel);
                        Packet send = new Packet("done");
                        out.writeObject(send);
                    }
                    else if(type.equals("about us")){
                        Packet send = new Packet("writers :\n\tmohammad hosein arian poor\n\tali zarneshani\nsupervisor :\n\tdr.vahidi");
                        out.writeObject(send);
                    }
                    else if(type.equals("like post")){
                        Post post = (Post) data.get("post");
                        Channel channel = null;
                        for (int i=0 ; i<channels.size() ; i++) {
                            if(channels.get(i).name.equals(post.channelName)){
                                channel = channels.get(i);
                                break;
                            }
                        }
                        for (int i=0 ; i<channel.posts.size() ; i++) {
                            if(channel.posts.get(i).equals(post)){
                                channel.posts.get(i).likes++;
                            }
                        }
                        Packet send = new Packet("done");
                        out.writeObject(send);
                    }
                    else if(type.equals("dislike post")){
                        Post post = (Post) data.get("post");
                        Channel channel = null;
                        for (int i=0 ; i<channels.size() ; i++) {
                            if(channels.get(i).name.equals(post.channelName)){
                                channel = channels.get(i);
                                break;
                            }
                        }
                        for (int i=0 ; i<channel.posts.size() ; i++) {
                            if(channel.posts.get(i).equals(post)){
                                channel.posts.get(i).dislikes++;
                            }
                        }
                        Packet send = new Packet("done");
                        out.writeObject(send);
                    }
                    else if(type.equals("like comment")){
                        Comment comment = (Comment) data.get("comment");
                        Channel channel = null;
                        for (int i=0 ; i<channels.size() ; i++) {
                            if(channels.get(i).name.equals(comment.post.channelName)){
                                channel = channels.get(i);
                                break;
                            }
                        }
                        Post post = null;
                        for (int i=0 ; i<channel.posts.size() ; i++) {
                            if(channel.posts.get(i).equals(comment.post)){
                                post = channel.posts.get(i);
                                break;
                            }
                        }
                        for (int i=0 ; i<post.comments.size() ; i++) {
                            if(post.comments.get(i).equals(comment)){
                                post.comments.get(i).likes++;
                            }
                        }
                        Packet send = new Packet("done");
                        out.writeObject(send);
                    }
                    else if(type.equals("dislike comment")){
                        Comment comment = (Comment) data.get("comment");
                        Channel channel = null;
                        for (int i=0 ; i<channels.size() ; i++) {
                            if(channels.get(i).name.equals(comment.post.channelName)){
                                channel = channels.get(i);
                                break;
                            }
                        }
                        Post post = null;
                        for (int i=0 ; i<channel.posts.size() ; i++) {
                            if(channel.posts.get(i).equals(comment.post)){
                                post = channel.posts.get(i);
                                break;
                            }
                        }
                        for (int i=0 ; i<post.comments.size() ; i++) {
                            if(post.comments.get(i).equals(comment)){
                                post.comments.get(i).dislikes++;
                            }
                        }
                        Packet send = new Packet("done");
                        out.writeObject(send);
                    }
                    update();
                }
            } catch (Exception e) {
                System.out.println("connection interrupted");
            }
            clients.remove(this);
        }
    }

    synchronized public void update(){
        try (FileOutputStream fos = new FileOutputStream("DB\\Server.bin"); ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (FileOutputStream fos = new FileOutputStream("DB\\Func.bin"); ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(functions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
