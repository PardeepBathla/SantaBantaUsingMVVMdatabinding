package com.example.mvvmsantabanta.fragments.jokes.jokesCategories;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.mvvmsantabanta.utility.Constants;
import com.example.mvvmsantabanta.activity.home.Main2Activity;
import com.example.mvvmsantabanta.databinding.FragmentJokesCategoriesBinding;
import com.example.mvvmsantabanta.fragments.jokes.FragmentJokes;
import com.example.mvvmsantabanta.nestedBackStack.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentJokesCategories extends BaseFragment {
    FragmentJokesCategoriesBinding binding;
    private JokesCategoriesViewModel mViewModel;
    private JokesCategoriesAdapter jokesCategoriesAdapter;
    private ArrayList<JokesCategoriesResponseModel> jokesCategoriesArrayList;

    public FragmentJokesCategories() {
        // Required empty public constructor
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ((Main2Activity)context).activityMain2Binding.customViewPager.setCurrentItem(1);
            int id = intent.getIntExtra("id",0);
            String slug = intent.getStringExtra("slug");

            Bundle bundle = new Bundle();
            bundle.putInt(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG_ID,id);
            bundle.putString(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG,slug);

            FragmentJokes fragmentJokes = new FragmentJokes();
            fragmentJokes.setArguments(bundle);
            enterNextFragment(fragmentJokes);


        }
    };

    public static Fragment newInstance() {
        return new FragmentJokesCategories();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jokes_categories, container, false);
        View view = binding.getRoot();
        getActivity().registerReceiver(receiver,new IntentFilter("showJoke"));

        mViewModel = ViewModelProviders.of(this).get(JokesCategoriesViewModel.class);
        binding.setLifecycleOwner(this);

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerJokesCategories.setVisibility(View.GONE);

        jokesCategoriesArrayList = new ArrayList<>();
        jokesCategoriesAdapter = new JokesCategoriesAdapter(getActivity(), jokesCategoriesArrayList, this, binding.progressBar);
        binding.setJokesCategoriesAdapter(jokesCategoriesAdapter);

        listerners();
        _observeSmsCategories();
        return view;
    }

    private void listerners() {
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                _observeSmsCategories();

            }
        });
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();

    }

    public void _observeSmsCategories() {

        mViewModel.getJokesCategoriesLiveData().observe(getActivity(), new Observer<List<JokesCategoriesResponseModel>>() {
            @Override
            public void onChanged(List<JokesCategoriesResponseModel> jokesCategoriesResponseModels) {

                if (jokesCategoriesResponseModels!= null && jokesCategoriesResponseModels.size()!=0) {
                    binding.recyclerJokesCategories.setVisibility(View.VISIBLE);
                    binding.tvNoData.setVisibility(View.GONE);
                    jokesCategoriesArrayList.clear();
                    jokesCategoriesArrayList.addAll(jokesCategoriesResponseModels);
                    jokesCategoriesAdapter.notifyDataSetChanged();

                }else {
                    binding.recyclerJokesCategories.setVisibility(View.GONE);
                    binding.tvNoData.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                }

                if (binding.swipeContainer.isRefreshing()) {
                    binding.swipeContainer.setRefreshing(false);
                }

            }
        });

    }


    public void enterNextFragment(FragmentJokes fragmentJokes) {

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        // Store the Fragment in stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.rootlay, fragmentJokes).commit();
    }
}
