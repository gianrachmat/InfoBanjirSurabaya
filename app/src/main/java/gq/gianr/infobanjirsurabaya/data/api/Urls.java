package gq.gianr.infobanjirsurabaya.data.api;

/**
 * Created by j on 17/03/2017.
 */

public class Urls {
        public static final String BASE_URL = "https://graph.facebook.com/v2.7/";

        public static final String USER_INFO = "me/?fields=picture.type(large),name";
        //    public static final String POSTS = "227268729878/?fields=posts{permalink_url,full_picture,message,created_time}";
        public static final String POSTS = "227268729878/?fields=posts{permalink_url,full_picture,message,created_time}";
}
