package com.pataa.sdk;

import static com.pataa.sdk.AppConstants.ON_ACT_RSLT_PATAA_DATA;
import static com.pataa.sdk.Utill.isNetworkConnected;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CreatePataaActivity extends Activity {
    private static String TAG_PARAM_WEB_URL = "TAG_PARAM_WEB_URL";
    private static String TAG_PARAM_API_KEY = "TAG_PARAM_API_KEY";
    private final int RP_ACCESS_LOCATION = 1001;
    private final int RP_ACCESS_LOCATION_PERMISSION = 1002;
    private WebView webView;
    private static DialogCallback callback;
    private String mGeolocationOrigin;
    private GeolocationPermissions.Callback mGeolocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inapp_web);

        checkPermission();
    }

    private void openCreatePataaFlow() {
        webView = findViewById(R.id.webView);
        String webUrl = getIntent().getExtras().getString(TAG_PARAM_WEB_URL);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebContentsDebuggingEnabled(true);

        webView.loadUrl(webUrl);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setGeolocationEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {

                final String permission = Manifest.permission.ACCESS_FINE_LOCATION;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                        ContextCompat.checkSelfPermission(CreatePataaActivity.this, permission) == PackageManager.PERMISSION_GRANTED) {
                    // we're on SDK < 23 OR user has already granted permission
                    callback.invoke(origin, true, false);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CreatePataaActivity.this, permission)) {
                        // user has denied this permission before and selected [/] DON'T ASK ME AGAIN
                        // TODO Best Practice: show an AlertDialog explaining why the user could allow this permission, then ask again
                    } else {
                        // ask the user for permissions
                        ActivityCompat.requestPermissions(CreatePataaActivity.this, new String[]{permission}, RP_ACCESS_LOCATION);
                        mGeolocationOrigin = origin;
                        mGeolocationCallback = callback;
                    }
                }
            }
        });

        webView.setWebViewClient(
                new SSLWebViewClient()
        );

        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface // For API 17+
            public void performClick(String strl) {
                Logger.e("create_pataa_data -> " + strl);
//                sendOnlyPataaCode(strl);
                callback.Response(strl);
                finish();
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
            case RP_ACCESS_LOCATION: {
                boolean allow = false;
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // user has allowed these permissions
                    allow = true;
                }
                if (mGeolocationCallback != null) {
                    mGeolocationCallback.invoke(mGeolocationOrigin, allow, false);
                }

                break;
            }
            case RP_ACCESS_LOCATION_PERMISSION: {
                if (grantResults.length == 0 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG", "User denied ACCESS_COARSE_LOCATION permission.");
                    Toast.makeText(this, "User denied permission.", Toast.LENGTH_SHORT).show();
                } else {
                    //  Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "User granted ACCESS_COARSE_LOCATION permission.");
                    openCreatePataaFlow();
                }
                break;
            }
        }

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            openCreatePataaFlow();
        } else {
//            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, RP_ACCESS_LOCATION_PERMISSION);
        }
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

    }

}