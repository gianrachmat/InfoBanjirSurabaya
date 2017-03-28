package gq.gianr.infobanjirsurabaya.data.api;

import gq.gianr.infobanjirsurabaya.data.dto.posts.PostsEntity;
import gq.gianr.infobanjirsurabaya.data.dto.user.UserEntity;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by j on 17/03/2017.
 */

public interface RetrofitFacebookFeedRestApi {
    @GET(Urls.USER_INFO)
    Observable<UserEntity> getUserInfo(@Query("access_token") String accessToken);

    @GET(Urls.POSTS)
    Observable<PostsEntity> getUserPosts(@Query("access_token") String accessToken);

}
