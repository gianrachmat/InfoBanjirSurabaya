package gq.gianr.infobanjirsurabaya.presenter;

import android.content.Intent;

import com.facebook.login.widget.LoginButton;

import gq.gianr.infobanjirsurabaya.ui.view.ILoginView;

/**
 * Created by j on 17/03/2017.
 */

public interface ILoginPresenter extends IBasePresenter<ILoginView> {
    void registerFbLoginButtonCallback(LoginButton loginButton);

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
