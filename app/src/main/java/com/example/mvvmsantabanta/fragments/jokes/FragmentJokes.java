package com.example.mvvmsantabanta.fragments.jokes;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.roomDb.SmsAndJokesDatabase;
import com.example.mvvmsantabanta.utility.Constants;
import com.example.mvvmsantabanta.utility.Utils;
import com.example.mvvmsantabanta.databinding.FragmentJokesFragmentBinding;
import com.example.mvvmsantabanta.fragments.favourites.AddFavouriteRequest;
import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesDetailModel;
import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesFavouriteModel;
import com.example.mvvmsantabanta.nestedBackStack.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class FragmentJokes extends BaseFragment {

    SmsAndJokesDatabase smsAndJokesDatabase;
    private FragmentJokesFragmentBinding binding;
    private JokesItemAdapter jokesItemAdapter;
    private FragmentJokesViewModel mViewModel;
    public static String cat_slug_name;
    public static int cat_slug_id;

    public static FragmentJokes newInstance() {
        return new FragmentJokes();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jokes_fragment, container, false);
        View view = binding.getRoot();

        if (getArguments() != null && getArguments().getString(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG) != null && getArguments().getInt(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG_ID) != -1) {
            cat_slug_name = getArguments().getString(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG);
            cat_slug_id = getArguments().getInt(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG_ID);
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerJokes.setVisibility(View.GONE);


        mViewModel = ViewModelProviders.of(this).get(FragmentJokesViewModel.class);
        binding.setLifecycleOwner(this);
        smsAndJokesDatabase = SmsAndJokesDatabase.getInstance(getActivity());


        jokesItemAdapter = new JokesItemAdapter(getActivity(), this, binding.progressBar);
        binding.setMyJokesadapter(jokesItemAdapter);

        _listeners();
        _observerJokes();


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private void _listeners() {
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                _observerJokes();

            }
        });
    }


    private void _observerJokes() {
            mViewModel.getJokesLiveData().observe(getActivity(), new Observer<PagedList<JokesDetailModel>>() {
            @Override
            public void onChanged(PagedList<JokesDetailModel> jokesDetailModel) {

                if (jokesDetailModel != null) {
                    jokesItemAdapter.submitList(jokesDetailModel); //paging method
                    jokesItemAdapter.notifyDataSetChanged();
                    binding.recyclerJokes.setVisibility(View.VISIBLE);
                    if (binding.swipeContainer.isRefreshing()) {
                        binding.swipeContainer.setRefreshing(false);
                    }
                }


            }
        });

    }

    public void addJokeToFav(JokesDetailModel obj, int position, CheckBox cbLike, ProgressBar progressBar) {

        AddFavouriteRequest addFavouriteRequest = new AddFavouriteRequest();
        addFavouriteRequest.setDeviceId(Utils.getMyDeviceId(getActivity()));
        addFavouriteRequest.setType("jokes");
        addFavouriteRequest.setItemId(obj.getId().intValue());

        mViewModel.postFavJoke(addFavouriteRequest).observe(getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                if (responseBody != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        if (jsonObject.has("status")) {
                            String status = jsonObject.getString("status");

                            if (status.equals("success")) {
                                progressBar.setVisibility(View.GONE);
                                cbLike.setClickable(true);
                                obj.setFav_count(obj.getFav_count()+1);
                                setFavItemToModel(position, addFavouriteRequest, obj, jsonObject.getInt("fav_id"));
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


        });

    }

    private void setFavItemToModel(int position, AddFavouriteRequest addFavouriteRequest, JokesDetailModel obj, int fav_id) {
        JokesFavouriteModel JokesFavouriteModel = new JokesFavouriteModel();
        JokesFavouriteModel.setDeviceId(addFavouriteRequest.getDeviceId());
        JokesFavouriteModel.setItemId(String.valueOf(addFavouriteRequest.getItemId()));
        JokesFavouriteModel.setId(fav_id);

        PagedList<JokesDetailModel> pagedLists = null;
        pagedLists = jokesItemAdapter.getCurrentList();
        if (obj.getmFavourite() != null && obj.getmFavourite().size() != 0) {
            obj.getmFavourite().add(0, JokesFavouriteModel);
        } else {
            List<JokesFavouriteModel> favouriteModelList = new ArrayList<>();
            favouriteModelList.add(0, JokesFavouriteModel);
            obj.setmFavourite(favouriteModelList);
        }
        pagedLists.get(position).setmFavourite(obj.getmFavourite());
        jokesItemAdapter.submitList(pagedLists); //paging method
        jokesItemAdapter.notifyDataSetChanged();
    }

    public void removeFromFav(JokesDetailModel obj, int id, int position, CheckBox cbLike, ProgressBar progressBar) {
        mViewModel.deleteFavJoke(id).observe(getActivity(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody responseBody) {
                if (responseBody != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        if (jsonObject.has("status")) {
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                cbLike.setClickable(true);
                                progressBar.setVisibility(View.GONE);
                                removeFavItemFromModel(position,obj);
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


        });
    }

    private void removeFavItemFromModel(int position, JokesDetailModel obj) {
        PagedList<JokesDetailModel> pagedLists = null;
        pagedLists = jokesItemAdapter.getCurrentList();

        pagedLists.get(position).setmFavourite(null);
        obj.setFav_count(obj.getFav_count()-1);
        jokesItemAdapter.submitList(pagedLists); //paging method
        jokesItemAdapter.notifyDataSetChanged();
    }



}
