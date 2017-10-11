package com.entrevista.ifood2.toolbox;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.entrevista.ifood2.R;
import com.entrevista.ifood2.network.bean.PaymentMethod;
import com.entrevista.ifood2.presentation.presenter.cart.MethodPaymentAdapter;
import com.entrevista.ifood2.presentation.ui.cart.CartActivity;

import java.util.List;

/**
 * Created by JCARAM on 08/10/2017.
 */

public class AlertDialogBuilder {

    public static AlertDialog alertDialogProgress(Context context, int layoutId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(layoutId, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        return dialogBuilder.create();
    }

    public static AlertDialog alertDialogWithButtonConfirmAndCancel(Context context, String msg,
                                                                    String buttonConfirmText,
                                                                    String buttonCancelText,
                                                                    DialogInterface.OnClickListener onClickListener,
                                                                    DialogInterface.OnClickListener onCancelListener) {
        return new AlertDialog.Builder(context)
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(buttonCancelText, onCancelListener)
                .setPositiveButton(buttonConfirmText, onClickListener)
                .create();
    }

    public static AlertDialog alertDialogTryAgain(Context context, String msg,
                                                  String buttonConfirmText,
                                                  String buttonCancelText,
                                                  DialogInterface.OnClickListener onClickListener,
                                                  DialogInterface.OnClickListener onCancelListener) {
        return new AlertDialog.Builder(context)
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(buttonCancelText, onCancelListener)
                .setPositiveButton(buttonConfirmText, onClickListener)
                .create();
    }


    public static AlertDialog alertDialogCustomPaymentMethod(Context context,
                                                             String msg,
                                                             List<PaymentMethod> paymentMethods,
                                                             DialogInterface.OnClickListener onPositiveButtonListener,
                                                             AdapterView.OnItemSelectedListener onItemSelectedListener) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(msg);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_spinner, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        Spinner spinner = dialogView.findViewById(R.id.spinner);
        spinner.setAdapter(new MethodPaymentAdapter(context, paymentMethods));
        spinner.setOnItemSelectedListener(onItemSelectedListener);
        dialogBuilder.setPositiveButton("COMPRAR",onPositiveButtonListener);
        dialogBuilder.setNegativeButton("CANCELAR", null);

        return dialogBuilder.create();
    }
}
