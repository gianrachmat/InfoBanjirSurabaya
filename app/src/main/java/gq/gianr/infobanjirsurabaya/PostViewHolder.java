package gq.gianr.infobanjirsurabaya;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

/**
 * Created by j on 24/04/2017.
 */

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public TextView numStarsView;
    public TextView bodyView;
    public ImageView imageView;
    public ImageView starView;
    public ImageView hoaxView;
    public ImageView veriView;
    public CardView cardView;
    View itemView;

    public PostViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;

        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.star);
        hoaxView = (ImageView) itemView.findViewById(R.id.hoax);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
        imageView = (ImageView) itemView.findViewById(R.id.post_picture);
        veriView = (ImageView) itemView.findViewById(R.id.verified);
        cardView = (CardView) itemView.findViewById(R.id.cardView);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener, Context ctx, String uid) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        bodyView.setText(post.body);
//        if (post.stars.containsKey(uid)) {
//            starView.setImageResource(R.drawable.ic_action_start_full);
//            hoaxView.setEnabled(false);
//        } else {
//            starView.setImageResource(R.drawable.ic_action_star);
//        }
//
//        if (post.hoaxs.containsKey(uid)) {
//            hoaxView.setImageResource(R.drawable.ic_action_hoax);
//            starView.setEnabled(false);
//        } else {
//            hoaxView.setImageResource(R.drawable.ic_action_hoax_click);
//        }
        if (post.verified!=null)
        if (!post.verified) {
            veriView.setBackgroundResource(R.drawable.ic_action_unveri);
        } else {
            veriView.setBackgroundResource(R.drawable.ic_action_verified);
        }
        System.out.println("imageView: size=" + imageView.getWidth() + "," + imageView.getHeight());
        if (post.url_picture == null || post.url_picture.isEmpty()) {
            imageView.setVisibility(View.GONE);
        } else {
            Glide.with(ctx)
                    .load(post.url_picture)
                    .centerCrop()
                    .into(imageView);
            int vis = imageView.getVisibility();
            imageView.setVisibility(View.GONE);
            imageView.setVisibility(vis);
        }
        starView.setOnClickListener(starClickListener);
        hoaxView.setOnClickListener(starClickListener);
    }
}
