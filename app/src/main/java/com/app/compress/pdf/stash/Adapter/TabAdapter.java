package com.app.compress.pdf.stash.Adapter;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.app.compress.pdf.stash.Fragments.BaseContentFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Sajal.Jain1 on 19-12-2017.
 */

public class TabAdapter extends FragmentPagerAdapter {
    FragmentManager manager;

    private final LinkedHashMap<String, BaseContentFragment> mFragmentList = new LinkedHashMap<>();

    public TabAdapter(FragmentManager manager) {
        super(manager);
        this.manager = manager;
    }

    @Override
    public BaseContentFragment getItem(int position) {

        ArrayList<BaseContentFragment> keys = new ArrayList<>(mFragmentList.values());
        return keys.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(BaseContentFragment fragment, String title) {
        mFragmentList.put(title, fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ArrayList<String> keys = new ArrayList<>(mFragmentList.keySet());
        return keys.get(position);
    }

    public int getSize() {
        return mFragmentList.size();
    }

}
