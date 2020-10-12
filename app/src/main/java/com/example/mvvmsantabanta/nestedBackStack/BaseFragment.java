package com.example.mvvmsantabanta.nestedBackStack;

import androidx.fragment.app.Fragment;

/**
 * Created by shahabuddin on 6/6/14.
 */
public class BaseFragment extends Fragment implements OnBackPressListener {

    @Override
    public boolean onBackPressed() {
        return new BackPressImpl(this).onBackPressed();
    }
}
