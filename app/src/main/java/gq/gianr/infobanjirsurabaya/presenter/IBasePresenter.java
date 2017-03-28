package gq.gianr.infobanjirsurabaya.presenter;

/**
 * Created by j on 17/03/2017.
 */

public interface IBasePresenter<T> {

    void setView(T t);

    void onStart();

    void onStop();

    void onDestroy();
}

