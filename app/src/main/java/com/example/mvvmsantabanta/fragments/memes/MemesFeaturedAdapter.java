package com.example.mvvmsantabanta.fragments.memes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.activity.sideMenu.Main2Activity;
import com.example.mvvmsantabanta.databinding.HomeFeaturedRowItemBinding;
import com.example.mvvmsantabanta.eventbus.Events;
import com.example.mvvmsantabanta.fragments.home.homeModel.FeaturedSubCategory;
import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesFeaturedCategory;
import com.example.mvvmsantabanta.fragments.jokes.jokesModel.JokesFeaturedSubCategory;
import com.example.mvvmsantabanta.fragments.memes.memesModel.MemesFeaturedCategory;
import com.example.mvvmsantabanta.fragments.memes.memesModel.MemesFeaturedSubCategory;
import com.example.mvvmsantabanta.fragments.sms.FragmentSms;
import com.example.mvvmsantabanta.fragments.sms.smsModel.SmsFeaturedCategory;
import com.example.mvvmsantabanta.fragments.sms.smsModel.SmsFeaturedSubCategory;
import com.example.mvvmsantabanta.utility.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.mvvmsantabanta.utility.Constants.SHOW_JOKES_SELECED_DATA;
import static com.example.mvvmsantabanta.utility.Constants.SHOW_MEMES_SELECED_DATA;

public class MemesFeaturedAdapter extends RecyclerView.Adapter<MemesFeaturedAdapter.ViewHolder> {
    private HomeFeaturedRowItemBinding binding;
    private Context context;
    private Activity mActivity;
    private MemesFragment memesFragment;
    private ArrayList<MemesFeaturedCategory> mList;

    public MemesFeaturedAdapter(Activity mActivity, Context context, MemesFragment memesFragment, ArrayList<MemesFeaturedCategory> mList) {
        this.mActivity = mActivity;
        this.context = context;
        this.mList = mList;
        this.memesFragment = memesFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_featured_row_item, parent, false);
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
        HomeFeaturedRowItemBinding binding;

        public ViewHolder(@NonNull HomeFeaturedRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(ArrayList<MemesFeaturedCategory> mList, int position) {
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
                    if (mList.get(position).getChildren() != null) {
                        Intent intent = new Intent();
                        intent.setAction(SHOW_MEMES_SELECED_DATA);
                        intent.putExtra("id", mList.get(position).getId());
                        intent.putExtra("slug",  mList.get(position).getSlug());
                        context.sendBroadcast(intent);
                    } else {
                        Toast.makeText(mActivity, "No sub featured category found!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

    private void addSmsFragment(ArrayList<MemesFeaturedCategory> featuredCategories, ArrayList<MemesFeaturedSubCategory> featuredSubCategory, int pos) {
        Constants.SHOW_SMS_SUB_FEATURE_LIST = Constants.COMMON.SHOW_SUB_FEAURES;
        FragmentSms fragmentSms = new FragmentSms();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("featureSubList", featuredSubCategory);
        bundle.putInt("id",featuredCategories.get(pos).getId());
        bundle.putString("slug",featuredCategories.get(pos).getSlug());
        fragmentSms.setArguments(bundle);
        FragmentTransaction ft = ((Main2Activity) mActivity).getSupportFragmentManager().beginTransaction();
        ft.add(R.id.smsRootLayout, fragmentSms, fragmentSms.toString());
        ft.addToBackStack(null);
        ft.commit();
    }
}