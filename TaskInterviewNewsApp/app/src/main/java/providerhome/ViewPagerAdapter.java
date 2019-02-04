package providerhome;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> vFragmentsList = new ArrayList();
    private final List<String> vFragmentsTitles = new ArrayList();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void AddFragment(Fragment fragment, String title) {

        vFragmentsList.add(fragment);
        vFragmentsTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return vFragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return vFragmentsList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return vFragmentsTitles.get(position);
    }
}
