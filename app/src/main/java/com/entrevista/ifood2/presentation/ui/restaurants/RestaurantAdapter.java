package com.entrevista.ifood2.presentation.ui.restaurants;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.entrevista.ifood2.R;
import com.entrevista.ifood2.network.bean.Restaurant;
import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by JCARAM on 07/10/2017.
 */

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<Restaurant> restaurants;
    private Context context;
    @Getter @Setter OnRestaurantClickListener onItemClickListener;

    public RestaurantAdapter(Context context) {
        this.context = context;
    }

    public void loadRestaurants(List<Restaurant> restaurants){
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Restaurant item = restaurants.get(position);

        if (StringUtils.isNotBlank(item.getName()))
            holder.name.setText(item.getName());

        if (StringUtils.isNotBlank(item.getImageUrl()))
            holder.restaurantImage.setImageURI(item.getImageUrl());

        if (StringUtils.isNotBlank(item.getDescription()))
            holder.description.setText(item.getDescription());

        if (StringUtils.isNotBlank(item.getAddress()))
            holder.address.setText(item.getAddress());

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
        if (item.getDeliveryFee() != 0)
            holder.deliveryFee.setText(format.format(item.getDeliveryFee()));
        else
            holder.deliveryFee.setText(context.getString(R.string.delivery_free));

        holder.ratingBar.setRating((float) item.getRating());
        LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context,R.color.star_yellow), PorterDuff.Mode.SRC_ATOP);

    }

    @Override
    public int getItemCount() {
        return restaurants != null ? restaurants.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView restaurantImage;
        TextView name, description, address, deliveryFee;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            restaurantImage = (SimpleDraweeView) itemView.findViewById(R.id.restaurant_image);
            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
            address = (TextView) itemView.findViewById(R.id.address);
            deliveryFee = (TextView) itemView.findViewById(R.id.delivery_fee);
            ratingBar = itemView.findViewById(R.id.rating_bar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null)
                        onItemClickListener.onClickItem(restaurants.get(getLayoutPosition()));
                }
            });
        }
    }

    public interface OnRestaurantClickListener{
        void onClickItem(Restaurant item);
    }
}
