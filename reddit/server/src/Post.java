import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Post implements Serializable {
    String channelName;
    String writer;
    LocalDateTime time;
    String title;
    String body;
    int likes;
    int dislikes;
    int commentsNum;
    ArrayList <Comment> comments = new ArrayList<>();

    public Post(String channelName, String writer, String title, String body) {
        this.channelName = channelName;
        this.writer = writer;
        this.title = title;
        this.body = body;
        time = LocalDateTime.now();
    }

    public void addComment(User user , String content){
        Comment comment = new Comment(user.username , content , this);
        comments.add(comment);
        commentsNum++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (writer != null ? !writer.equals(post.writer) : post.writer != null) return false;
        if (title != null ? !title.equals(post.title) : post.title != null) return false;
        return body != null ? body.equals(post.body) : post.body == null;
    }

    @Override
    public int hashCode() {
        int result = writer != null ? writer.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }
}
