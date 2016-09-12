package com.statickidz.versionchecker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by StaticKidz on 12/09/2016.
 * Require: compile 'org.jsoup:jsoup:1.9.2'
 */
public class VersionChecker {

    private Context context;
    private String currentVersion, latestVersion, packageName;
    private Dialog dialog;


    // Configuration
    private String updateButtonText;
    private String cancelButtonText;
    private String dialogMessageText;
    private String dialogTitleText;
    private boolean cancellable = true;

    // Testing
    private boolean isDemoMode = false;

    public VersionChecker(Context activity) {
        this.context = activity;
        this.getCurrentVersion();
    }

    public void check() {
        new GetLatestVersion().execute();
    }

    public void setUpdateButtonText(String updateButtonText) {
        this.updateButtonText = updateButtonText;
    }

    public void setCancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
    }

    public void setDialogMessageText(String dialogMessageText) {
        this.dialogMessageText = dialogMessageText;
    }

    public void setDialogTitleText(String dialogTitleText) {
        this.dialogTitleText = dialogTitleText;
    }

    public void setDemoMode() {
        this.isDemoMode = true;
    }

    private void getCurrentVersion() {
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo = null;

        try {
            packageName = context.getPackageName();
            pInfo = pm.getPackageInfo(context.getPackageName(), 0);
            currentVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }

    }

    public boolean isCancellable() {
        return this.cancellable;
    }

    public void setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + packageName).get();
                latestVersion = doc.getElementsByAttributeValue
                        ("itemprop", "softwareVersion").first().text();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    showUpdateDialog();
                }
            }

            if (isDemoMode) {
                showUpdateDialog();
                Log.v("VERSIONING", "Current Version: " + currentVersion);
                Log.v("VERSIONING", "Latest Version: " + latestVersion);
                Log.v("VERSIONING", "Package Name: " + packageName);
            }

            super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(dialogTitleText != null ?
                dialogTitleText : context.getString(R.string.version_check_new_update_available));
        builder.setMessage(dialogMessageText != null ?
                dialogMessageText : context.getString(R.string.version_check_message));

        builder.setPositiveButton(updateButtonText != null ?
                updateButtonText : context.getString(R.string.version_check_update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse
                            ("market://details?id=" + packageName));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        });

        if (!isCancellable()) {
            builder.setNegativeButton(cancelButtonText != null ?
                    cancelButtonText : context.getString(R.string.version_check_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
        }

        dialog = builder.show();
    }
}
