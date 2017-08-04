package com.cerezaconsulting.pushay.presentation.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cerezaconsulting.pushay.R;
import com.cerezaconsulting.pushay.core.LoaderAdapter;
import com.cerezaconsulting.pushay.data.entities.DestinyTravelEntity;
import com.cerezaconsulting.pushay.presentation.adapters.listener.OnClickListListener;
import com.cerezaconsulting.pushay.presentation.presenters.commons.DestinyItem;
import com.cerezaconsulting.pushay.utils.CircleTransform;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by katherine on 15/05/17.
 */

public class DestinyAdapter extends LoaderAdapter<DestinyTravelEntity> implements OnClickListListener {

    private Context context;
    private DestinyItem destinyItem;

    public DestinyAdapter(ArrayList<DestinyTravelEntity> destinyTravelEntities, Context context, DestinyItem destinyItem) {
        super(context);
        setItems(destinyTravelEntities);
        this.context = context;
        this.destinyItem = destinyItem;
    }

    public DestinyAdapter(ArrayList<DestinyTravelEntity> destinyTravelEntities, Context context) {
        super(context);
        setItems(destinyTravelEntities);
        this.context = context;

    }

    public ArrayList<DestinyTravelEntity> getItems() {
        return (ArrayList<DestinyTravelEntity>) getmItems();
    }

    @Override
    public long getYourItemId(int position) {
        return getmItems().get(position).getId();
    }

    @Override
    public RecyclerView.ViewHolder getYourItemViewHolder(ViewGroup parent) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_places, parent, false);
        return new ViewHolder(root, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void bindYourViewHolder(RecyclerView.ViewHolder holder, int position) {
        DestinyTravelEntity destinyTravelEntity = getItems().get(position);
        ((ViewHolder) holder).tvNamePlace.setText(destinyTravelEntity.getName());
        if (destinyTravelEntity.getImage_1()!=null){
            Glide.with(context)
                    .load(destinyTravelEntity.getImage_1())
                    .transform(new CircleTransform(context))
                    .into(((ViewHolder) holder).ivPlaces);
        }else{
            ((ViewHolder) holder).ivPlaces.setImageDrawable(context.getDrawable(R.drawable.ic_no_pais));
        }
    }

    @Override
    public void onClick(int position) {

        DestinyTravelEntity destinyTravelEntity = getItems().get(position);
        destinyItem.clickItem(destinyTravelEntity);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_places)
        ImageView ivPlaces;
        @BindView(R.id.tv_name_place)
        TextView tvNamePlace;

        private OnClickListListener onClickListListener;

        ViewHolder(View itemView, OnClickListListener onClickListListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onClickListListener = onClickListListener;
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListListener.onClick(getAdapterPosition());
        }
    }
}
