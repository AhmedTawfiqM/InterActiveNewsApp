package providerhome;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import at.tasknews.news.R;
import categoryprovider.BusinessFragment;
import categoryprovider.HealthFragment;
import categoryprovider.ScienceFragment;
import categoryprovider.SportsFragment;
import utilities.Statics;

public class ProviderHome extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    TextView tv_address;

    String providerNameArabic,providerNameEngliosh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_home_activity);

        tv_address = findViewById(R.id.tv_address);

        providerNameArabic = getIntent().getStringExtra(Statics.providerKEY);
        providerNameEngliosh = getIntent().getStringExtra(Statics.providerKEY+"E");
        tv_address.setText(providerNameArabic);

        InitializeUI();

        //Establish View Pager Adapter
        setupViewPager();

        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setScrollX(tabLayout.getWidth());
        //tabLayout.getTabAt(lastTabIndex).select();

        viewPager.setCurrentItem(3);
    }

    private void setupViewPager() {

        //Send Provider Name to Fragments
        Bundle bundle = new Bundle();
        bundle.putString(Statics.providerKEY, providerNameEngliosh);

        SportsFragment sportsFragment = new SportsFragment();
        ScienceFragment scienceFragment = new ScienceFragment();
        HealthFragment healthFragment = new HealthFragment();
        BusinessFragment businessFragment = new BusinessFragment();

        sportsFragment.setArguments(bundle);
        scienceFragment.setArguments(bundle);
        healthFragment.setArguments(bundle);
        businessFragment.setArguments(bundle);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(sportsFragment, "الرياضة");
        adapter.AddFragment(scienceFragment, "العلوم");
        adapter.AddFragment(healthFragment, "الصحة");
        adapter.AddFragment(businessFragment, "التجارة");

        viewPager.setAdapter(adapter);

    }

    private void InitializeUI() {

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
    }

    public void imag_Back_Listner(View view) {
        onBackPressed();
    }


}
