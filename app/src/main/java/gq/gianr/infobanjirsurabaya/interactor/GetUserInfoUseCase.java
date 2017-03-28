package gq.gianr.infobanjirsurabaya.interactor;

import gq.gianr.infobanjirsurabaya.data.repository.UserRepository;
import gq.gianr.infobanjirsurabaya.model.User;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

/**
 * Created by j on 17/03/2017.
 */

public class GetUserInfoUseCase {
    private UserRepository repository = new UserRepository();
    private Subscription subscription = Subscribers.empty();


    public void execute(Subscriber<User> subscriber) {
        subscription = repository.getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void unSubscribe() {
        if (!subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
