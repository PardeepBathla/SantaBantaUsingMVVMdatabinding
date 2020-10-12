package com.example.mvvmsantabanta;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mvvmsantabanta.fragments.jokes.jokesCategories.FragmentJokesCategories;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.SmsCategoriesFragment;
import com.example.mvvmsantabanta.fragments.memes.MemesFragment;

public class MainSectionsAdapter extends FragmentPagerAdapter {
    private static final int SMS = 0;
    private static final int JOKES = 1;
    private static final int MEMES = 2;

    private static final int[] TABS = new int[]{SMS, JOKES,MEMES};
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private Context mContext;

    public MainSectionsAdapter(final Context context, final FragmentManager fm) {
        super(fm);
        mContext = context.getApplicationContext();
    }

    @Override
    public Fragment getItem(int position) {
        switch (TABS[position]) {
            case SMS:
                return SmsCategoriesFragment.newInstance();
            case JOKES:
                return FragmentJokesCategories.newInstance();
                case MEMES:
                return MemesFragment.newInstance();

        }
        return null;
    }

    @Override
    public int getCount() {
        return TABS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (TABS[position]) {
            case SMS:
                return mContext.getResources().getString(R.string.SmsTab);
            case JOKES:
                return mContext.getResources().getString(R.string.JokesTab);
            case MEMES:
                return mContext.getResources().getString(R.string.MemesTab);

        }
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    /**
     * Remove the saved reference from our Map on the Fragment destroy
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }


    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}