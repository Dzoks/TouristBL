package org.unibl.etf.mr.touristbl.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.unibl.etf.mr.touristbl.MainActivity;
import org.unibl.etf.mr.touristbl.R;
import org.unibl.etf.mr.touristbl.util.Utility;
import org.unibl.etf.mr.touristbl.model.News;
import org.unibl.etf.mr.touristbl.model.NewsDetails;

public class NewsDetailsFragment extends Fragment {


    private ImageView detailsPhoto;
    private TextView detailsTitle;
    private WebView detailsFull;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        detailsPhoto=view.findViewById(R.id.details_photo);
        detailsFull=view.findViewById(R.id.details_full);
        detailsTitle=view.findViewById(R.id.details_title);
        view.findViewById(R.id.button_see_map).setVisibility(View.GONE);
        view.findViewById(R.id.button_favorite).setVisibility(View.GONE);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.loading));
        dialog.show();
        News news=(News)getArguments().getSerializable("NEWS");
        new NewsDetailsLoader().execute(news);
        getActivity().findViewById(R.id.search_entry).setVisibility(View.GONE);

        return view;
    }

    private class NewsDetailsLoader extends AsyncTask<News,Void,NewsDetails> {
        @Override
        protected NewsDetails doInBackground(News... news) {
            try {
                if (Utility.checkForConnection(getContext())){
                    Gson gson=new Gson();
                    NewsDetails fullNews=gson.fromJson(Utility.readJsonFromUrl(getString(R.string.news_details_url)+news[0].getId()),NewsDetails.class);
                    fullNews.setImageUrl(news[0].getImageUrl().replace("263x158","555x333"));
                    return fullNews;
                }else if (PreferenceManager.getDefaultSharedPreferences(NewsDetailsFragment.this.getActivity()).getBoolean("cache_switch",false)){
                    for (NewsDetails details :MainActivity.getNewsCache()){
                        if (details.getId()==news[0].getId()){
                            return details;
                        }
                    }
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(NewsDetails fullNews) {
            Glide.with(getActivity()).load(fullNews.getImageUrl()).apply(new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)).into(detailsPhoto);
            if (PreferenceManager.getDefaultSharedPreferences(NewsDetailsFragment.this.getActivity()).getBoolean("cache_switch",false)&&!MainActivity.getNewsCache().contains(fullNews))
                MainActivity.getNewsCache().add(fullNews);
            detailsFull.loadData(fullNews.getFullArticle(),"text/html","utf-8");
            detailsTitle.setText(fullNews.getTitle());
            hideDialog();
        }
    }


    public void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
