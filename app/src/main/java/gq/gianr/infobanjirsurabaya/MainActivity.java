package gq.gianr.infobanjirsurabaya;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.internal.ServerProtocol;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import gq.gianr.infobanjirsurabaya.model.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GraphRequest.GraphJSONObjectCallback, GraphRequest.Callback {
    FragmentManager fragmentManager;
    String TAG = "gq.gianr.infobanjirsurabaya.IP_ADDRESS";
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    View hView;
    SharedPreferences sp;
    EditText ip;
    String ips;
    public static Context contextOfApplication;

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contextOfApplication = getApplicationContext();
        sp = getSharedPreferences(this.getPackageName()+"_preferences",MODE_PRIVATE);
        setTitle(R.string.app_name);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        TabFragment f = new TabFragment();
        Bundle args = new Bundle();
        args.putInt("pos", 0);
        f.setArguments(args);
        fragmentTransaction.replace(R.id.containerView, f).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        hView = navigationView.getHeaderView(0);

        request();

        navigationView.setNavigationItemSelectedListener(this);
    }

    void request(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), this);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        request.setParameters(parameters);
        request.executeAsync();

        request = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(), "/me/picture", this);
        parameters = new Bundle();
        parameters.putString("redirect", "false");
        parameters.putString("height", "279");
        parameters.putString("width", "279");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ip);
        dialog.show();
        ip = (EditText) dialog.findViewById(R.id.ip_address);
        ip.setText(sp.getString("ip_address", ""));
        ips = ip.getText().toString();
        Log.i("dialog","ips:"+ips+", editText:"+ip.getText().toString());
        Button simpan = (Button) dialog.findViewById(R.id.btn_simpan);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(ip.getText().toString());
                Log.i("dialog","ip:"+sp.getString("ip_address", "")+", ips:"+ip.getText().toString());
                dialog.dismiss();
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        TabFragment f = new TabFragment();
        Bundle args = new Bundle();
        if (id == R.id.nav_home) {
            args.putInt("pos", 0);
            f.setArguments(args);
            fragmentTransaction.replace(R.id.containerView, f).addToBackStack("home").commit();
        } else if (id == R.id.nav_cuaca) {
            args.putInt("pos", 1);
            f.setArguments(args);
            fragmentTransaction.replace(R.id.containerView, f).addToBackStack("cuaca").commit();
        } else if (id == R.id.nav_daerah) {
            fragmentTransaction.replace(R.id.containerView, new RawanFragment()).addToBackStack("daerah").commit();
        } else if (id == R.id.nav_lapor) {
            fragmentTransaction.replace(R.id.containerView, new LaporFragment()).addToBackStack("lapor").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showUserInfo(User user) {
        TextView te = (TextView) hView.findViewById(R.id.namaUser);
        te.setText(user.getUsername());
        te = (TextView) hView.findViewById(R.id.emailUser);
        te.setText(user.getEmail());
        Picasso.with(this)
                .load(user.getProfileImageUrl())
                .fit()
                .error(R.drawable.ic_action_remove)
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .transform(new RoundedTransformationBuilder()
                        .borderColor(Color.BLACK)
                        .borderWidthDp(3)
                        .cornerRadiusDp(30)
                        .oval(false)
                        .build())
                .into((ImageView) findViewById(R.id.fotoUserRound));
    }

    void save(String s){
        SharedPreferences.Editor editor = getSharedPreferences(this.getPackageName()+"_preferences",MODE_PRIVATE).edit();
        System.out.println(getPackageName());
        editor.putString("ip_address", s);
        editor.apply();

        Log.i("dialog","ip:"+sp.getString("ip_address", ""));
    }

    void saveToPreferences(User user){
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("user_pic_url", user.getProfileImageUrl());
        editor.putString("user_name", user.getUsername());
        editor.putString("user_email", user.getEmail());
        editor.apply();
    }

    User loadFromPreferences(){
        User user = new User();
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        user.setUsername(sp.getString("user_name", null));
        user.setEmail(sp.getString("user_email", null));
        user.setProfileImageUrl(sp.getString("user_pic_url", null));
        return user;
    }

    @Override
    public void onCompleted(JSONObject object, GraphResponse response) {
        try {
            if (object != null) {
                Log.i("json", "graphrequest = " + object.toString() + ", code = " + response.getConnection().getResponseCode());
                user.setUsername(object.getString("name"));
                Log.i("json", "name = " + user.getUsername());
                if (object.has("email")) {
                    user.setEmail(object.getString("email"));
                } else user.setEmail("");
                Log.i("json", "email = " + user.getEmail());
            } else {
                user = loadFromPreferences();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        showUserInfo(user);
        saveToPreferences(user);
    }

    @Override
    public void onCompleted(GraphResponse response) {
        try {
            if (response.getJSONObject() != null) {
                Log.i("json", "" + response.getJSONObject().toString());
                user.setProfileImageUrl(response.getJSONObject().getJSONObject("data").getString("url"));
                Log.i("json", "pics url = " + user.getProfileImageUrl());
                Log.i("json", "name = " + user.getUsername());
            } else user = loadFromPreferences();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showUserInfo(user);
        saveToPreferences(user);
    }
}
