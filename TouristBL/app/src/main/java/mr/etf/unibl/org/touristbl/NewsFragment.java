package mr.etf.unibl.org.touristbl;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NewsFragment extends Fragment {
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> newsArrayList;
    private ProgressDialog dialog;


    public NewsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Učitava se...");
        dialog.show();
        new NewsLoader().execute();
        return view;
    }

    public class NewsLoader extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String json=Utility.readJsonFromUrl(getString(R.string.news_url));
                Gson gson = new Gson();
                newsArrayList=  Arrays.asList(gson.fromJson(json,News[].class));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new NewsAdapter(newsArrayList, getActivity());

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    NewsDetailsFragment fragment=new NewsDetailsFragment();
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("NEWS",newsArrayList.get(position));
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container,fragment ).addToBackStack(null).commit();
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }
            }));
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
