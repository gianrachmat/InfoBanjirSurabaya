package gq.gianr.infobanjirsurabaya;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by j on 24/04/2017.
 */
@IgnoreExtraProperties
public class Post {
    public String uid;
    public String author;
    public String title;
    public String url_picture;
    public String body;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String title, String url_picture, String body) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.url_picture = url_picture;
        this.body = body;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("url_picture", url_picture);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
    // [END post_to_map]

}
