package com.entrevista.ifood2.presentation.presenter.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.entrevista.ifood2.R;
import com.entrevista.ifood2.network.bean.PaymentMethod;

import java.util.List;

/**
 * Created by JCARAM on 11/10/2017.
 */

public class MethodPaymentAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context mContext;
    private List<PaymentMethod> methodList;

    public MethodPaymentAdapter(Context mContext, List<PaymentMethod> methodList) {
        this.mContext = mContext;
        this.methodList = methodList;
    }

    @Override
    public int getCount() {
        return methodList != null ? methodList.size() : 0;
    }

    @Override
    public Object getItem(int i) {

        return methodList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        if (convertView == null)
            view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_spinner_item, null);
        else
            view = convertView;

        TextView method = view.findViewById(android.R.id.text1);
        method.setPadding(8,8,8,8);
        method.setText(methodList.get(i).getName());
//        method.setVisibility(methodList.get(i).isEnabled() ? View.VISIBLE : View.GONE);

        return view;
    }
}
