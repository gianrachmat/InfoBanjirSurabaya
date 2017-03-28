package gq.gianr.infobanjirsurabaya.presenter;

import gq.gianr.infobanjirsurabaya.ui.view.IFeedView;

/**
 * Created by j on 17/03/2017.
 */

public interface IFeedPresenter extends IBasePresenter<IFeedView> {
    void getPosts();
}
