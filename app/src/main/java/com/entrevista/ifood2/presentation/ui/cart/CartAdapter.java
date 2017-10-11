package com.entrevista.ifood2.presentation.ui.cart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.entrevista.ifood2.R;
import com.entrevista.ifood2.repository.model.Product;
import com.entrevista.ifood2.repository.model.Restaurant;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by JCARAM on 09/10/2017.
 */

class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context mContext;
    private List<Product> products;
    @Getter
    @Setter
    private OnProductItemClick onProductItemClick;

    public CartAdapter(Context context) {
        mContext = context;
    }

    public void updateList(@NonNull List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void updateItemList(Restaurant item, int position) {
        notifyItemChanged(position, item);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product item = products.get(position);

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        holder.amount.setText(format.format(item.getAmount()));
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.name.setText(item.getName());

    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton add, remove;
        TextView amount, quantity, name;

        public ViewHolder(View itemView) {
            super(itemView);
            add = itemView.findViewById(R.id.add);
            remove = itemView.findViewById(R.id.remove);
            amount = itemView.findViewById(R.id.amount);
            quantity = itemView.findViewById(R.id.quantity);
            name = itemView.findViewById(R.id.name);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onProductItemClick != null)
                        onProductItemClick.updateItem(products, products.get(getLayoutPosition()),
                                getLayoutPosition(), products.get(getLayoutPosition()).getQuantity() + 1);
                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onProductItemClick != null)
                        onProductItemClick.updateItem(products, products.get(getLayoutPosition()),
                                getLayoutPosition(), products.get(getLayoutPosition()).getQuantity() - 1);
                }
            });

        }
    }


    public interface OnProductItemClick {
        void updateItem(List<Product> products, Product item, int position, int increment);
    }
}
