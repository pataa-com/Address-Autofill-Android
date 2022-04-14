package com.pataa.sdk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utill {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    public static boolean isNotNullOrEmpty(String string) {
        return !(string == null || string.isEmpty());
    }

    @Nullable
    public static String getMeta(Context context, String metaKey) {
        Object metaData = getMetaData(context, metaKey);
        return (metaData!=null && metaData instanceof String) ? (String) metaData : null;
    }

    @Nullable
    public static Boolean getMetaBoolean(Context context, String metaKey) {
        Object metaData = getMetaData(context, metaKey);
        return (metaData!=null && metaData instanceof Boolean) ? (Boolean) metaData : false;
    }

    private static Object getMetaData(Context context, String metaKey) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if ((null == applicationInfo) || (null == applicationInfo.metaData)) {
                Logger.e("applicationinfo -> " + applicationInfo);
                return null;
            }
            return applicationInfo.metaData.get(metaKey);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AlertDialog showWhatIsPataaDialog(Context context, WhatIsPataaDialogCallBack callBack) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_pataa_Dialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_what_is_pataa, null);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_corner_dialog_shadow_bg));

        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialog.getWindow().getAttributes());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        dialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        lWindowParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lWindowParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lWindowParams);
        dialog.show();
        dialog.setContentView(view);


        EditText editText = view.findViewById(R.id.edtPataaCode);
        ImageView btnCross = view.findViewById(R.id.btnCross);
        TextView tvCreatePataa = view.findViewById(R.id.tvCreatePataa);
        TextView tvDigitalTitle = view.findViewById(R.id.tvDigitalTitle);
        TextView tvPataaTwo = view.findViewById(R.id.tvPataaTwo);
        TextView tvPataaOne = view.findViewById(R.id.tvPataaOne);

//        if (showUnderLineOnPataa) {
//            tvPataaOne.setText(tvPataaOne.getText().toString());
//            tvPataaTwo.setText(tvPataaTwo.getText().toString());
//        }

        editText.setText("^KUMAR100");
        tvDigitalTitle.setSelected(true);
        tvCreatePataa.setSelected(true);

        btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callBack != null) callBack.onDismiss();
            }
        });

        tvCreatePataa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callBack != null) callBack.clickOnCreatePataa();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (callBack != null) callBack.onDismiss();
            }
        });

        return dialog;
    }

    public static String getAppHash(Context context){
        String hash_key = "";

        try {
            final PackageInfo info = context.getPackageManager()
                    .getPackageInfo( context.getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                final MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                final byte[] digest = md.digest();
                final StringBuilder toRet = new StringBuilder();
                for (int i = 0; i < digest.length; i++) {
                    if (i != 0) toRet.append(":");
                    int b = digest[i] & 0xff;
                    String hex = Integer.toHexString(b);
                    if (hex.length() == 1) toRet.append("0");
                    toRet.append(hex);
                }
                hash_key = toRet.toString();
                //Log.e(KeyHelper.class.getSimpleName(), key + " " + toRet.toString());
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Logger.e("name not found "+ e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Logger.e("no such an algorithm"+ e.toString());
        } catch (Exception e) {
            Logger.e("exception"+ e.toString());
        }
        Logger.e("SHA1 : "+ hash_key);
        return hash_key;
    }
}
