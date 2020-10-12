package com.example.mvvmsantabanta.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.activity.home.Main2Activity;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.SmsCategoriesAdapter;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.SmsCategoriesViewModel;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel.SmsCategoriesResponseModel;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    SmsCategoriesViewModel mViewModel;
    SmsCategoriesAdapter smsCategoriesAdapter;
    private ArrayList<SmsCategoriesResponseModel> smsCategoriesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mViewModel = ViewModelProviders.of(this).get(SmsCategoriesViewModel.class);

//        smsCategoriesArrayList = new ArrayList<>();
//        _observeSmsCategories();
        goToMainScreen();
    }

    private void goToMainScreen() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, Main2Activity.class);
//                i.putExtra(Constants.COMMON.CATEGORIES_SUBCATEGORIES, smsCategoriesArrayList);
                startActivity(i);

                // close this activity
                finish();

            }

        }, 3 * 1000); // wait for 5 seconds
    }

    public void _observeSmsCategories() {

        mViewModel.getSmsCategoriesLiveData().observe(SplashActivity.this, new Observer<List<SmsCategoriesResponseModel>>() {
            @Override
            public void onChanged(List<SmsCategoriesResponseModel> smsCategoriesResponseModel) {

                if (smsCategoriesResponseModel != null) {
                    smsCategoriesArrayList.addAll(smsCategoriesResponseModel);
                    goToMainScreen();
                }

            }
        });

    }
}
