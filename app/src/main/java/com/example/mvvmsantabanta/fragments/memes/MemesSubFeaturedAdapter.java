package com.example.mvvmsantabanta.fragments.memes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.activity.sideMenu.Main2Activity;
import com.example.mvvmsantabanta.databinding.HomeSubFeatureRowItemBinding;
import com.example.mvvmsantabanta.fragments.home.HomeFragment;
import com.example.mvvmsantabanta.fragments.home.homeModel.FeaturedSubCategory;
import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesFeaturedSubCategory;
import com.example.mvvmsantabanta.fragments.memes.memesModel.MemesFeaturedSubCategory;
import com.example.mvvmsantabanta.fragments.sms.smsModel.SmsFeaturedSubCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MemesSubFeaturedAdapter extends RecyclerView.Adapter<MemesSubFeaturedAdapter.ViewHolder> {
    private HomeSubFeatureRowItemBinding binding;
    private Context context;
    private Activity mActivity;
    private ArrayList<MemesFeaturedSubCategory> mList;

    public MemesSubFeaturedAdapter(Activity mActivity, Context context, ArrayList<MemesFeaturedSubCategory> mList) {
        this.mActivity = mActivity;
        this.context = context;
        this.mList = mList;
        //test
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_sub_feature_row_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mList, position);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        HomeSubFeatureRowItemBinding binding;

        public ViewHolder(@NonNull HomeSubFeatureRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(ArrayList<MemesFeaturedSubCategory> mList, int position) {
/*
            binding.setVariable(BR.featuredListModel, mList); //jokesDetailModel name will be same as in xml model object name
            binding.executePendingBindings();
*/
            if (mList.get(position).getIcon() != null && !mList.get(position).getIcon().isEmpty()) {
                Picasso.get().load(mList.get(position).getIcon()).placeholder(R.drawable.ic_santa_banta_logo).into(binding.ivSubCategory);

            } else {
                binding.ivSubCategory.setImageResource(R.drawable.ic_santa_banta_logo);
            }
            if (mList.get(position).getName() != null && !mList.get(position).getName().isEmpty()) {
                binding.tvSubCatName.setText(mList.get(position).getName());
            }

            binding.rlFeaturedItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* if (mList.get(position).getChildren()!=null){
                        addHomeFragment(mList.get(position).getChildren());

                    }else {
                        Toast.makeText(mActivity,"No sub featured category found!!",Toast.LENGTH_LONG).show();
                    }*/
                }
            });


        }
    }

    private void addHomeFragment(ArrayList<JokesFeaturedSubCategory> featuredSubCategory) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("featureSubList", featuredSubCategory);
        homeFragment.setArguments(bundle);
        FragmentTransaction ft = ((Main2Activity) mActivity).getSupportFragmentManager().beginTransaction();
        ft.add(R.id.customViewPager, homeFragment, homeFragment.toString());
        ft.addToBackStack(null);
        ft.commit();
    }
}
