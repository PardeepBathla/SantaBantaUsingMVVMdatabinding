package com.example.mvvmsantabanta.activity.home;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmsantabanta.R;
import com.example.mvvmsantabanta.activity.home.homeModels.NavMenuDetails;
import com.example.mvvmsantabanta.animation.Animations;

import com.example.mvvmsantabanta.databinding.SideMenuHeaderBinding;
import com.example.mvvmsantabanta.databinding.SideMenuNavItemBinding;
import com.example.mvvmsantabanta.databinding.ThemeChangeViewBinding;

import java.util.ArrayList;

public class SideMenuExpandableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_TYPE = 0;
    private static final int CONTENT_TYPE = 1;
    private static final int THEME_LAYOUT = 2;
    Context context;
    ArrayList<NavMenuDetails> list;
    ExpandableViewAdapter adapter;
    int lastPos = -1;
    boolean isFirst = true;

    public SideMenuExpandableAdapter(Context context, ArrayList<NavMenuDetails> list) {

        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        switch (i) {
            case CONTENT_TYPE:
                SideMenuNavItemBinding sideMenuNavItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.side_menu_nav_item, parent, false);
                return new ContentViewHolder(sideMenuNavItemBinding);
            case HEADER_TYPE:
                SideMenuHeaderBinding sideMenuHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.side_menu_header, parent, false);
                return new HeaderViewHolder(sideMenuHeaderBinding);

            case THEME_LAYOUT:
                ThemeChangeViewBinding themeChangeViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.theme_change_view, parent, false);
                return new ThemeViewHolder(themeChangeViewBinding);

        }

        return null;


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
//
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).binding.closeDrawer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Main2Activity) context).closeDrawer();
                }
            });
        } else if (holder instanceof ThemeViewHolder) {


            ((ThemeViewHolder)holder).bind();
        }
        else if (holder instanceof ContentViewHolder) {

            NavMenuDetails navMenuDetails = list.get(i - 2);
            ((ContentViewHolder) holder).bind(navMenuDetails, i - 2);
        }
    }

    private boolean toggleLayout(boolean isExpanded, View v, LinearLayout layoutExpand) {
        Animations.toggleArrow(v, isExpanded);
        if (isExpanded) {
            Animations.expand(layoutExpand);

        } else {
            Animations.collapse(layoutExpand);
        }
        return isExpanded;

    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER_TYPE;
        if (position == 1)
            return THEME_LAYOUT;
        else
            return CONTENT_TYPE;
    }

    @Override
    public int getItemCount() {
        return list.size() + 2;
    }

    public void updataAdapter(ArrayList<NavMenuDetails> navList) {
        this.list = navList;
        notifyDataSetChanged();
    }


    public class ContentViewHolder extends RecyclerView.ViewHolder {
        SideMenuNavItemBinding binding;

        public ContentViewHolder(@NonNull SideMenuNavItemBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }

        public void bind(NavMenuDetails navMenuDetails, int pos) {


            binding.name.setText(Html.fromHtml(navMenuDetails.getName()));


            binding.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Main2Activity) context).ChangeTab(pos);

                }
            });
            if (navMenuDetails.isExpanded()) {
                _expand(navMenuDetails);
            } else {
                _collapse(navMenuDetails);
            }


            binding.viewMoreBtn.setVisibility(View.VISIBLE);

            adapter = new ExpandableViewAdapter(list.get(pos).getName(), context, list.get(pos).getNavMenuInfo());
            binding.setMyAdapter(adapter);


            binding.viewMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    for (int i = 0; i < list.size(); i++) {
                        if (i == pos) {
                            if (binding.viewMoreBtn.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.ic_icon_feather_plus).getConstantState())) {
                                list.get(i).setExpanded(true);
                            } else if (binding.viewMoreBtn.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.ic_minus).getConstantState())) {
                                list.get(i).setExpanded(false);
                            }
                        } else {
                            list.get(i).setExpanded(false);
                        }
                    }
                    notifyDataSetChanged();

                        /*  boolean is_Expanded = navMenuDetails.isExpanded();
                          if (is_Expanded){
                              Animations.collapse(binding.layoutExpand);
                              navMenuDetails.setExpanded(false);
                              binding.viewMoreBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_icon_feather_plus));
                          }else {
                              Animations.expand(binding.layoutExpand);
                              navMenuDetails.setExpanded(true);
                              binding.viewMoreBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_minus));
                          }
*/


                }
            });

        }

        private void _collapse(NavMenuDetails navMenuDetails) {
            Animations.collapse(binding.layoutExpand);
            navMenuDetails.setExpanded(false);
            binding.viewMoreBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_icon_feather_plus));
        }

        private void _expand(NavMenuDetails navMenuDetails) {
            Animations.expand(binding.layoutExpand);
            navMenuDetails.setExpanded(true);
            binding.viewMoreBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_minus));
        }
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        SideMenuHeaderBinding binding;

        public HeaderViewHolder(@NonNull SideMenuHeaderBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public class ThemeViewHolder extends RecyclerView.ViewHolder {
        ThemeChangeViewBinding binding;
        public ThemeViewHolder(ThemeChangeViewBinding themeChangeViewBinding) {
            super(themeChangeViewBinding.getRoot());
            binding = themeChangeViewBinding;
        }

        public void bind() {

            if (((Main2Activity)context).iSelectedThemeLight()){
               binding.cbChangeLang.setChecked(true);
                binding.name.setText(context.getResources().getString(R.string.light));
            }else {
               binding.cbChangeLang.setChecked(false);
                binding.name.setText(context.getResources().getString(R.string.dark));
            }
          binding.cbChangeLang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                      binding.name.setText(context.getResources().getString(R.string.light));
                    else
                       binding.name.setText(context.getResources().getString(R.string.dark));
                    ((Main2Activity)context).switchTheme();
                }
            });
        }
    }
}
