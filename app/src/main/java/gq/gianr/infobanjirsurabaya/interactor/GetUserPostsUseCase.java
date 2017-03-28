package gq.gianr.infobanjirsurabaya.interactor;

import gq.gianr.infobanjirsurabaya.data.repository.UserRepository;
import gq.gianr.infobanjirsurabaya.model.Posts;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by j on 17/03/2017.
 */

public class GetUserPostsUseCase {
    private UserRepository userRepository = new UserRepository();
    private Subscription subscription = Subscriptions.empty();

    public void execute(Subscriber<Posts> subscriber){
        subscription = userRepository.getUserPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void unSubscribe(){
        if (!subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
