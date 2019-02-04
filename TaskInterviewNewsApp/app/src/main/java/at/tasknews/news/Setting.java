package at.tasknews.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import providerhome.ProviderHome;
import utilities.Jops;
import utilities.Statics;

public class Setting extends AppCompatActivity {


    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        listview = findViewById(R.id.listview);
        listview.setAdapter(new MyAdapter());
        //
        //....
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //

                final String providerName = getProviderNameInEnglish(position);


                TextView tv_Provider = view.findViewById(R.id.tv_title);
                String providerArab = tv_Provider.getText().toString();


                Intent intent = new Intent(getBaseContext(), ProviderHome.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                        .putExtra(Statics.providerKEY + "E", providerName)
                        .putExtra(Statics.providerKEY, providerArab);
                startActivity(intent);
                //
            }
        });

    }


    private void AddDataProviderSHaredPrefer(Context context, String provider) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Statics.App_Setting, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> set = new HashSet<>();

        //Get All Data Providers
        ArrayList<String> dataProviders = Jops.GetUserProviders(Setting.this);

        if (!dataProviders.contains(provider)) {
//              ///
            if (provider.equals(Statics.AljazeeraCOM_Provider)
                    || provider.equals(Statics.AljazeeraNet_Provider)) {

                dataProviders.add(Statics.AljazeeraCOM_Provider);
                dataProviders.add(Statics.AljazeeraNet_Provider);

            } else {
                dataProviders.add(provider);
            }

            Log.d("storesharedPreferences", "not Existed  " + provider);
        } else {
            Log.d("storesharedPreferences", "Existed  " + provider);
        }

        set.addAll(dataProviders);
        editor.putStringSet(Statics.providerKEY, set);
        editor.apply();
        Log.d("storesharedPreferences", "" + set);
    }

    private void RemovefromDataProviderSHaredPrefer(Context context, String provider) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Statics.App_Setting, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> set = new HashSet<>();

        //Get All Data Providers
        ArrayList<String> dataProviders = Jops.GetUserProviders(Setting.this);

        dataProviders.remove(provider);
        if (provider.equals(Statics.AljazeeraCOM_Provider)
                || provider.equals(Statics.AljazeeraNet_Provider)) {

            dataProviders.remove(Statics.AljazeeraCOM_Provider);
            dataProviders.remove(Statics.AljazeeraNet_Provider);

        } else {
            dataProviders.remove(provider);
        }

        set.addAll(dataProviders);
        editor.putStringSet(Statics.providerKEY, set);
        editor.apply();

        Log.d("allUserProviders", "Removed : " + provider);

        Log.d("storesharedPreferences", "" + set);
    }

    public void imag_Back_Listner(View view) {
        onBackPressed();
    }


    private class MyAdapter extends BaseAdapter {

        String[] providers = getResources().getStringArray(R.array.providers);


        @Override
        public int getCount() {
            return providers.length;
        }

        @Override
        public Object getItem(int i) {
            return providers[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertview, ViewGroup viewGroup) {

            View row = convertview;
            ViewHolder myHolderView = null;
            if (row == null) {

                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = layoutInflater.inflate(R.layout.singlerow_setting, viewGroup, false);

                myHolderView = new ViewHolder(row);
                row.setTag(myHolderView);
            } else {
                myHolderView = (ViewHolder) row.getTag();
            }

            myHolderView.tv_Title.setText(providers[position]);

            //Check User provider UI
            final ArrayList<String> allUserProviders = Jops.GetUserProviders(Setting.this);
            final String providerName = getProviderNameInEnglish(position);
            //..
            if (allUserProviders.contains(providerName)) {

                myHolderView.iv_status.setImageResource(android.R.drawable.checkbox_on_background);
            } else {
                myHolderView.iv_status.setImageResource(android.R.drawable.ic_input_add);
            }
            ///...........

            myHolderView.iv_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!allUserProviders.contains(providerName)) {

                        AddDataProviderSHaredPrefer(Setting.this, providerName);
                        notifyDataSetChanged();

                        Log.d("allUserProviders", "Added : " + providerName);

                    } else {
                        //Remove from Shared prefernces
                        //..Check if there is 2 providers or more
                        int sizeAllowed;
                        if (allUserProviders.contains(Statics.AljazeeraNet_Provider)
                                || allUserProviders.contains(Statics.AljazeeraCOM_Provider)) {

                            sizeAllowed = allUserProviders.size() - 1;
                        } else {
                            sizeAllowed = allUserProviders.size();
                        }
                        //Toast.makeText(getBaseContext(), sizeAllowed + "", Toast.LENGTH_SHORT).show();

                        if (sizeAllowed <= 3) {

                            Toast.makeText(getBaseContext(), "يجب عرض الاخبار من 3 مصادر على الأقل", Toast.LENGTH_SHORT).show();

                        } else {


                            RemovefromDataProviderSHaredPrefer(Setting.this, providerName);
                            notifyDataSetChanged();

                        }
                        Log.d("allUserProviders", "Removed : " + providerName);
                    }

                }
            });


            return row;
        }

        class ViewHolder {
            TextView tv_Title;
            ImageView iv_status;

            public ViewHolder(View row) {
                tv_Title = row.findViewById(R.id.tv_title);
                iv_status = row.findViewById(R.id.iv_Youm7);
            }
        }//
    }

    private String getProviderNameInEnglish(int numberKEY) {

        if (numberKEY == 0) {
            return Statics.Youm7_Provider;

        } else if (numberKEY == 1) {

            return Statics.Skynewsarabia_Provider;
        } else if (numberKEY == 2) {
            return Statics.Albawabhnews_Provider;

        } else if (numberKEY == 3) {
            return Statics.Masrawy_Provider;

        } else if (numberKEY == 4) {
            return Statics.Albayan_ae_Provider;

        } else if (numberKEY == 5) {
            return Statics.Elbalad_newsProvider;

        } else if (numberKEY == 6) {
            return Statics.ElwatannewsProvider;

        } else if (numberKEY == 7) {
            return Statics.YallakoraProvider;

        } else if (numberKEY == 8) {
            return Statics.AljazeeraCOM_Provider;

        } else if (numberKEY == 9) {
            return Statics.AlahlyegyptProvider;

        } else if (numberKEY == 10) {
            return Statics.Alwatanvoice_Provider;

        } else if (numberKEY == 11) {
            return Statics.Ahram_Provider;

        } else if (numberKEY == 12) {
            return Statics.Elnashra_Provider;

        } else if (numberKEY == 13) {
            return Statics.Alarabiya_Provider;

        } else if (numberKEY == 14) {
            return Statics.FilgoalProvider;

        } else if (numberKEY == 15) {
            return Statics.Akhbarak_Provider;

        } else if (numberKEY == 16) {
            return Statics.Samanews__Provider;

        } else if (numberKEY == 17) {
            return Statics.Alyaoum24_Provider;

        } else if (numberKEY == 18) {
            return Statics.Almasryalyoum_Provider;

        } else if (numberKEY == 19) {
            return Statics.Watanserb_Provider;

        } else if (numberKEY == 20) {
            return Statics.RT_Provider;

        } else if (numberKEY == 21) {
            return Statics.Bbc_Provider;

        } else if (numberKEY == 22) {
            return Statics.Tech_wd_Provider;

        } else if (numberKEY == 23) {
            return Statics.Hespress__Provider;

        }

        return "";
    }
}
