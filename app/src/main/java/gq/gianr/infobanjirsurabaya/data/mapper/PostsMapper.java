package gq.gianr.infobanjirsurabaya.data.mapper;

import gq.gianr.infobanjirsurabaya.data.dto.posts.PostEntity;
import gq.gianr.infobanjirsurabaya.data.dto.posts.PostsEntity;
import gq.gianr.infobanjirsurabaya.model.Post;
import gq.gianr.infobanjirsurabaya.model.Posts;
import gq.gianr.infobanjirsurabaya.util.DateFormatter;

/**
 * Created by j on 17/03/2017.
 */

public class PostsMapper {
    public static Posts transfer(PostsEntity postsEntity){
        Posts posts = new Posts();
        posts.setNext(postsEntity.getPosts().getPagingEntity().getNext());
        posts.setPrevious(postsEntity.getPosts().getPagingEntity().getPrevious());

        for (PostEntity postEntity : postsEntity.getPosts().getData()) {
            posts.getPosts().add(transfer(postEntity));
        }

        return posts;
    }
    public static Post transfer(PostEntity postEntity) {
        Post post = new Post();
        post.setMessage(postEntity.getMessage());
        post.setPermalinkUrl(postEntity.getPermalinkUrl());
        post.setPictureUrl(postEntity.getFullPicture());
        post.setCreated(DateFormatter.convertDate(postEntity.getCreatedTime()));

        return post;
    }
}
