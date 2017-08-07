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
    public String alamat;
    public String tanggal;
    public Boolean verified;
    public double lat;
    public double lng;
    public int starCount = 0;
    public int hoaxCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();
    public Map<String, Boolean> hoaxs = new HashMap<>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String title, String url_picture, String body, String alamat, String tanggal, Boolean verified, double lat, double lng) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.url_picture = url_picture;
        this.body = body;
        this.alamat = alamat;
        this.tanggal = tanggal;
        this.verified = verified;
        this.lat = lat;
        this.lng = lng;
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
        result.put("alamat", alamat);
        result.put("tanggal", tanggal);
        result.put("verified", verified);
        result.put("lat", lat);
        result.put("lng", lng);
        result.put("starCount", starCount);
        result.put("hoaxCount", hoaxCount);
        result.put("stars", stars);
        result.put("hoaxs", hoaxs);

        return result;
    }
    // [END post_to_map]

}
