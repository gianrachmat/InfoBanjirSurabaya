package gq.gianr.infobanjirsurabaya.presenter.impl;

import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import gq.gianr.infobanjirsurabaya.R;
import gq.gianr.infobanjirsurabaya.interactor.BaseSubscriber;
import gq.gianr.infobanjirsurabaya.interactor.GetUserInfoUseCase;
import gq.gianr.infobanjirsurabaya.model.User;
import gq.gianr.infobanjirsurabaya.presenter.ILoginPresenter;
import gq.gianr.infobanjirsurabaya.ui.view.ILoginView;
import gq.gianr.infobanjirsurabaya.util.StringsUtil;

/**
 * Created by j on 17/03/2017.
 */

public class LoginPresenter implements ILoginPresenter {
    private ILoginView view;
    private CallbackManager fbCallbackManager = CallbackManager.Factory.create();
    private FbFeedFacebookCallback fbFeedFacebookCallback = new FbFeedFacebookCallback();
    private GetUserInfoUseCase useCase = new GetUserInfoUseCase();
    private AccessTokenTracker accessTokenTracker;

    @Override
    public void setView(ILoginView view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null)
                    view.hideUserInfo();
            }
        };
        accessTokenTracker.startTracking();

        if (AccessToken.getCurrentAccessToken() != null)
            useCase.execute(new UserInfoSubscriber());
    }

    @Override
    public void onStop() {
        useCase.unSubscribe();
    }

    @Override
    public void onDestroy() {
        accessTokenTracker.stopTracking();
    }

    @Override
    public void registerFbLoginButtonCallback(LoginButton loginButton) {
        loginButton.registerCallback(fbCallbackManager, fbFeedFacebookCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    class UserInfoSubscriber extends BaseSubscriber<User> {

        @Override
        public void onError(Throwable e) {
            view.showMessage(e.getMessage());
        }

        @Override
        public void onNext(User user) {
            view.showUserInfo(user);
        }
    }

    class FbFeedFacebookCallback implements FacebookCallback<LoginResult> {
        @Override
        public void onSuccess(LoginResult loginResult) {
            useCase.execute(new UserInfoSubscriber());
        }

        @Override
        public void onCancel() {
            view.showMessage(StringsUtil.getString(R.string.message_user_canceled_login));
        }

        @Override
        public void onError(FacebookException error) {
            view.showMessage(error.getMessage());
        }
    }
}