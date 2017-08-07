package gq.gianr.infobanjirsurabaya;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;


public class PostDetailActivity extends EasyLocationAppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PostDetailActivity";

    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;
    private DatabaseReference mDatabase;
    private Post post;
    private String mPostKey;
    private TextView mAuthorView;
    private TextView mTitleView;
    private TextView mBodyView;
    private ImageView imageView;
    private ImageView hoax;
    private ImageView star;
    Location locs;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showProgressDialog();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(5000);

        EasyLocationRequest easyLocationRequest = new EasyLocationRequestBuilder()
                .setLocationRequest(locationRequest)
                .setFallBackToLastLocationTime(3000)
                .build();

        requestSingleLocationFix(easyLocationRequest);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostKey);
        // Initialize Views
        mAuthorView = (TextView) findViewById(R.id.post_author);
        mTitleView = (TextView) findViewById(R.id.post_title);
        mBodyView = (TextView) findViewById(R.id.post_body);
        imageView = (ImageView) findViewById(R.id.post_picture_detail);
        hoax = (ImageView) findViewById(R.id.hoax_detail);
        star = (ImageView) findViewById(R.id.star_detail);

        hoax.setOnClickListener(this);
        star.setOnClickListener(this);

    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        SmartLocation.with(PostDetailActivity.this).location()
//                .oneFix().start(new OnLocationUpdatedListener() {
//                    @Override
//                    public void onLocationUpdated(Location location) {
//                        locs = location;
//                        setContent();
////                        hideProgressDialog();
//                    }
//                });
//        setContent();
        // Add value event listener to the post
        // [START post_value_event_listener]

    }

    void setContent() {
        hideProgressDialog();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                post = dataSnapshot.getValue(Post.class);
                // [START_EXCLUDE]
                mAuthorView.setText(post.author);
                mTitleView.setText(post.title);
                mBodyView.setText(post.body);
                Glide.with(PostDetailActivity.this)
                        .load(post.url_picture)
                        .centerCrop()
                        .into(imageView);

//                float[] results = new float[1];
//                if (locs!=null) {
//                    Location.distanceBetween(post.lat, post.lng, locs.getLatitude(), locs.getLongitude(), results);
//                    Log.i(TAG, "onDataChange: resulst=" + results[0]);
//                    if (results[0] < 500f) {
                if (post.stars.containsKey(getUid())) {
                    star.setImageResource(R.drawable.ic_action_start_full);
                    hoax.setVisibility(View.GONE);
                    star.setVisibility(View.VISIBLE);
                    Log.i(TAG, "populateViewHolder: star");
                } else if (post.hoaxs.containsKey(getUid())) {
                    hoax.setImageResource(R.drawable.ic_action_hoax);
                    star.setVisibility(View.GONE);
                    hoax.setVisibility(View.VISIBLE);
                    Log.i(TAG, "populateViewHolder: hoax");
                } else {
                    star.setVisibility(View.VISIBLE);
                    hoax.setVisibility(View.VISIBLE);
                    star.setImageResource(R.drawable.ic_action_star);
                    hoax.setImageResource(R.drawable.ic_action_hoax_click);
                    Log.i(TAG, "populateViewHolder: not both");
                }
            }
//                    else {
//                        star.setVisibility(View.GONE);
//                        hoax.setVisibility(View.GONE);
//                    }
//                }
//            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void klikButton(DatabaseReference postRef, final View v) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (v.getId() == R.id.star_detail) {
                    if (p.stars.containsKey(getUid())) {
                        // Unstar the post and remove self from stars
                        p.starCount -= 1;
                        p.stars.remove(getUid());
                    } else {
                        // Star the post and add self to stars
                        p.starCount += 1;
                        p.stars.put(getUid(), true);

                    }
                } else if (v.getId() == R.id.hoax_detail) {
                    if (p.hoaxs.containsKey(getUid())) {
                        // Unhoax the post and remove self from hoaxs
                        p.hoaxCount -= 1;
                        p.hoaxs.remove(getUid());

                    } else {
                        // Hoax the post and add self to hoaxs
                        p.hoaxCount += 1;
                        p.hoaxs.put(getUid(), true);

                    }
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
//        SmartLocation.with(this).location().stop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }
    }

    @Override
    public void onClick(View v) {
        DatabaseReference globalPostRef = mPostReference;
        DatabaseReference userPostRef = mDatabase.child("user-posts").child(post.uid).child(mPostReference.getKey());

        klikButton(globalPostRef, v);
        klikButton(userPostRef, v);
    }

    @Override
    public void onLocationPermissionGranted() {

    }

    @Override
    public void onLocationPermissionDenied() {

    }

    @Override
    public void onLocationReceived(Location location) {
        locs = location;
        setContent();
    }

    @Override
    public void onLocationProviderEnabled() {

    }

    @Override
    public void onLocationProviderDisabled() {

    }
}
