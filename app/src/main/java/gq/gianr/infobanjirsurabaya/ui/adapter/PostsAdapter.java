package gq.gianr.infobanjirsurabaya.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import gq.gianr.infobanjirsurabaya.R;
import gq.gianr.infobanjirsurabaya.model.Post;
import gq.gianr.infobanjirsurabaya.model.Posts;

/**
 * Created by j on 17/03/2017.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private Context context;
    private OnPermalinkClickListener permalinkClickListener;
    private List<Post> posts = new ArrayList<>();

    public PostsAdapter(Context context, OnPermalinkClickListener permalinkClickListener) {
        this.context = context;
        this.permalinkClickListener = permalinkClickListener;
    }

    public void updatePosts(Posts posts) {
        this.posts = posts.getPosts();
        this.notifyDataSetChanged();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null){
            context = parent.getContext();
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.tvCreated.setText(post.getCreated());
        if (post.getMessage() == null)
            holder.tvMessage.setVisibility(View.GONE);
        else {
            holder.tvMessage.setText(post.getMessage());
            holder.tvMessage.setVisibility(View.VISIBLE);
        }
        holder.loadPhoto(post.getPictureUrl());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public interface OnPermalinkClickListener {
        void onPhotoClick(String permalinkUrl);
    }

    class PostViewHolder extends RecyclerView.ViewHolder{
        TextView tvCreated;
        TextView tvMessage;
        ImageView ivPicture;

        public PostViewHolder(View itemView) {
            super(itemView);

            tvCreated = (TextView) itemView.findViewById(R.id.tv_created);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
            ivPicture = (ImageView)itemView.findViewById(R.id.iv_picture);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String permalinkUrl = posts.get(getAdapterPosition()).getPermalinkUrl();
                    permalinkClickListener.onPhotoClick(permalinkUrl);
                }
            });
        }

        public void loadPhoto(String imageUrl) {
            if (imageUrl != null)
                Glide.with(context)
                        .load(imageUrl)
                        .centerCrop()
                        .crossFade()
                        .into(ivPicture);
            else
                ivPicture.setVisibility(View.GONE);
        }
    }
}
