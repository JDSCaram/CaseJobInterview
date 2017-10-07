package com.entrevista.ifood2.presentation.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.entrevista.ifood2.R;
import com.entrevista.ifood2.presentation.ui.MainActivity;
import com.entrevista.ifood2.presentation.ui.menu.MenuFragment;

/**
 * Created by JCARAM on 05/10/2017.
 */

public class ProductDetailFrament extends Fragment {

    private TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        mTextView = (TextView) view.findViewById(R.id.text_view);
        afterViews();
        return view;
    }

    private void afterViews() {
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).attachFragment(new MenuFragment(),
                        MenuFragment.class.getSimpleName());
            }
        });
    }
}
