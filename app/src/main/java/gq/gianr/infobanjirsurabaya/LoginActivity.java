package gq.gianr.infobanjirsurabaya;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import gq.gianr.infobanjirsurabaya.model.User;

public class LoginActivity extends BaseActivity{
    private static final String TAG = "FacebookLogin";
    // [START declare_auth]

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.bt_fb_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                update(null);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                update(null);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        update(user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token){
        Log.d(TAG, "facebook:onError"+token);
        showProgressDialog();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            update(user);
                        } else {
                            Log.d(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Login Gagal",
                                    Toast.LENGTH_SHORT).show();
                            update(null);
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void update(FirebaseUser user){
        hideProgressDialog();
        if (user != null){
            Log.i(TAG, "user email : "+user.getEmail());
            System.out.println(user.toString());
            FirebaseMessaging.getInstance().subscribeToTopic("news");
            String msg = getString(R.string.msg_subscribed);
            Log.d(TAG, msg);
            onAuthSuccess(user);

        } else {
            Toast.makeText(this, "Gagal, coba lagi.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }

    public boolean isLoggedIn(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }


    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        setContentView(R.layout.activity_login);
//        if (isLoggedIn()){
//            showPostFragment();
//        } else showLoginFragment();
//    }
//
//    public boolean isLoggedIn(){
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        return accessToken != null;
//    }
//
//    public void showLoginFragment(){
//        getSupportFragmentManager().beginTransaction().
//                add(R.id.activity_login, LoginFragment.newInstance(), LoginFragment.TAG).commit();
//    }
//
//    public void showPostFragment(){
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
