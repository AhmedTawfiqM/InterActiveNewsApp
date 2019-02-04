package at.tasknews.news;

import android.content.Context;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.security.Provider;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import providerhome.ProviderHome;
import utilities.Jops;
import utilities.Statics;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecclerViewAdapter";

    private ArrayList<String> titlesList = new ArrayList<>();
    private ArrayList<String> imgsURLs = new ArrayList<>();
    private ArrayList<String> contents = new ArrayList<>();
    private ArrayList<String> Urls = new ArrayList<>();
    private ArrayList<String> providers = new ArrayList<>();
    private ArrayList<String> datePublished = new ArrayList<>();
    private Context context;

    //KEY for differing between Fragment And Main Screen
    // 0 for Main Screen (Activity)
    //1 for Fragments
    private int KeyScreen;

    public RecyclerViewAdapter(ArrayList<String> titlesList, ArrayList<String> imgsURL, ArrayList<String> Urls,
                               ArrayList<String> contents, ArrayList<String> providers, ArrayList<String> datePublished,
                               Context context, int KeyScreen) {

        this.context = context;
        this.titlesList = titlesList;
        this.imgsURLs = imgsURL;
        this.contents = contents;
        this.Urls = Urls;
        this.providers = providers;
        this.datePublished = datePublished;
        this.KeyScreen = KeyScreen;

        Log.d(TAG, "Constructer is Called");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_news, parent, false);
        ViewHolder holder = new ViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder");

        try {

            holder.progressBarImage.setVisibility(View.VISIBLE);

            //Get Provider Name...
            final String provider = providers.get(position);


            Picasso.get()
                    .load(imgsURLs.get(position))
                    .fit()
                    .into(holder.imv_News, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("ImageLoading", "Success");

                            if (holder.progressBarImage != null) {
                                holder.progressBarImage.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("ImageLoading", "Error " + e.getMessage());
                        }
                    });
            holder.tv_Title.setText(titlesList.get(position));

            holder.layout_Parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, "Url:  " + imgsURL.get(position), Toast.LENGTH_SHORT).show();


                    //Open Article Details
                    Intent intent = new Intent(context, ArticleDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra("title", titlesList.get(position));
                    intent.putExtra("content", contents.get(position));
                    intent.putExtra("imgurl", imgsURLs.get(position));
                    intent.putExtra("provider", providers.get(position));
                    intent.putExtra("url", Urls.get(position));
                    intent.putExtra("date", datePublished.get(position));
                    context.startActivity(intent);
                }
            });


            final String ARbicProv = Jops.getProviderNameInArabic(provider);

            //Check if Fragment or Adapter
            if (KeyScreen == Statics.ActivityKEY) {

                holder.topProvider.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(context, "topProvider  ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, ProviderHome.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                                .putExtra(Statics.providerKEY + "E", provider)
                                .putExtra(Statics.providerKEY, ARbicProv);
                        context.startActivity(intent);

                    }
                });

                holder.topProvider.setVisibility(View.VISIBLE);

            } else {

                holder.topProvider.setVisibility(View.GONE);
            }

            Log.d("ProbvidersConvertor", provider + " : " + ARbicProv);
            holder.tv_title_provider.setText(ARbicProv);

            holder.iv_shareArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Jops.SHareLink(context, Urls.get(position));
                }
            });

            holder.iv_OpenUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Jops.OpenUrlCustomTab(Urls.get(position), context);
                }
            });

            //.....


        } catch (Exception ex) {
            Log.d("errorLoadingData", ex.getMessage());
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return titlesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imv_News, iv_shareArticle, iv_OpenUrl;
        TextView tv_Title;
        LinearLayout layout_Parent;
        ProgressBar progressBarImage;
        RelativeLayout topProvider;
        TextView tv_title_provider;

        public ViewHolder(View row) {
            super(row);

            imv_News = row.findViewById(R.id.imv_news);
            tv_Title = row.findViewById(R.id.tv_title);
            layout_Parent = row.findViewById(R.id.parent_row);
            topProvider = row.findViewById(R.id.topProvider);
            progressBarImage = row.findViewById(R.id.progressBarImage);
            tv_title_provider = row.findViewById(R.id.tv_title_provider);
            iv_shareArticle = row.findViewById(R.id.iv_shareArticle);
            iv_OpenUrl = row.findViewById(R.id.iv_OpenUrl);

        }
    }


}
