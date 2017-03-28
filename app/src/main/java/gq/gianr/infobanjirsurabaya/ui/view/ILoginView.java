package gq.gianr.infobanjirsurabaya.ui.view;

import gq.gianr.infobanjirsurabaya.model.User;

/**
 * Created by j on 17/03/2017.
 */

public interface ILoginView extends IBaseView {
    void showUserInfo(User user);

    void hideUserInfo();

    void showPosts();
}
