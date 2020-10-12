package com.example.mvvmsantabanta.activity.home;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuDetails;
import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuInfo;
import com.example.mvvmsantabanta.databinding.ItemToExpandBinding;
import com.example.mvvmsantabanta.fragments.jokes.FragmentJokes;
import com.example.mvvmsantabanta.fragments.jokes.jokesCategories.FragmentJokesCategories;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.SmsSubCategoriesFragment;
import com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel.SmsCategoriesResponseModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ExpandableViewAdapter extends RecyclerView.Adapter<ExpandableViewAdapter.ViewHolder> {

    Context context;
    ArrayList<NavMenuInfo> children;
    String name;


    public ExpandableViewAdapter(String name, Context context, ArrayList<NavMenuInfo> navMenuInfo) {
        this.context = context;
        this.children = navMenuInfo;
        this.name = name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemToExpandBinding view = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_to_expand, parent, false);
//        ItemExpandBinding bi = DataBindingUtil.bind(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.binding.text.setText(Html.fromHtml(children.get(position).getName()));
        Picasso.get()
                .load(children.get(position).getIcon())
//                    .networkPolicy(NetworkPolicy.OFFLINE)
                .fit().error(R.mipmap.ic_launcher)
                .into(holder.binding.ivCat, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("picasso", e.getMessage());
                    }
                });
        holder.binding.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (name.toUpperCase()) {
                    case "SMS":
                        ((Main2Activity) context).showSubCatFragment(new SmsSubCategoriesFragment(), children, position);
                        break;
                    case "JOKES":
                        ((Main2Activity) context).showJokesFragment(new FragmentJokes(), children.get(position), position);
                        break;

                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return children.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemToExpandBinding binding;

        public ViewHolder(@NonNull ItemToExpandBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }
    }


}
