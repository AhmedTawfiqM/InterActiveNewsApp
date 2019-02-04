package at.tasknews.news;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import utilities.Jops;
import utilities.Statics;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    //https://newsapi.org/v2/top-headlines?country=eg&category=business&apiKey=ebdcdcf772724f5facfd86d4f2f811ed

    RecyclerView recyclerView;

    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mProgressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recclerview);


        mProgressBar.setVisibility(View.VISIBLE);

        //Retrofit
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .baseUrl("https://newsapi.org/v2/")
                .build();

        //Check Internet Conection
        if (!Jops.isNetworkConnected(MainActivity.this)) {

            ShowSnackBar_ALert();
        } else {


            GetDataByRetrofit();
        }

    }

    private void ShowSnackBar_ALert() {

        String alertMessage = getResources().getString(R.string.alert_internet);

        View parentLayout = findViewById(android.R.id.content);

        Snackbar.make(parentLayout, alertMessage, Snackbar.LENGTH_LONG).show();

        mProgressBar.setVisibility(View.GONE);
    }

    private void GetDataByRetrofit() {


        APIDataNews apiDataNews = retrofit.create(APIDataNews.class);
        apiDataNews.getNews("eg", Statics.KeyNEWSAPI).enqueue(new Callback<JsonObject>() {
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

                ArrayList<String> UserProviders = Jops.GetUserProviders(MainActivity.this);

                //- Check if Provider is Choosen in List of USer Providers
                //if YES Load Article
                if (UserProviders.contains(provider)) {

                    titlesList.add(title);
                    imgsURL.add(image);
                    Urls.add(url);
                    contents.add(description);
                    Providers.add(provider);
                    datePublished_AL.add(datePublished);
                }

            }
            //

            if (titlesList.size() <= 0) {
                Toast.makeText(getBaseContext(), "لا يوجد أخبار من المصادر المختارة حاليا", Toast.LENGTH_SHORT).show();
            }

            //Set Adapter
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(titlesList, imgsURL, Urls, contents, Providers, datePublished_AL,
                    MainActivity.this, Statics.ActivityKEY);


            recyclerView.setAdapter(adapter);

            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            mProgressBar.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OpenSeting(View view) {
        startActivity(new Intent(this, Setting.class));
    }

    private interface APIDataNews {

        @GET("top-headlines")
        retrofit2.Call<JsonObject> getNews(@Query("country") String country
                , @Query("apiKey") String apiKey);

    }//


    @Override
    protected void onStart() {
        super.onStart();

        //Refresh Data
        try {
            GetDataByRetrofit();
            //Toast.makeText(getBaseContext(), "Refreshed", Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    /*
    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpsURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpsURLConnection) url.openConnection();


                if (result != null) {

                    String response = streamToString(urlConnection.getInputStream());


                    parseResult(response);


                    return result;


                }
            } catch (MalformedURLException e) {
                e.printStackTrace();


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // Download complete. Let us update UI
            if (result != null) {

                ArrayList<String> titlesList = new ArrayList<>();
                ArrayList<String> imgsURL = new ArrayList<>();
                ArrayList<String> contents = new ArrayList<>();
                ArrayList<String> Urls = new ArrayList<>();
                ArrayList<String> Providers = new ArrayList<>();

                for (ArticlesItem articlesItem : mListData) {

                    titlesList.add(articlesItem.getTitle());
                    imgsURL.add(articlesItem.getImage());
                    contents.add(articlesItem.getDescription());
                    Urls.add(articlesItem.getUrl());
                    Providers.add(articlesItem.getSource());
                }


                //Set Adapter
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(titlesList, imgsURL, Urls, contents, Providers, titlesList,
                        getApplicationContext(), 0);


                recyclerView.setAdapter(adapter);

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            } else {
                Toast.makeText(MainActivity.this, "Failed to load data!", Toast.LENGTH_SHORT).show();

            }

            mProgressBar.setVisibility(View.GONE);
        }
    }

    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }
*/

}
