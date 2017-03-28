package gq.gianr.infobanjirsurabaya.data.mapper;

import gq.gianr.infobanjirsurabaya.data.dto.user.UserEntity;
import gq.gianr.infobanjirsurabaya.model.User;

/**
 * Created by j on 17/03/2017.
 */

public class UserMapper {
    public static User transfer(UserEntity userEntity){
        User user = new User();
        user.setUsername(userEntity.getName());
        user.setProfileImageUrl(userEntity.getPictureEntity().getPictureInfoEntity().getUrl());

        return user;
    }
}
