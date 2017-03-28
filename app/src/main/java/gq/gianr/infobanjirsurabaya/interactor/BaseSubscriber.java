package gq.gianr.infobanjirsurabaya.interactor;

import rx.Subscriber;

/**
 * Created by j on 17/03/2017.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {

    }
}
