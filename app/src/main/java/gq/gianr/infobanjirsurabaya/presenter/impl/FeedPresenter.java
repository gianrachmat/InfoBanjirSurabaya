package gq.gianr.infobanjirsurabaya.presenter.impl;

import gq.gianr.infobanjirsurabaya.interactor.BaseSubscriber;
import gq.gianr.infobanjirsurabaya.interactor.GetUserPostsUseCase;
import gq.gianr.infobanjirsurabaya.model.Posts;
import gq.gianr.infobanjirsurabaya.presenter.IFeedPresenter;
import gq.gianr.infobanjirsurabaya.ui.view.IFeedView;

/**
 * Created by j on 17/03/2017.
 */

public class FeedPresenter implements IFeedPresenter{
    private IFeedView view;
    private GetUserPostsUseCase useCase = new GetUserPostsUseCase();

    @Override
    public void setView(IFeedView view) {
        this.view = view;
    }

    @Override
    public void getPosts() {
        useCase.execute(new GetPostsSubscriber());
    }

    @Override
    public void onStart() {
        getPosts();
    }

    @Override
    public void onStop() {
        useCase.unSubscribe();
    }

    @Override
    public void onDestroy() {

    }

    class GetPostsSubscriber extends BaseSubscriber<Posts> {

        @Override
        public void onError(Throwable e) {
            view.showMessage(e.getMessage());
        }

        @Override
        public void onNext(Posts posts) {
            view.showPosts(posts);
        }
    }
}
