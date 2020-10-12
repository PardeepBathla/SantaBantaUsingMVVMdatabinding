package com.example.mvvmsantabanta.fragments.sms;

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

import com.example.mvvmsantabanta.roomDb.SmsAndJokesDatabase;
import com.example.mvvmsantabanta.utility.Constants;
import com.example.mvvmsantabanta.utility.Utils;
import com.example.mvvmsantabanta.fragments.favourites.AddFavouriteRequest;
import com.example.mvvmsantabanta.fragments.sms.smsModel.SmsDetailModel;
import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.databinding.FragmentSmsFragmentBinding;
import com.example.mvvmsantabanta.fragments.sms.smsModel.SmsFavouriteModel;
import com.example.mvvmsantabanta.nestedBackStack.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;


public class FragmentSms extends BaseFragment  {

    public static String subcat_slug_name;
    public static int subcat_slug_id;
    private  FragmentSmsFragmentBinding binding;
    SmsAndJokesDatabase smsAndJokesDatabase;
    SmsItemAdapter smsItemAdapter;
    private FragmentSmsViewModel mViewModel;

    private void _noData() {
        binding.recyclerSms.setVisibility(View.GONE);
        binding.tvNoData.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sms_fragment, container, false);
        View view = binding.getRoot();

        binding.recyclerSms.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);


        if (getArguments() != null && getArguments().getString(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG) != null && getArguments().getInt(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG_ID) != -1) {
            subcat_slug_name = getArguments().getString(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG);
            subcat_slug_id = getArguments().getInt(Constants.COMMON.SELECTED_SUBCATEGORY_SLUG_ID);
        }

        mViewModel = ViewModelProviders.of(this).get(FragmentSmsViewModel.class);
        binding.setLifecycleOwner(this);
        smsAndJokesDatabase = SmsAndJokesDatabase.getInstance(getActivity());


        smsItemAdapter = new SmsItemAdapter(getActivity(), this, binding.progressBar);
        binding.setMySmsadapter(smsItemAdapter);

        _listeners();

        _observerSms();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mViewModel.init();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private void _listeners() {
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                _observerSms();

            }
        });
    }

    private void _observerSms() {


        try {
            mViewModel.getSmsLiveData(FragmentSms.this).observe(getActivity(), new Observer<PagedList<SmsDetailModel>>() {
                @Override
                public void onChanged(PagedList<SmsDetailModel> smsResponseModel) {


                    if (smsResponseModel != null || smsResponseModel.size() != 0) {

                        smsItemAdapter.submitList(smsResponseModel); //paging method
                        smsItemAdapter.notifyDataSetChanged();
                        binding.recyclerSms.setVisibility(View.VISIBLE);
                        binding.tvNoData.setVisibility(View.GONE);
                    } else {

                        FragmentSms.this._noData();
                    }


                    if (binding.swipeContainer.isRefreshing()) {
                        binding.swipeContainer.setRefreshing(false);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            _noData();
        }

    }

    public void addSmsToFav(SmsDetailModel obj, int position, ProgressBar progressBar, CheckBox cbLike) {

        AddFavouriteRequest addFavouriteRequest = new AddFavouriteRequest();
        addFavouriteRequest.setDeviceId(Utils.getMyDeviceId(getActivity()));
        addFavouriteRequest.setType("sms");
        addFavouriteRequest.setItemId(obj.getId().intValue());


        mViewModel.postFavSms(addFavouriteRequest).observe(getActivity(), new Observer<ResponseBody>() {
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

    private void setFavItemToModel(int position, AddFavouriteRequest addFavouriteRequest, SmsDetailModel obj, int fav_id) {
        SmsFavouriteModel smsFavouriteModel = new SmsFavouriteModel();
        smsFavouriteModel.setDeviceId(addFavouriteRequest.getDeviceId());
        smsFavouriteModel.setItemId(String.valueOf(addFavouriteRequest.getItemId()));
        smsFavouriteModel.setId(fav_id);

        PagedList<SmsDetailModel> pagedLists = null;
        pagedLists = smsItemAdapter.getCurrentList();
        if (obj.getmFavourite() != null && obj.getmFavourite().size() != 0) {
            obj.getmFavourite().add(0, smsFavouriteModel);
        } else {
            List<SmsFavouriteModel> favouriteModelList = new ArrayList<>();
            favouriteModelList.add(0, smsFavouriteModel);
            obj.setmFavourite(favouriteModelList);
        }
        pagedLists.get(position).setmFavourite(obj.getmFavourite());
        smsItemAdapter.submitList(pagedLists); //paging method
        smsItemAdapter.notifyDataSetChanged();
    }

    public void removeFromFav(SmsDetailModel obj, int position, int id, ProgressBar progressBar, CheckBox cbLike) {
        mViewModel.deleteFavSms(id).observe(getActivity(), new Observer<ResponseBody>() {
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

                                removeFavItemFromModel(position, obj);
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

    private void removeFavItemFromModel(int position, SmsDetailModel obj) {
        PagedList<SmsDetailModel> pagedLists = null;
        pagedLists = smsItemAdapter.getCurrentList();

        pagedLists.get(position).setmFavourite(null);
        obj.setFav_count(obj.getFav_count()-1);
        smsItemAdapter.submitList(pagedLists); //paging method
        smsItemAdapter.notifyDataSetChanged();
    }


    public void ApiError() {
        _noData();
    }
}
