package com.example.mvvmsantabanta.fragments.memes;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.utility.ExoPlayerRecyclerView;
import com.example.mvvmsantabanta.databinding.FragmentMemesBinding;
import com.example.mvvmsantabanta.fragments.memes.memesModel.MemesDetailModel;
import com.example.mvvmsantabanta.nestedBackStack.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemesFragment extends BaseFragment {
    FragmentMemesBinding binding;
    MemesItemAdapter memesItemAdapter;
    private MemesViewModel mViewModel;
    ExoPlayerRecyclerView exoPlayerRecyclerView;

    public MemesFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new MemesFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_memes, container, false);
        View view = binding.getRoot();

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerMemes.setVisibility(View.GONE);


        mViewModel = ViewModelProviders.of(this).get(MemesViewModel.class);
        binding.setLifecycleOwner(this);

        exoPlayerRecyclerView = (ExoPlayerRecyclerView) binding.recyclerMemes;

        memesItemAdapter = new MemesItemAdapter(getActivity(), this, binding.progressBar);
        binding.setMyMemesAdapter(memesItemAdapter);

        _listeners();
        _observerMemes();
        return view;
    }


    private void _listeners() {
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                _observerMemes();

            }
        });
    }

    private void _observerMemes() {
        mViewModel.getMemesLiveData().observe(getActivity(), new Observer<PagedList<MemesDetailModel>>() {
            @Override
            public void onChanged(PagedList<MemesDetailModel> memesDetailModels) {

                if (memesDetailModels != null) {

                    exoPlayerRecyclerView.setMediaObjects(memesDetailModels);
                    memesItemAdapter.submitList(memesDetailModels); //paging method
                    memesItemAdapter.notifyDataSetChanged();
                    binding.recyclerMemes.setVisibility(View.VISIBLE);
                    if (binding.swipeContainer.isRefreshing()) {
                        binding.swipeContainer.setRefreshing(false);
                    }
                }


            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (exoPlayerRecyclerView != null)
            exoPlayerRecyclerView.startPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayerRecyclerView != null)
           exoPlayerRecyclerView.pausePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayerRecyclerView != null)
            Log.d( "onDestroy: ","destroyed");
            exoPlayerRecyclerView.releasePlayer();

    }
}
