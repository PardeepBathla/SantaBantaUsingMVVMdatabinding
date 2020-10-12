package com.example.mvvmsantabanta.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.mvvmsantabanta.MainSectionsAdapter;
import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.utility.CheckPermissions;
import com.example.mvvmsantabanta.utility.Constants;
import com.example.mvvmsantabanta.NonSwipeableViewPager;
import com.example.mvvmsantabanta.utility.LocaleHelper;
import com.example.mvvmsantabanta.utility.Utils;
import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuDetails;
import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuInfo;
import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuResponse;
import com.example.mvvmsantabanta.databinding.ActivityMain2Binding;

import com.example.mvvmsantabanta.fragments.jokes.FragmentJokes;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.SmsSubCategoriesFragment;
import com.example.mvvmsantabanta.nestedBackStack.OnBackPressListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity  {
    static MainSectionsAdapter viewPagerAdapter;
    public SharedPreferences pref;
    HomeViewModel homeViewModel;
    ArrayList<NavMenuResponse> navList;
    MenuItem prevMenuItem;
    public ActivityMain2Binding activityMain2Binding;
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            activityMain2Binding.tvSelectedModule.setText(item.getTitle().toString());
            switch (item.getItemId()) {
                case R.id.nav_bottom_menu_sms:

                    activityMain2Binding.customViewPager.setCurrentItem(0);
                    return true; //don't use break here

                case R.id.nav_bottom_menu_jokes:

                    activityMain2Binding.customViewPager.setCurrentItem(1);
                    return true;

                    case R.id.nav_bottom_menu_memes:

                    activityMain2Binding.customViewPager.setCurrentItem(2);
                    return true;

            }
            return false;
        }
    };

    private SideMenuExpandableAdapter sideMenuExpandableAdapter;

    private HomeViewModel mViewModel;
    private String languageToLoad = "en";

    @BindingAdapter({"pagerhandler"})
    public static void bindViewPagerAdapter(final ViewPager view, final Main2Activity activity) {

        viewPagerAdapter = new MainSectionsAdapter(view.getContext(), activity.getSupportFragmentManager());
        view.setAdapter(viewPagerAdapter);

    }

    @BindingAdapter({"pagerrhandlerr"})
    public static void bindViewPagerAdapter(final NonSwipeableViewPager view, final Main2Activity activity) {

        try {
            viewPagerAdapter = new MainSectionsAdapter(view.getContext(), activity.getSupportFragmentManager());
            view.setAdapter(viewPagerAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show status bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        pref = Utils.getSharedPref(Main2Activity.this);
        setThemePreference();
        LocaleHelper.onAttach(Main2Activity.this);
        String locale = LocaleHelper.getPersistedData(Main2Activity.this, Locale.getDefault().getLanguage());
        if (locale.equalsIgnoreCase("en"))
            Constants.LANGUAGE_SELECTED = Constants.COMMON.ENGLISH;
        else
            Constants.LANGUAGE_SELECTED = Constants.COMMON.HINDI;

        activityMain2Binding = DataBindingUtil.setContentView(this, R.layout.activity_main2);

        init();


    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        recreate();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void init() {

        if (Utils.isNetworkAvailable(Main2Activity.this)) {


            mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
            activityMain2Binding.setLifecycleOwner(this);

            activityMain2Binding.setMycontext(this);


            goToMainScreen();


            if (pref.getBoolean(Constants.COMMON.THEME_MODE_LIGHT, false)) {
                activityMain2Binding.drawer.setScrimColor(getResources().getColor(R.color.white_60));// drawer shadow color
            }

            Log.d("locale", LocaleHelper.getPersistedData(Main2Activity.this, "en"));

            setViewPagerProperties();

            setSideMenuAdapter(new ArrayList<>());

            _listeners();
            _observers();
        } else {
            showNoInternetDialog();
        }
    }

    private void showNoInternetDialog() {

        Dialog dialog = Utils.returnDialog(Main2Activity.this, R.layout.default_dialog_yes_no);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvContenttvContent = dialog.findViewById(R.id.tvContenttvContent);
        TextView tvOk = dialog.findViewById(R.id.tvOk);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);

        tvOk.setText("Try Again");
        tvCancel.setText("Network Settings");

        tvTitle.setText(getString(R.string.alert));
//        tvContenttvContent.setText(getResources().getString(R.string.internet_error));
        tvContenttvContent.setText("Please check your internet connection and try again");

        tvOk.setOnClickListener(v -> {
            dialog.dismiss();
            init();
        });
        tvCancel.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
            startActivity(intent);
        });
        dialog.show();

    }

    private void setViewPagerProperties() {
        activityMain2Binding.customViewPager.setOffscreenPageLimit(3);
        activityMain2Binding.customViewPager.setCurrentItem(0);
        activityMain2Binding.tvSelectedModule.setText(getString(R.string.SmsTab));
        activityMain2Binding.customViewPager.disableScroll(true);
    }

    private void setThemePreference() {
        if (pref.getBoolean(Constants.COMMON.THEME_MODE_LIGHT, false)) {
            setTheme(R.style.AppThemeLight);
        } else {
            setTheme(R.style.AppThemeDark);
        }

    }

    public void setSideMenuAdapter(ArrayList<NavMenuDetails> navMenuDetails) {

        sideMenuExpandableAdapter = new SideMenuExpandableAdapter(Main2Activity.this, navMenuDetails);
        activityMain2Binding.setMyExpandableAdapter(sideMenuExpandableAdapter);
    }


    private void _observers() {
        mViewModel.getSideMenuLiveData().observe(this, new Observer<ArrayList<NavMenuDetails>>() {
            @Override
            public void onChanged(ArrayList<NavMenuDetails> navMenuResponse) {
                if (navMenuResponse != null) {
                    activityMain2Binding.leftDrawerRecycler.setVisibility(View.VISIBLE);


                    for (int i = 0; i < navMenuResponse.size(); i++) {
                        NavMenuDetails navMenuDetails = navMenuResponse.get(i);
                        navMenuDetails.setExpanded(false);
                        navMenuResponse.set(i, navMenuDetails);
                        if (i == navMenuResponse.size() - 1) {
                            sideMenuExpandableAdapter.updataAdapter(navMenuResponse);
                        }
                    }


                } else {
                    activityMain2Binding.leftDrawerRecycler.setVisibility(View.GONE);
                }
            }
        });

    }

    private void _listeners() {

        activityMain2Binding.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        activityMain2Binding.customViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                activityMain2Binding.customViewPager.setCurrentItem(position);

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    activityMain2Binding.bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                activityMain2Binding.bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = activityMain2Binding.bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        activityMain2Binding.ivHamburger.setOnClickListener(v -> {
            if (activityMain2Binding.drawer.isDrawerOpen(GravityCompat.START)) {
                activityMain2Binding.drawer.closeDrawer(GravityCompat.START);
            } else {
                activityMain2Binding.drawer.openDrawer(GravityCompat.START);
            }
        });


        activityMain2Binding.ivLanguage.setOnClickListener(v -> {


            if (Constants.LANGUAGE_SELECTED.equalsIgnoreCase(Constants.COMMON.HINDI)) {
                languageToLoad = "en";
            } else {

                languageToLoad = "hi";
            }

            LocaleHelper.setLocale(Main2Activity.this, languageToLoad);
            finish();
            startActivity(getIntent());

        });

    }

    public void switchTheme() {
        if (pref.getBoolean(Constants.COMMON.THEME_MODE_LIGHT, false)) {
            onCheckedChanged(false);

        } else {
            onCheckedChanged(true);
        }
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        if (!onBack()) {
            if (activityMain2Binding.customViewPager.getCurrentItem() == 1) {
                activityMain2Binding.customViewPager.setCurrentItem(0);
            } else {
                super.onBackPressed();
            }
        }

    }

    public void onCheckedChanged(boolean checked) {

        SharedPreferences.Editor editor = pref.edit();

        if (checked) {
            editor.putBoolean(Constants.COMMON.THEME_MODE_LIGHT, checked); // Storing boolean - true/false
        } else {
            editor.putBoolean(Constants.COMMON.THEME_MODE_LIGHT, checked); // Storing boolean - true/false
        }
        editor.apply();

    }


    public void closeDrawer() {
        activityMain2Binding.drawer.closeDrawer(GravityCompat.START);
    }


    public boolean onBack() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = (OnBackPressListener) viewPagerAdapter.getRegisteredFragment(activityMain2Binding.customViewPager.getCurrentItem());

        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            return currentFragment.onBackPressed();
        }

        // this Fragment couldn't handle the onBackPressed call
        return false;
    }

    public void showSubCatFragment(SmsSubCategoriesFragment fragment, ArrayList<NavMenuInfo> children, int position) {
        Constants.REDIRECTING_FROM = Constants.NAVIGATION_DRAWER_SCREEN;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.COMMON.CATEGORIES_SUBCATEGORIES, children);
        bundle.putInt(Constants.COMMON.SELECTED_CATEGORY_POSITION, position);

        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Store the Fragment in stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.rootlay, fragment).commit();
        if (activityMain2Binding.customViewPager.getCurrentItem() != 0) {
            activityMain2Binding.customViewPager.setCurrentItem(0);
        }
        activityMain2Binding.drawer.closeDrawer(GravityCompat.START);
    }

    public void showJokesFragment(FragmentJokes fragment, NavMenuInfo children, int position) {
//        Constants.REDIRECTING_FROM = Constants.NAVIGATION_DRAWER_SCREEN;
//
//        Bundle bundle = new Bundle();
//        bundle.putInt(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG_ID,children.getId());
//        bundle.putString(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG,children.getSlug());
//
//
//        fragment.setArguments(bundle);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        // Store the Fragment in stack
//        transaction.addToBackStack(null);
//        transaction.add(R.id.rootlay, fragment).commit();

        Intent intent = new Intent();
        intent.setAction("showJoke");
        intent.putExtra("id", children.getId());
        intent.putExtra("slug", children.getSlug());
        sendBroadcast(intent);

        if (activityMain2Binding.customViewPager.getCurrentItem() != 1) {
            activityMain2Binding.customViewPager.setCurrentItem(1);
        }

        activityMain2Binding.drawer.closeDrawer(GravityCompat.START);
    }

    private void goToMainScreen() {

        activityMain2Binding.rlSplash.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {


            @Override

            public void run() {


                slideToBottom(activityMain2Binding.rlSplash);
                CheckPermissions.isStoragePermissionGranted(Main2Activity.this);


            }

        }, 3 * 1000); // wait for 5 seconds
    }

    public void slideToBottom(View view) {

        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight());
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);


    }


    public void ChangeTab(int i) {

        if (i == 2) {
            activityMain2Binding.customViewPager.setCurrentItem(0);
        } else if (i == 3) {
            activityMain2Binding.customViewPager.setCurrentItem(1);
        }
        activityMain2Binding.drawer.closeDrawer(GravityCompat.START);
    }

    public void vibrate() {

        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CheckPermissions.REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // permission was granted :)

            }
        }
    }

    public boolean iSelectedThemeLight() {
        return pref.getBoolean(Constants.COMMON.THEME_MODE_LIGHT, false);
    }
}
