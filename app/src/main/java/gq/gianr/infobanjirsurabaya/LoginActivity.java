package gq.gianr.infobanjirsurabaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import gq.gianr.infobanjirsurabaya.ui.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        if (isLoggedIn()){
            showPostFragment();
        } else showLoginFragment();
    }

    public boolean isLoggedIn(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void showLoginFragment(){
        getSupportFragmentManager().beginTransaction().
                add(R.id.activity_login, LoginFragment.newInstance(), LoginFragment.TAG).commit();
    }

    public void showPostFragment(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
