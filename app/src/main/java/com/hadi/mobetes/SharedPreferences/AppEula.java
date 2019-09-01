package com.hadi.mobetes.SharedPreferences;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;

import com.hadi.mobetes.R;

public class AppEula extends AppCompatActivity
{
    private String EULA_PREFIX;
    private Activity mContext;

    public AppEula(Activity context)
    {
        mContext = context;
    }

    private PackageInfo getPackageInfo()
    {
        PackageInfo info = null;

        try
        {
            info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return info;
    }

    public void show()
    {
        PackageInfo versionInfo = getPackageInfo();

        // The eulaKey changes every time you increment the version number in
        // the AndroidManifest.xml
        final String eulaKey = EULA_PREFIX + versionInfo.versionCode;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        boolean bAlreadyAccepted = prefs.getBoolean(eulaKey, false);
        if (bAlreadyAccepted == false)
        {
            // EULA title
            String title = mContext.getString(R.string.app_name) + versionInfo.versionName;

            // EULA text
            String message = mContext.getString(R.string.eula_string);

            // Disable orientation changes, to prevent parent activity
            // reinitialization
            // mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                    //.setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.accept,
                            new Dialog.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    // Mark this version as read.
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean(eulaKey, true);
                                    editor.commit();

                                    // Close dialog
                                    dialogInterface.dismiss();

                                    // Enable orientation changes based on
                                    // device's sensor
                                    mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new Dialog.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Close the activity as they have declined
                                    // the EULA
                                    mContext.finish();
                                    mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                                }

                            });
            builder.create().show();
        }
    }

    public void show2()
    {
        PackageInfo versionInfo = getPackageInfo();

        // The eulaKey changes every time you increment the version number in
        // the AndroidManifest.xml
        final String eulaKey = EULA_PREFIX + versionInfo.versionCode;
        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);

        // EULA title
        String title = mContext.getString(R.string.app_name) + versionInfo.versionName;

        // EULA text
        String message = mContext.getString(R.string.eula_string);

        // Disable orientation changes, to prevent parent activity
        // reinitialization
        // mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                //.setTitle(Html.fromHtml("<font color='#FB4748'>"+title+"</font>"))
                .setMessage(message)
                .setPositiveButton(R.string.accept, new Dialog.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        // Mark this version as read.
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean(eulaKey, true);
                        editor.commit();

                        // Close dialog
                        dialogInterface.dismiss();

                        // Enable orientation changes based on
                        // device's sensor
                        mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    }
                });
        builder.create().show();
        /*builder.create();
        Dialog d = builder.show();
        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setBackgroundColor(getResources().getColor(R.color.redLight));*/
    }
}
