package com.pataa.sdk;

import static com.pataa.sdk.Utill.isNetworkConnected;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CreatePataaActivity extends Activity {
    private static String TAG_PARAM_WEB_URL = "TAG_PARAM_WEB_URL";
    private static String TAG_PARAM_API_KEY = "TAG_PARAM_API_KEY";
    private final int RP_ACCESS_LOCATION = 1001;
    private final int RP_ACCESS_LOCATION_PERMISSION = 1002;
    private final int RP_ACCESS_LOCATION_PERMISSION_SETTING = 1003;
    private WebView webView;
    private ProgressBar progressBar;
    private static DialogCallback callback;
    private String mGeolocationOrigin;
    private GeolocationPermissions.Callback mGeolocationCallback;
    private final String permission = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inapp_web);
        progressBar = findViewById(R.id.progressBar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissionLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void openCreatePataaFlow() {
        webView = findViewById(R.id.webView);
        String webUrl = getIntent().getExtras().getString(TAG_PARAM_WEB_URL);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setGeolocationEnabled(true);
        webView.loadUrl(webUrl);
        webView.setWebChromeClient(new WebChromeClient() {

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                        ContextCompat.checkSelfPermission(CreatePataaActivity.this, permission) == PackageManager.PERMISSION_GRANTED) {
                    // we're on SDK < 23 OR user has already granted permission
                    callback.invoke(origin, true, true);
                } else {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(CreatePataaActivity.this, permission)) {
//                        // user has denied this permission before and selected [/] DON'T ASK ME AGAIN
//                        // TODO Best Practice: show an AlertDialog explaining why the user could allow this permission, then ask again
//                    } else {
                    // ask the user for permissions
                    ActivityCompat.requestPermissions(CreatePataaActivity.this, new String[]{permission}, RP_ACCESS_LOCATION);
                    mGeolocationOrigin = origin;
                    mGeolocationCallback = callback;
//                    }
                }
            }
        });
        webView.setWebViewClient(
                new SSLWebViewClient()
        );
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface // For API 17+
            public void performClick(String strl) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Logger.e("create_pataa_data -> " + strl);
                        callback.Response(strl);
                        webView.clearCache(true);
                        finish();
                    }
                });

            }
        }, "createPataaCode");
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface // For API 17+
            public void performClick() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });
            }
        }, "closeWindow");

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RP_ACCESS_LOCATION_PERMISSION_SETTING: {
                openCreatePataaFlow();
                break;
            }
            case RP_ACCESS_LOCATION: {
                boolean allow = false;
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // user has allowed these permissions
                    allow = true;
                }
                if (mGeolocationCallback != null) {
                    if (allow) {
                        mGeolocationCallback.invoke(mGeolocationOrigin, allow, true);
                    } else {
                        mGeolocationCallback.invoke(mGeolocationOrigin, allow, false);
                    }
                }

                break;
            }
            case RP_ACCESS_LOCATION_PERMISSION: {
                if (grantResults.length == 0 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Please enable location permission", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //permission granted
                    openCreatePataaFlow();
                }
                break;
            }
        }

    }

    private void checkPermissionLocation() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, permission);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission)) {
                showExplanation();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, RP_ACCESS_LOCATION_PERMISSION);
            }
        } else {
            openCreatePataaFlow();
        }
    }

    private void showExplanation() {
        setIsLoading(false);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_dialog);
        bottomSheetDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                finish();
            }
        });
        bottomSheetDialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, RP_ACCESS_LOCATION_PERMISSION_SETTING);
//                finish();
            }
        });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

    }

    public static Intent createPataa(Context context, String webUrl, String apiKey, DialogCallback callbacks) {
        callback = callbacks;
        Intent intent = new Intent(context, CreatePataaActivity.class);
        intent.putExtra(TAG_PARAM_WEB_URL, webUrl);
        intent.putExtra(TAG_PARAM_API_KEY, apiKey);
        return intent;
    }

    class SSLWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Log.d("Error", error.toString());
            if (error.toString() == "piglet")
                handler.cancel();
            else
                handler.proceed(); // Ignore SSL certificate errors
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            setIsLoading(false);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            setIsLoading(true);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!isNetworkConnected(CreatePataaActivity.this)) {
                Toast.makeText(CreatePataaActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                Logger.e(getString(R.string.no_internet_connection));
                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            if (!isNetworkConnected(CreatePataaActivity.this)) {
                Toast.makeText(getApplicationContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                Logger.e(getString(R.string.no_internet_connection));
                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, request);
            }
        }
    }

    public void setIsLoading(boolean b) {
        if (b)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

}