package com.example.mvvmsantabanta.fragments.sms.smsCategories;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.activity.home.Main2Activity;
import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuResponse;
import com.example.mvvmsantabanta.databinding.FragmentSmsCategoriesBinding;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel.SmsCategoriesResponseModel;
import com.example.mvvmsantabanta.nestedBackStack.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmsCategoriesFragment extends BaseFragment {

    private FragmentSmsCategoriesBinding fragmentSmsCategoriesBinding;
    private SmsCategoriesAdapter smsCategoriesAdapter;
    private  SmsCategoriesViewModel mViewModel;
    public  ArrayList<SmsCategoriesResponseModel> smsCategoriesArrayList;


    public SmsCategoriesFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new SmsCategoriesFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSmsCategoriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sms_categories, container, false);
        View view = fragmentSmsCategoriesBinding.getRoot();

        mViewModel = ViewModelProviders.of(this).get(SmsCategoriesViewModel.class);
        fragmentSmsCategoriesBinding.setLifecycleOwner(this);

        fragmentSmsCategoriesBinding.progressBar.setVisibility(View.VISIBLE);
        fragmentSmsCategoriesBinding.recyclerSmsCategories.setVisibility(View.GONE);

        smsCategoriesArrayList = new ArrayList<>();
        smsCategoriesAdapter = new SmsCategoriesAdapter(getActivity(), smsCategoriesArrayList,this,fragmentSmsCategoriesBinding.progressBar);
        fragmentSmsCategoriesBinding.setSmsCategoriesAdapter(smsCategoriesAdapter);


        listerners();
        _observeSmsCategories();
        return view;

    }

    private void listerners() {
        fragmentSmsCategoriesBinding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                _observeSmsCategories();

            }
        });
    }

    public void _observeSmsCategories() {

        mViewModel.getSmsCategoriesLiveData().observe(getActivity(), smsCategoriesResponseModel -> {

            if (smsCategoriesResponseModel!= null && smsCategoriesResponseModel.size()!=0) {
                fragmentSmsCategoriesBinding.recyclerSmsCategories.setVisibility(View.VISIBLE);
                fragmentSmsCategoriesBinding.tvNoData.setVisibility(View.GONE);
                smsCategoriesArrayList.clear();
                smsCategoriesArrayList.addAll(smsCategoriesResponseModel);
                smsCategoriesAdapter.notifyDataSetChanged();


            }else {
                fragmentSmsCategoriesBinding.recyclerSmsCategories.setVisibility(View.GONE);
                fragmentSmsCategoriesBinding.tvNoData.setVisibility(View.VISIBLE);
                fragmentSmsCategoriesBinding.progressBar.setVisibility(View.GONE);
            }

            if (fragmentSmsCategoriesBinding.swipeContainer.isRefreshing()) {
                fragmentSmsCategoriesBinding.swipeContainer.setRefreshing(false);
            }

//                ((Main2Activity) Objects.requireNonNull(getActivity())).setSideMenuAdapter(smsCategoriesArrayList);
        });

    }


    public void enterNextFragment(SmsSubCategoriesFragment smsSubCategoriesFragment) {

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            // Store the Fragment in stack
            transaction.addToBackStack(null);
            transaction.replace(R.id.rootlay, smsSubCategoriesFragment).commit();
        }

}
