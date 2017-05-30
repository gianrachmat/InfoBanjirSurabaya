package gq.gianr.infobanjirsurabaya;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by j on 24/04/2017.
 */

class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public ImageView imageView;

    public PostViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
        imageView = (ImageView) itemView.findViewById(R.id.post_picture);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener, Context ctx) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        bodyView.setText(post.body);
        if (post.url_picture == null || post.url_picture.isEmpty()){
            imageView.setVisibility(View.GONE);
        } else {
            Picasso.with(ctx)
                    .load(post.url_picture)
                    .resizeDimen(R.dimen.image_width_height, R.dimen.image_width_height)
                    .centerCrop()
                    .into(imageView);
            int vis = imageView.getVisibility();
            imageView.setVisibility(View.GONE);
            imageView.setVisibility(vis);
        }
        starView.setOnClickListener(starClickListener);
    }
}
