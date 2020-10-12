package com.example.mvvmsantabanta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.mvvmsantabanta.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

       binding.setContext(this);
        binding.setManager(getSupportFragmentManager());

    }

    @BindingAdapter({"handler"})
    public static void bindViewPagerAdapter(final ViewPager view, final MainActivity activity)
    {
        final MainSectionsAdapter adapter = new MainSectionsAdapter(view.getContext(), activity.getSupportFragmentManager());
        view.setAdapter(adapter);

    }

    @BindingAdapter({"pagerAdapter"})
    public static void bindViewPagerTabs(final TabLayout view, final ViewPager pagerView)
    {
        view.setupWithViewPager(pagerView, true);
    }


}
