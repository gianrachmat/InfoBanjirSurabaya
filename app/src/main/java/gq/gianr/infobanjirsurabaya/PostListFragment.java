package gq.gianr.infobanjirsurabaya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by j on 24/04/2017.
 */

public abstract class PostListFragment extends Fragment implements GetLocation.GetLoc {
    private static final String TAG = "PostListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]
    private ProgressDialog mProgressDialog;
    GetLocation gl;

    private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private SwipeRefreshLayout srl;
    Location locs;

    public PostListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.primary_layout, container, false);
        showProgressDialog();
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        gl = new GetLocation(this, getContext(), getActivity());
        mRecycler = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecycler.setHasFixedSize(true);

//        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_primary);

        return rootView;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
//        setmAdapter();
    }

    public void setmAdapter() {
        hideProgressDialog();
//        srl.setRefreshing(true);

        Query postsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class, R.layout.text_row_item, PostViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, final Post model, final int position) {
                final DatabaseReference postRef = getRef(position);
                float[] results = new float[1];

                if (locs != null) {
                    Location.distanceBetween(model.lat, model.lng, locs.getLatitude(), locs.getLongitude(), results);
                    Log.i(TAG, "onDataChange: results=" + results[0]);
                    if (results[0] < 500f) {
                        // Set click listener for the whole post view
                        final String postKey = postRef.getKey();
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "Klik pada item " + position + ", postkey " + postKey, Toast.LENGTH_SHORT).show();
                                // Launch PostDetailActivity
                                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                                intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                                startActivity(intent);
                            }
                        });

                        DatabaseReference globalPostRef = mDatabase.child("posts").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("user-posts").child(model.uid).child(postRef.getKey());

                        if (model.hoaxCount < model.starCount) {
                            viewHolder.veriView.setBackgroundResource(R.drawable.ic_action_verified);
                            globalPostRef.child("verified").setValue(true);
                            userPostRef.child("verified").setValue(true);
                        } else {
                            viewHolder.veriView.setBackgroundResource(R.drawable.ic_action_unveri);
                            globalPostRef.child("verified").setValue(false);
                            userPostRef.child("verified").setValue(false);
                        }

                        if (model.stars.containsKey(getUid())) {
                            viewHolder.starView.setImageResource(R.drawable.ic_action_start_full);
                            viewHolder.hoaxView.setVisibility(View.GONE);
                            viewHolder.starView.setVisibility(View.VISIBLE);
                            Log.i(TAG, "populateViewHolder: star");
                        } else if (model.hoaxs.containsKey(getUid())) {
                            viewHolder.hoaxView.setImageResource(R.drawable.ic_action_hoax);
                            viewHolder.starView.setVisibility(View.GONE);
                            viewHolder.hoaxView.setVisibility(View.VISIBLE);
                            Log.i(TAG, "populateViewHolder: hoax");
                        } else {
                            viewHolder.starView.setVisibility(View.VISIBLE);
                            viewHolder.hoaxView.setVisibility(View.VISIBLE);
                            viewHolder.starView.setImageResource(R.drawable.ic_action_star);
                            viewHolder.hoaxView.setImageResource(R.drawable.ic_action_hoax_click);
                            Log.i(TAG, "populateViewHolder: not both");
                        }
                        viewHolder.bindToPost(model, new View.OnClickListener() {
                            @Override
                            public void onClick(View starView) {
                                // Need to write to both places the post is stored
                                DatabaseReference globalPostRef = mDatabase.child("posts").child(postRef.getKey());
                                DatabaseReference userPostRef = mDatabase.child("user-posts").child(model.uid).child(postRef.getKey());

                                onStarClicked(globalPostRef, starView);
                                onStarClicked(userPostRef, starView);
                            }
                        }, getContext(), getUid());
                    } else {
                        Log.i(TAG, "populateViewHolder: position " + position + ", adapterPos " + viewHolder.getAdapterPosition());
//                        mRecycler.removeViewAt(position);
                        viewHolder.itemView.setVisibility(View.GONE);
                    }
                }
                // Determine if the current user has liked this post and set UI accordingly


                // Bind Post to ViewHolder, setting OnClickListener for the star button

//                viewHolder.imageView.setVisibility(View.GONE);
            }
        };
        mRecycler.setAdapter(mAdapter);
        Log.i(TAG, "mAdapter.getItemCount():" + mAdapter.getItemCount());

        Log.i(TAG, "Adapter setted");
        mAdapter.notifyDataSetChanged();

//        srl.setRefreshing(false);
    }

    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef, final View v) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (v.getId() == R.id.star) {
                    if (p.stars.containsKey(getUid())) {
                        // Unstar the post and remove self from stars
                        p.starCount -= 1;
                        p.stars.remove(getUid());
                    } else {
                        // Star the post and add self to stars
                        p.starCount += 1;
                        p.stars.put(getUid(), true);

                    }
                } else if (v.getId() == R.id.hoax) {
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
    // [END post_stars_transaction]


    @Override
    public void onStart() {
        super.onStart();
        gl.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

    @Override
    public void onLocation(Location location) {
        if (location!=null) {
            this.locs = location;
            Log.i(TAG, "onLocation: location " + locs.getLatitude() + ", " + locs.getLongitude());
        }
        setmAdapter();
    }
}