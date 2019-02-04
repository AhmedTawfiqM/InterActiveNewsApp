package at.tasknews.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import utilities.Jops;

public class ArticleDetails extends AppCompatActivity {

    SeekBar seekBar_FontSize;
    TextView tv_provider, tv_title, tv_date, tv_description;
    ImageView iv_urlimg, iv_share, iv_OpenUrl;
    ProgressBar progressBar;

    String UrlArticle;

    int MAX = 60;
    final int MIN = 20;
    final int PLUS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_details_activity);

        InitiliazeUI();

        //Get Article Details
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String imgUrl = getIntent().getStringExtra("imgurl");
        String provider = getIntent().getStringExtra("provider");
        String url = getIntent().getStringExtra("url");
        String date = getIntent().getStringExtra("date");

        UrlArticle = url;
        //
        tv_provider.setText(Jops.getProviderNameInArabic(provider));
        tv_title.setText(title);
        tv_date.setText(date);
        tv_description.setText(content);


        Log.d("ImageLoadingA", imgUrl);

        //Load Image
        Picasso.get()
                .load(imgUrl)
                .fit()
                .into(iv_urlimg, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("ImageLoadingA", "Success in Article details");

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("ImageLoadingA", "Error " + e.getMessage());
                    }
                });
    }


    private void InitiliazeUI() {


        seekBar_FontSize = findViewById(R.id.seekBar_FontSize);
        tv_provider = findViewById(R.id.tv_provider);
        tv_title = findViewById(R.id.tv_title);
        tv_date = findViewById(R.id.tv_date);
        iv_urlimg = findViewById(R.id.iv_urlimg);
        tv_description = findViewById(R.id.tv_description);
        iv_share = findViewById(R.id.iv_share);
        iv_OpenUrl = findViewById(R.id.iv_OpenUrl);
        progressBar = findViewById(R.id.progressBar);

        seekBar_FontSize.setMax((MAX - MIN) / PLUS);

        seekBar_FontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                tv_description.setTextSize(MIN + (progress * PLUS));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void OpenUrl(View view) {

        Jops.OpenUrlCustomTab(UrlArticle, ArticleDetails.this);
    }

    public void imag_Back_Listner(View view) {
        onBackPressed();
    }


    public void ShareArticle(View view) {

        Jops.SHareLink(ArticleDetails.this, UrlArticle);
    }
}
