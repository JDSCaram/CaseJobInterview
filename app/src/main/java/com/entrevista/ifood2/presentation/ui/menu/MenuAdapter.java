package com.entrevista.ifood2.presentation.ui.menu;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entrevista.ifood2.R;
import com.entrevista.ifood2.network.bean.Menu;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


/**
 * Created by JCARAM on 08/10/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Context context;
    private List<Menu> menus;
    private OnMenuClickListener onMenuClickListener;

    public MenuAdapter(Context context) {
        this.context = context;
    }

    public void updateMenus(List<Menu> response){
        this.menus = response;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Menu item = menus.get(position);
        if (!TextUtils.isEmpty(item.getName()))
            holder.name.setText(item.getName());

        if (!TextUtils.isEmpty(item.getImageUrl())){
            Uri uri = Uri.parse(item.getImageUrl());
            DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(uri).build();
            holder.image.setController(controller);
        }


        if (!TextUtils.isEmpty(item.getDescription()))
            holder.description.setText(item.getDescription());

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
        if (item.getPrice() != 0)
            holder.price.setText(format.format(item.getPrice()));
        else
            holder.price.setText(context.getString(R.string.delivery_free));
    }

    @Override
    public int getItemCount() {
        return menus != null ? menus.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView price, name, description;
        private SimpleDraweeView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
            price = (TextView) itemView.findViewById(R.id.price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onMenuClickListener != null)
                        onMenuClickListener.onClickItem(menus.get(getLayoutPosition()));
                }
            });

        }
    }

    public interface OnMenuClickListener{
        void onClickItem(Menu item);
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }
}
