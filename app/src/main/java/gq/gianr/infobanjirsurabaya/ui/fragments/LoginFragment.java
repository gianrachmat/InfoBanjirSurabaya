package gq.gianr.infobanjirsurabaya.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.login.widget.LoginButton;

import gq.gianr.infobanjirsurabaya.LoginActivity;
import gq.gianr.infobanjirsurabaya.R;
import gq.gianr.infobanjirsurabaya.model.User;
import gq.gianr.infobanjirsurabaya.presenter.ILoginPresenter;
import gq.gianr.infobanjirsurabaya.presenter.impl.LoginPresenter;
import gq.gianr.infobanjirsurabaya.ui.view.ILoginView;

/**
 * Created by j on 17/03/2017.
 */

public class LoginFragment extends BaseFragment implements ILoginView{
    ILoginPresenter presenter;
    public static final String TAG = LoginFragment.class.getSimpleName();
    LoginButton loginButton;
    Button lewati;

    public static LoginFragment newInstance(){ return new LoginFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginPresenter();
        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        initComponents(view);
        return view;
    }

    @Override
    public void showUserInfo(User user) {

    }

    @Override
    public void hideUserInfo() {

    }

    @Override
    public void showPosts() {
//        ((LoginActivity) getActivity()).showPostFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
        showPosts();
    }

    @Override
    public void initComponents(View view) {
        loginButton = (LoginButton) view.findViewById(R.id.bt_fb_login);
        loginButton.setReadPermissions(getResources().getString(R.string.fb_permissions));
        loginButton.setFragment(this);
        presenter.registerFbLoginButtonCallback(loginButton);
    }
}
