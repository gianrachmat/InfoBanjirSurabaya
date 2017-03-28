package gq.gianr.infobanjirsurabaya.data.repository;

import gq.gianr.infobanjirsurabaya.data.api.FacebookFeedApi;
import gq.gianr.infobanjirsurabaya.data.dto.posts.PostsEntity;
import gq.gianr.infobanjirsurabaya.data.dto.user.UserEntity;
import gq.gianr.infobanjirsurabaya.data.mapper.PostsMapper;
import gq.gianr.infobanjirsurabaya.data.mapper.UserMapper;
import gq.gianr.infobanjirsurabaya.model.Posts;
import gq.gianr.infobanjirsurabaya.model.User;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by j on 17/03/2017.
 */

public class UserRepository {
    public Observable<User> getUserInfo() {
        return FacebookFeedApi.getInstance().getUserInfo().map(new Func1<UserEntity, User>() {
            @Override
            public User call(UserEntity userEntity) {
                return UserMapper.transfer(userEntity);
            }
        });
    }

    public Observable<Posts> getUserPosts() {
        return FacebookFeedApi.getInstance().getUserPosts().map(new Func1<PostsEntity, Posts>() {
            @Override
            public Posts call(PostsEntity postsEntity) {
                return PostsMapper.transfer(postsEntity);
            }
        });
    }
}
