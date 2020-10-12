package com.example.mvvmsantabanta.fragments.sms.smsCategories;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmsantabanta.BR;
import com.example.mvvmsantabanta.activity.home.Main2Activity;
import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuInfo;
import com.example.mvvmsantabanta.databinding.SmsSubCategoryItemBinding;
import com.example.mvvmsantabanta.fragments.sms.FragmentSms;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel.SmsCategoriesResponseModel;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel.SubCategoryResponse;
import com.example.mvvmsantabanta.interfaces.OnClickHandlerInterface;

public class SmsSubCategoriesAdapter extends RecyclerView.Adapter<SmsSubCategoriesAdapter.MyViewHolder> implements OnClickHandlerInterface {

    Context context;
    SmsCategoriesResponseModel smsCategoriesArrayList;
    NavMenuInfo smsNavCategoriesArrayList;
    SmsSubCategoriesFragment smsSubCategoriesFragment;
    private FragmentSms fragmentSms;

    public SmsSubCategoriesAdapter(SmsSubCategoriesFragment smsSubCategoriesFragment, Context context, SmsCategoriesResponseModel smsCategoriesArrayList) {
        this.context = context;
        this.smsCategoriesArrayList = smsCategoriesArrayList;
        this.smsSubCategoriesFragment = smsSubCategoriesFragment;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SmsSubCategoryItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.sms_sub_category_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubCategoryResponse subCategoryResponse = smsCategoriesArrayList.getChildren().get(position);
        ((MyViewHolder) holder).bind(subCategoryResponse);
    }

    @Override
    public int getItemCount() {
        return smsCategoriesArrayList.getChildren().size();
    }

    public void updateSubCategoryAccordingToCategory(SmsCategoriesResponseModel smsCategoriesResponseModel) {
        this.smsCategoriesArrayList = smsCategoriesResponseModel;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view, Object model) {
        if (fragmentSms == null) {
            fragmentSms = new FragmentSms();
        }
//        ((Main2Activity)context).setFragment(fragmentSms, Constants.COMMON.SMS);

        smsSubCategoriesFragment.enterNextFragment(fragmentSms, ((SubCategoryResponse) model).getSlug(), ((SubCategoryResponse) model).getId());


    }

    @Override
    public void onShareClick(View view) {

    }

    private void setLayoutWidth(@NonNull SmsSubCategoryItemBinding smsSubCategoryItemBinding, int width) {
        RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(width, (int) context.getResources().getDimension(R.dimen._32sdp));
        int margin_6 = (int) context.getResources().getDimension(R.dimen._6sdp);
        int margin_3 = (int) context.getResources().getDimension(R.dimen._3sdp);
        rel_btn.setMargins(margin_3, margin_3, margin_3, margin_3);
        smsSubCategoryItemBinding.cardCategory.setLayoutParams(rel_btn);
    }

    private int getDisplayWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Main2Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        width = (width / 2);
        return width;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SmsSubCategoryItemBinding subCategoryItemBinding;

        public MyViewHolder(@NonNull SmsSubCategoryItemBinding subCategoryItemBinding) {
            super(subCategoryItemBinding.getRoot());
            this.subCategoryItemBinding = subCategoryItemBinding;
            subCategoryItemBinding.setClickhandler(SmsSubCategoriesAdapter.this);

            setLayoutWidth(subCategoryItemBinding, getDisplayWidth());
        }

        public void bind(SubCategoryResponse subCategoryResponse) {
            subCategoryItemBinding.setVariable(BR.subCategoryModel, subCategoryResponse); //smsModel name will be same as in xml model object name
            subCategoryItemBinding.executePendingBindings();
        }
    }
//    -(context.getResources().getDimension(R.dimen.start_end_margin))
}
