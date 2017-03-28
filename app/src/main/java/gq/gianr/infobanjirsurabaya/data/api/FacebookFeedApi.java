package gq.gianr.infobanjirsurabaya.data.api;

import com.facebook.AccessToken;

import gq.gianr.infobanjirsurabaya.data.dto.posts.PostsEntity;
import gq.gianr.infobanjirsurabaya.data.dto.user.UserEntity;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by j on 17/03/2017.
 */

public class FacebookFeedApi {
    private static FacebookFeedApi instance;
    private RetrofitFacebookFeedRestApi retrofitFacebookFeedRestApi;

    private FacebookFeedApi(){
        retrofitFacebookFeedRestApi = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(RetrofitFacebookFeedRestApi.class);
    }

    public static FacebookFeedApi getInstance() {
        if (instance == null)
            instance = new FacebookFeedApi();
        return instance;
    }

    public Observable<UserEntity> getUserInfo() {
        return retrofitFacebookFeedRestApi.getUserInfo(AccessToken.getCurrentAccessToken().getToken());
    }

    public Observable<PostsEntity> getUserPosts() {
        return retrofitFacebookFeedRestApi.getUserPosts(AccessToken.getCurrentAccessToken().getToken());
    }
}
