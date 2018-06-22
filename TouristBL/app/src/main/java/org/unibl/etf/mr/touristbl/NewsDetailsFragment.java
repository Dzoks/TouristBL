package org.unibl.etf.mr.touristbl;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import org.unibl.etf.mr.touristbl.R;

public class NewsDetailsFragment extends Fragment {


    private ImageView detailsPhoto;
    private TextView detailsTitle;
    private WebView detailsFull;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_details, container, false);
        detailsPhoto=view.findViewById(R.id.details_photo);
        detailsFull=view.findViewById(R.id.details_full);
        detailsTitle=view.findViewById(R.id.details_title);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Učitava se...");
        dialog.show();
        News news=(News)getArguments().getSerializable("NEWS");
        new NewsDetailsLoader().execute(news);
        return view;
    }

    private class NewsDetailsLoader extends AsyncTask<News,Void,NewsDetails> {
        @Override
        protected NewsDetails doInBackground(News... news) {
            try {
                Gson gson=new Gson();
                NewsDetails fullNews=gson.fromJson(Utility.readJsonFromUrl(getString(R.string.news_details_url)+news[0].getId()),NewsDetails.class);
                fullNews.setImageUrl(news[0].getImageUrl().replace("263x158","555x333"));
                return fullNews;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(NewsDetails fullNews) {
            detailsFull.loadData(fullNews.getFullArticle(),"text/html; charset=utf-8", "UTF-8");
            detailsTitle.setText(fullNews.getTitle());
            Picasso.get().load(fullNews.getImageUrl()).into(detailsPhoto);
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
