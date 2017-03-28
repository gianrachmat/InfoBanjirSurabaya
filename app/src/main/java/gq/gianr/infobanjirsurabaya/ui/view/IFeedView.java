package gq.gianr.infobanjirsurabaya.ui.view;

import gq.gianr.infobanjirsurabaya.model.Posts;

/**
 * Created by j on 17/03/2017.
 */

public interface IFeedView extends IBaseView {

    void initPostsAdapter();

    void showPosts(Posts posts);
}
