package com.example.mvvmsantabanta.fragments.sms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmsantabanta.R;

import com.example.mvvmsantabanta.fragments.sms.smsModel.SmsFeaturedCategory;
import com.example.mvvmsantabanta.fragments.sms.smsModel.SmsFeaturedSubCategory;
import com.example.mvvmsantabanta.utility.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class SmsFeaturedAdapter extends RecyclerView.Adapter<SmsFeaturedAdapter.ViewHolder> {
    private HomeFeaturedRowItemBinding binding;
    private Context context;
    private Activity mActivity;
    private FragmentSms fragmentSms;
    private ArrayList<SmsFeaturedCategory> mList;

    public SmsFeaturedAdapter(Activity mActivity, Context context, FragmentSms fragmentSms, ArrayList<SmsFeaturedCategory> mList) {
        this.mActivity = mActivity;
        this.context = context;
        this.mList = mList;
        this.fragmentSms = fragmentSms;
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
        if (mList == null)
            return 0;
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        HomeFeaturedRowItemBinding binding;

        public ViewHolder(@NonNull HomeFeaturedRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(ArrayList<SmsFeaturedCategory> mList, int position) {
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

                    if (mList.get(position).getCatType()!=null && mList.get(position).getCatType().equalsIgnoreCase("subcat")){
                        Intent intent = new Intent();
                        intent.setAction(SHOW_SMS_SELECED_DATA);
                        intent.putExtra("id", mList.get(position).getId());
                        intent.putExtra("slug",  mList.get(position).getSlug());
                        intent.putExtra("Veg", "");
                        context.sendBroadcast(intent);
                }else {
                        if (mList.get(position).getChildren() != null) {
                            addSmsFragment(mList,mList.get(position).getChildren(), position);

                        } else {
                            Toast.makeText(mActivity, "No sub featured category found!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    }
            });


        }
    }

    private void addSmsFragment(ArrayList<SmsFeaturedCategory> featuredCategories, ArrayList<SmsFeaturedSubCategory> featuredSubCategory, int pos) {
        Constants.SHOW_SMS_SUB_FEATURE_LIST = Constants.COMMON.SHOW_SUB_FEAURES;
        FragmentSms.IS_FROM_SUB_MENU = true;
        FragmentSms.subcat_slug_name = featuredCategories.get(pos).getSlug();
        FragmentSms.subcat_slug_id = featuredCategories.get(pos).getId();
//        FragmentSms fragmentSms = new FragmentSms();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("featureSubList", featuredSubCategory);
        bundle.putInt("id",featuredCategories.get(pos).getId());
        bundle.putString("slug",featuredCategories.get(pos).getSlug());
        bundle.putString("Veg","Veg");
        bundle.putString("isFromSubMenu","1");
//        fragmentSms.setArguments(bundle);

        FragmentSms fragmentSms = FragmentSms.newInstance(bundle);

        FragmentTransaction ft = ((Main2Activity) mActivity).getSupportFragmentManager().beginTransaction();
        ft.add(R.id.smsRootLayout, FragmentSms.newInstance(bundle), fragmentSms.toString());
        ft.addToBackStack(null);
        ft.commit();



        /*Intent intent = new Intent();
        intent.putExtra("category",featuredCategories.get(pos).getSlug());
        intent.putExtra("isFromSubMenu","1");
        intent.putExtra("id",featuredCategories.get(pos).getId()+"");
        intent.putExtra("slug",featuredCategories.get(pos).getSlug());
        intent.setAction("RefreshSMS");
        context.sendBroadcast(intent);*/

    }
}