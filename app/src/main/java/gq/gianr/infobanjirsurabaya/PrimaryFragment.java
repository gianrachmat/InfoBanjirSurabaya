package gq.gianr.infobanjirsurabaya;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import gq.gianr.infobanjirsurabaya.model.Posts;
import gq.gianr.infobanjirsurabaya.presenter.IFeedPresenter;
import gq.gianr.infobanjirsurabaya.presenter.impl.FeedPresenter;
import gq.gianr.infobanjirsurabaya.ui.adapter.PostsAdapter;
import gq.gianr.infobanjirsurabaya.ui.view.IFeedView;

/**
 * Created by j on 02/03/2017.
 */
public class PrimaryFragment extends Fragment implements IFeedView, PostsAdapter.OnPermalinkClickListener{
    public static final String TAG = PrimaryFragment.class.getSimpleName();

    private IFeedPresenter presenter;

    private RecyclerView rvPosts;
    private PostsAdapter postsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new FeedPresenter();
        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.primary_layout, container, false);
        rootView.setTag(TAG);

        rvPosts = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        initPostsAdapter();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getPosts();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void initPostsAdapter() {
        postsAdapter = new PostsAdapter(getContext(), this);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(lm);
        rvPosts.setAdapter(postsAdapter);
    }

    @Override
    public void showPosts(Posts posts) {
        postsAdapter.updatePosts(posts);
    }

    @Override
    public void onPhotoClick(String permalinkUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(permalinkUrl));
        startActivity(intent);
    }
}
