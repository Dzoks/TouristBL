package org.unibl.etf.mr.touristbl.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentManager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

import org.unibl.etf.mr.touristbl.R;
import org.unibl.etf.mr.touristbl.fragment.NewsDetailsFragment;
import org.unibl.etf.mr.touristbl.model.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> newsList;
    private Activity activity;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    public NewsAdapter(List<News> newsArrayList,Activity activity) {
        newsList=newsArrayList;
        this.activity=activity;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.news_card, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {

        holder.primaryText.setText(newsList.get(position).getTitle());

        holder.mediaImage.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                FragmentManager fragmentManager = ((FragmentActivity)activity).getSupportFragmentManager();
                NewsDetailsFragment fragment=new NewsDetailsFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("NEWS",newsList.get(position));
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.frame_container,fragment ).addToBackStack(null).commit();
            }
        });
        Glide.with(activity).load(newsList.get(position).getImageUrl()).apply(new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder.mediaImage);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


    class NewsViewHolder extends RecyclerView.ViewHolder{

        TextView primaryText;
        ImageView mediaImage;

        NewsViewHolder(View itemView) {
            super(itemView);
            primaryText = itemView.findViewById(R.id.primary_text);
            mediaImage = itemView.findViewById(R.id.media_image);
        }
    }
}
