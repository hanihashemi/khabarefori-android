package ir.khabarefori.helper.update;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import ir.khabarefori.AppPath;
import ir.khabarefori.R;

/**
 * Created by Hani on 5/21/14.
 */

public class UpdateDialog {
    public AlertDialog dialog;

    public void show(final Context context) {
        dialog = new AlertDialog.Builder(context)
                .setMessage(R.string.update_question)
                .setCancelable(false)
                .setTitle(R.string.update_dialog_title)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(R.string.update_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UpdateManager.doNotUpdate = true;
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppPath.Network.getDownloadPage()));
                        context.startActivity(browserIntent);
                    }
                })
                .setPositiveButton(R.string.update_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UpdateManager.doNotUpdate = true;
                        dialog.dismiss();
                    }
                }).show();
    }
}