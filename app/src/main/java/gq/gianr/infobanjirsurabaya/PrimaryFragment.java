package gq.gianr.infobanjirsurabaya;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by j on 02/03/2017.
 */
public class PrimaryFragment extends PostListFragment{
    public static final String TAG = PrimaryFragment.class.getSimpleName();

    public PrimaryFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query recentPostsQuery = databaseReference.child("posts")
                .limitToFirst(100);

        return recentPostsQuery;
    }
}
