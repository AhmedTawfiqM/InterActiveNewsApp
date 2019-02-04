package utilities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Jops {

    public static void OpenUrlCustomTab(String Url, Context context) {

        Log.d("URLCutomTAbs", Url);
        String custom_Package = "http://www.google.com";

        final CustomTabsClient[] customTabsClient = new CustomTabsClient[1];
        final CustomTabsSession[] customTabsSession = new CustomTabsSession[1];
        CustomTabsIntent customTabsIntent;

        CustomTabsServiceConnection customTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {

                customTabsClient[0] = client;
                customTabsClient[0].warmup(0);
                customTabsSession[0] = customTabsClient[0].newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

                customTabsClient[0] = null;
            }
        };
        CustomTabsClient.bindCustomTabsService(context, custom_Package, customTabsServiceConnection);

        customTabsIntent = new CustomTabsIntent.Builder(customTabsSession[0])
                .setShowTitle(true)
                .setToolbarColor(Color.DKGRAY)
                .build();

        customTabsIntent.launchUrl(context, Uri.parse(Url));
    }

    public static String getProviderNameInArabic(String providerEnglish) {


        if (providerEnglish.equals(Statics.Youm7_Provider)) {

            return "اليوم السابع";

        } else if (providerEnglish.equals(Statics.Masrawy_Provider)) {

            return "مصراوي";

        } else if (providerEnglish.equals(Statics.Skynewsarabia_Provider)) {

            return "سكاي نيوز عرابيا";

        } else if (providerEnglish.equals(Statics.Tech_wd_Provider)) {

            return "تيك وود";
        } else if (providerEnglish.equals(Statics.Albawabhnews_Provider)) {

            return "البوابة ";

        } else if (providerEnglish.equals(Statics.Albayan_ae_Provider)) {

            return "البوابة ";
        } else if (providerEnglish.equals(Statics.Albayan_ae_Provider)) {

            return "البيان ";
        } else if (providerEnglish.equals(Statics.RT_Provider)) {

            return "أر تي ";
        } else if (providerEnglish.equals(Statics.Elbalad_newsProvider)) {

            return "البلد ";
        } else if (providerEnglish.equals(Statics.AlahlyegyptProvider)) {

            return "الأهلي ";
        } else if (providerEnglish.equals(Statics.AljazeeraCOM_Provider)
                || providerEnglish.equals(Statics.AljazeeraNet_Provider)) {

            return "الجزيرة ";
        } else if (providerEnglish.equals(Statics.Bbc_Provider)) {

            return "بي بي سي ";
        } else if (providerEnglish.equals(Statics.YallakoraProvider)) {

            return "يلا كورة ";
        } else if (providerEnglish.equals(Statics.FilgoalProvider)) {

            return "فايل جول ";
        } else if (providerEnglish.equals(Statics.ElwatannewsProvider)) {

            return "الوطن ";
        } else if (providerEnglish.equals(Statics.Akhbarak_Provider)) {

            return "أخبارك";
        } else if (providerEnglish.equals(Statics.Alwatanvoice_Provider)) {

            return "صوت الوطن";
        } else if (providerEnglish.equals(Statics.Samanews__Provider)) {

            return "سما نيوز";
        } else if (providerEnglish.equals(Statics.Alarabiya_Provider)) {

            return "العربية";
        } else if (providerEnglish.equals(Statics.Hespress__Provider)) {

            return "هسبريس";
        } else if (providerEnglish.equals(Statics.Alyaoum24_Provider)) {

            return "اليوم 24";
        } else if (providerEnglish.equals(Statics.Almasryalyoum_Provider)) {

            return "المصري اليوم";
        } else if (providerEnglish.equals(Statics.Elnashra_Provider)) {

            return "النشرة";
        } else if (providerEnglish.equals(Statics.Ahram_Provider)) {

            return "الأهرام";
        } else if (providerEnglish.equals(Statics.Watanserb_Provider)) {

            return "وطن سيرب";
        } else {
            return "مصدر مجهور";
        }
    }

    public static ArrayList<String> GetUserProviders(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Statics.App_Setting, 0);

        Set<String> set = new HashSet<>();
        ArrayList<String> al_DefaultProviders = new ArrayList<>();

        //3 Defaults Providers
        al_DefaultProviders.add(Statics.Youm7_Provider);
        al_DefaultProviders.add(Statics.Skynewsarabia_Provider);
        al_DefaultProviders.add(Statics.Masrawy_Provider);

        set.addAll(al_DefaultProviders);

        al_DefaultProviders.clear();
        Set<String> setsDefaultsProvider = sharedPreferences.getStringSet(Statics.providerKEY, set);

        al_DefaultProviders.addAll(setsDefaultsProvider);
        Log.d("sharedItemsPROV", setsDefaultsProvider + "");

        return al_DefaultProviders;
    }

    public static void SHareLink(Context context, String url) {

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "NEWS APP");
            String shareMessage = "";
            shareMessage = shareMessage + url;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }

    }

    public static String getProviderNameInEnglish(String providerArabic) {


        if (providerArabic.equals("اليوم السابع")) {

            return Statics.Youm7_Provider;

        } else if (providerArabic.equals("مصراوي")) {

            return Statics.Masrawy_Provider;

        } else if (providerArabic.equals("سكاي نيوز عرابية")) {

            return Statics.Skynewsarabia_Provider;

        } else if (providerArabic.equals("تيك وود")) {

            return Statics.Tech_wd_Provider;
        } else if (providerArabic.equals(Statics.Albawabhnews_Provider)) {

            return "البوابة ";

        } else if (providerArabic.equals(Statics.Albayan_ae_Provider)) {

            return "البوابة ";
        } else if (providerArabic.equals(Statics.RT_Provider)) {

            return "أر تي ";
        } else if (providerArabic.equals(Statics.Elbalad_newsProvider)) {

            return "البلد ";
        } else if (providerArabic.equals(Statics.AlahlyegyptProvider)) {

            return "الأهلي ";
        } else if (providerArabic.equals(Statics.AljazeeraCOM_Provider)
                || providerArabic.equals(Statics.AljazeeraNet_Provider)) {

            return "الجزيرة ";
        } else if (providerArabic.equals(Statics.Bbc_Provider)) {

            return "بي بي سي ";
        } else if (providerArabic.equals(Statics.YallakoraProvider)) {

            return "يلا كورة ";
        } else if (providerArabic.equals(Statics.FilgoalProvider)) {

            return "فايل جول ";
        } else if (providerArabic.equals(Statics.ElwatannewsProvider)) {

            return "الوطن ";
        } else if (providerArabic.equals(Statics.Akhbarak_Provider)) {

            return "أخبارك";
        } else if (providerArabic.equals(Statics.Alwatanvoice_Provider)) {

            return "صوت الوطن";
        } else if (providerArabic.equals(Statics.Samanews__Provider)) {

            return "سما نيوز";
        } else if (providerArabic.equals(Statics.Alarabiya_Provider)) {

            return "العربية";
        } else if (providerArabic.equals(Statics.Hespress__Provider)) {

            return "هسبريس";
        } else if (providerArabic.equals(Statics.Alyaoum24_Provider)) {

            return "اليوم 24";
        } else if (providerArabic.equals(Statics.Almasryalyoum_Provider)) {

            return "المصري اليوم";
        } else if (providerArabic.equals(Statics.Elnashra_Provider)) {

            return "النشرة";
        } else if (providerArabic.equals(Statics.Ahram_Provider)) {

            return "الأهرام";
        } else if (providerArabic.equals(Statics.Watanserb_Provider)) {

            return "وطن سيرب";
        } else {
            return "مصدر مجهور";
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
