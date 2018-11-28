package org.unibl.etf.mr.touristbl.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.unibl.etf.mr.touristbl.MainActivity;
import org.unibl.etf.mr.touristbl.adapter.NewsAdapter;
import org.unibl.etf.mr.touristbl.R;
import org.unibl.etf.mr.touristbl.model.NewsDetails;
import org.unibl.etf.mr.touristbl.util.Utility;
import org.unibl.etf.mr.touristbl.model.News;

import okhttp3.internal.Util;


public class NewsFragment extends Fragment {
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> newsArrayList;
    private ProgressDialog dialog;
    private Integer startItem=1;
    private Integer endItem=10;
    private Integer increment=10;


    public NewsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView =  view.findViewById(R.id.recycler_view);
        newsArrayList=new ArrayList<>();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager=(LinearLayoutManager)recyclerView.getLayoutManager();
                if (manager.getItemCount()-1==manager.findLastCompletelyVisibleItemPosition()){
                    dialog = new ProgressDialog(getActivity());
                    dialog.setMessage(getString(R.string.loading));
                    dialog.show();
                    new NewsLoader().execute(startItem+=increment,endItem+=increment);
                    System.out.println(startItem+":"+endItem);
                }

            }
        });
        adapter = new NewsAdapter(newsArrayList, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        getActivity().findViewById(R.id.search_entry).setVisibility(View.GONE);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.loading));
        dialog.show();
        new NewsLoader().execute(startItem,endItem);
        return view;
    }

    public class NewsLoader extends AsyncTask<Integer, Void, Void> {


        @Override
        protected Void doInBackground(Integer... length) {
            try {
                if (PreferenceManager.getDefaultSharedPreferences(NewsFragment.this.getActivity()).getBoolean("cache_switch",false)&&!Utility.checkForConnection(getContext())){
                    newsArrayList.clear();
                    for (NewsDetails details:MainActivity.getNewsCache()){
                        News news=new News();
                        news.setId(details.getId());
                        news.setImageUrl(details.getImageUrl());
                        news.setTitle(details.getTitle());
                        newsArrayList.add(news);
                        hideDialog();

                    }
                }else{
                    String json=Utility.readJsonFromUrl(getString(R.string.news_url)+length[0]+"/"+length[1]);
                    Gson gson = new Gson();
                    newsArrayList.addAll(Arrays.asList(gson.fromJson(json,News[].class)));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyItemChanged(newsArrayList.size()-1);
            hideDialog();
        }


    }
    public void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ((NavigationView)getActivity().findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_news).setChecked(true);
    }
}
