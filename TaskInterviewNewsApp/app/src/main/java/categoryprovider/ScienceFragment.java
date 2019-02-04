package categoryprovider;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import at.tasknews.news.ArticlesItem;
import at.tasknews.news.R;
import at.tasknews.news.RecyclerViewAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utilities.APIDataNews;
import utilities.Jops;
import utilities.Statics;


public class ScienceFragment extends Fragment {

    RecyclerViewAdapter adapter;

    RecyclerView recclerview;
    ProgressBar mProgressBar;
    View parentLayout;

    String providerName;
    Retrofit retrofit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            providerName = getArguments().getString(Statics.providerKEY);
            Log.d("fromFragment_Pr", providerName);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.science_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View layout, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(layout, savedInstanceState);

        recclerview = layout.findViewById(R.id.recclerview);
        mProgressBar = layout.findViewById(R.id.progressBar);
        parentLayout = layout.findViewById(android.R.id.content);

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .baseUrl("https://newsapi.org/v2/")
                .build();

        if (!Jops.isNetworkConnected(getContext())) {

            ShowSnackBar_ALert();
        } else {




            ConnectAPINews();
        }

    }

    private void parseResult(String result) {

        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("articles");

            ArrayList<String> titlesList = new ArrayList<>();
            ArrayList<String> imgsURL = new ArrayList<>();
            ArrayList<String> contents = new ArrayList<>();
            ArrayList<String> Urls = new ArrayList<>();
            ArrayList<String> Providers = new ArrayList<>();
            ArrayList<String> datePublished_AL = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {

                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");
                String image = post.optString("urlToImage");
                String description = post.optString("description");
                String url = post.optString("url");
                String datePublished = post.getString("publishedAt");

                JSONObject jsonSource = post.optJSONObject("source");
                String provider = jsonSource.optString("name");
                Log.d("providers", provider);

                if (provider.equals(providerName)) {

                    titlesList.add(title);
                    imgsURL.add(image);
                    contents.add(description);
                    Urls.add(url);
                    Providers.add(provider);
                    datePublished_AL.add(datePublished);
                }
                Log.d("testData1", title + " : " + description);

            }

            if (titlesList.size() == 0) {
                //Toast.makeText(getContext(), "لا يوجد أخبار حاليا", Toast.LENGTH_SHORT).show();
                Snackbar.make(getActivity().findViewById(android.R.id.content)
                        , "لا يوجد أخبار حاليا", Snackbar.LENGTH_LONG).show();

                Log.d("testData1", "" + titlesList.size());
            } else {
                Log.d("testData1titlesList", titlesList.size() + "");
            }

            //Set Adapter
            adapter = new RecyclerViewAdapter(titlesList, imgsURL, Urls, contents, Providers, datePublished_AL,
                    getContext(), Statics.FragmentKEY);
            adapter.notifyDataSetChanged();


            recclerview.setAdapter(adapter);


            recclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            mProgressBar.setVisibility(View.GONE);

            //mProgressBar.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowSnackBar_ALert() {

        String alertMessage = getResources().getString(R.string.alert_internet);

        Snackbar.make(getActivity().findViewById(android.R.id.content)
                , alertMessage, Snackbar.LENGTH_LONG).show();

        mProgressBar.setVisibility(View.GONE);
    }

    private void ConnectAPINews() {

        APIDataNews apiDataNews = retrofit.create(APIDataNews.class);
        apiDataNews.getNews("eg", Statics.ScienceCAT, Statics.KeyNEWSAPI).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                Log.d("retrofitDATA", response.body().toString());
                parseResult(response.body().toString());

            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                Log.d("retrofitDATA", t.getMessage());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ConnectAPINews();
    }
}
