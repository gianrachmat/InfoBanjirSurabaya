package gq.gianr.infobanjirsurabaya;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import gq.gianr.infobanjirsurabaya.model.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    View hView;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.i("json","graphrequest = "+object.toString());
                try {
                    user.setUsername(object.getString("name"));
                    Log.i("json", "name = "+user.getUsername());
                    user.setProfileImageUrl(object.getJSONObject("picture").getJSONObject("data").getString("url"));
                    Log.i("json", "pics url = "+user.getProfileImageUrl());
                    user.setEmail(object.getString("email"));
                    Log.i("json", "email = "+user.getEmail());
                    showUserInfo(user);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,picture,email");
        request.setParameters(parameters);
        request.executeAsync();


        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TabFragment f = new TabFragment();
        Bundle args = new Bundle();
        if (id == R.id.nav_home) {
            args.putInt("pos", 0);
            f.setArguments(args);
            fragmentTransaction.replace(R.id.containerView, f).commit();
        } else if (id == R.id.nav_cuaca) {
            args.putInt("pos", 1);
            f.setArguments(args);
            fragmentTransaction.replace(R.id.containerView, f).commit();
        } else if (id == R.id.nav_daerah) {
            fragmentTransaction.replace(R.id.containerView, new RawanFragment()).commit();
        } else if (id == R.id.nav_lapor) {
            fragmentTransaction.replace(R.id.containerView, new LaporFragment()).commit();
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
        Picasso.with(this).load(user.getProfileImageUrl()).into((ImageView) hView.findViewById(R.id.fotoUser));
    }
}
