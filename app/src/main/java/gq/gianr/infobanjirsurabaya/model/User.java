package gq.gianr.infobanjirsurabaya.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by j on 17/03/2017.
 */
@IgnoreExtraProperties
public class User {
    public String username;
    private String profileImageUrl;
    public String email;

    public User() {}

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
