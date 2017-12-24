package com.company.radio.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.company.radio.R;

/**
 * Created by Ashiq on 8/29/2016.
 */
public class InputDialog {

    public static void showInputDialog(Activity activity, String defaultInput, String title, String positiveButtonText, String negativeButtonText, final InputDialogActionListener inputDialogActionListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.DialogTheme);

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptsView = layoutInflater.inflate(R.layout.layout_save_dialog, null);
        alertDialogBuilder.setView(promptsView);

        if (title != null) {
            alertDialogBuilder.setTitle(title);
        }

        final EditText fileName = (EditText) promptsView.findViewById(R.id.fileName);

        if(defaultInput != null) {
            fileName.setText(defaultInput);
            fileName.setSelection(defaultInput.length());
        }

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(positiveButtonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(inputDialogActionListener != null) {
                            inputDialogActionListener.onSave(fileName.getText().toString());
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton(negativeButtonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(inputDialogActionListener != null) {
                            inputDialogActionListener.onDiscard();
                        }
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public interface InputDialogActionListener {
        public void onSave(String fileName);
        public void onDiscard();
    }

}
