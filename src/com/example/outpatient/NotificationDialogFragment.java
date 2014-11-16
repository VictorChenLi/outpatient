package com.example.outpatient;

import com.outpatient.notification.service.NotificationHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

public class NotificationDialogFragment extends DialogFragment {
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
		final int rid = this.getActivity().getIntent().getExtras().getInt("rid");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.notification)
               .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       NotificationHelper.dismissNotificationReminder(getActivity(), rid);
                       getActivity().finish();
                   }
               })
               .setNegativeButton(R.string.snooze, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                	   NotificationHelper.snoozeNotification(getActivity(), rid);
                	   getActivity().finish();
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
