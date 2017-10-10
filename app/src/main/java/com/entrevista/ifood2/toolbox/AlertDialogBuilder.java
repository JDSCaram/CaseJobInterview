package com.entrevista.ifood2.toolbox;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.entrevista.ifood2.R;

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

}
