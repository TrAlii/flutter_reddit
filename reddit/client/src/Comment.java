import java.io.Serializable;

public class Comment implements Serializable {
    Post post;
    String writer;
    String content;
    int likes;
    int dislikes;

    public Comment(String writer , String content , Post post){
        this.writer = writer;
        this.content = content;
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (post != null ? !post.equals(comment.post) : comment.post != null) return false;
        if (writer != null ? !writer.equals(comment.writer) : comment.writer != null) return false;
        return content != null ? content.equals(comment.content) : comment.content == null;
    }

    @Override
    public int hashCode() {
        int result = post != null ? post.hashCode() : 0;
        result = 31 * result + (writer != null ? writer.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
