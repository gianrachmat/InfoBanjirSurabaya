package gq.gianr.infobanjirsurabaya.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j on 17/03/2017.
 */

public class Posts {
    private String previous;
    private String next;
    private List<Post> posts = new ArrayList<>();

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
