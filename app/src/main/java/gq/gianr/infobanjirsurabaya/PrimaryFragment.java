package gq.gianr.infobanjirsurabaya;

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
    //    private IFeedPresenter presenter;
//
//    private RecyclerView rvPosts;
//    private PostsAdapter postsAdapter;
//
//    private SwipeRefreshLayout srl;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        presenter = new FeedPresenter();
//        presenter.setView(this);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.primary_layout, container, false);
//        rootView.setTag(TAG);
//
//        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_primary);
//        rvPosts = (RecyclerView) rootView.findViewById(R.id.recyclerView);
//        initPostsAdapter();
//        return rootView;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        presenter.getPosts();
//    }
//
//    @Override
//    public void showMessage(String message) {
//        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void initPostsAdapter() {
//        postsAdapter = new PostsAdapter(getContext(), this);
//        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
//        rvPosts.setLayoutManager(lm);
//        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                presenter.getPosts();
//                srl.setRefreshing(false);
//            }
//        });
//        rvPosts.setAdapter(postsAdapter);
//    }
//
//    @Override
//    public void showPosts(Posts posts) {
//        postsAdapter.updatePosts(posts);
//    }
//
//    @Override
//    public void onPhotoClick(String permalinkUrl) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse(permalinkUrl));
//        startActivity(intent);
//    }
}
