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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class CreatePataaActivity extends AppCompatActivity {
    private static String TAG_PARAM_WEB_URL = "TAG_PARAM_WEB_URL";
    private static String TAG_PARAM_API_KEY = "TAG_PARAM_API_KEY";
    private WebView webView;
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//                if (isGranted) {
//                } else {
//                }
                openCreatePataaFlow();
            });

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
                callback.invoke(origin, true, false);
            }
        });

        webView.setWebViewClient(
                new SSLWebViewClient()
        );

        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface // For API 17+
            public void performClick(String strl) {
                Logger.e("create_pataa_data -> " + strl);
                sendOnlyPataaCode(strl);
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

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            openCreatePataaFlow();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    public static Intent createPataa(Context context, String webUrl, String apiKey) {
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

//    public void getPataadetail(String pataa) {
//        String API_KEY = null;
//        try {
//            API_KEY = getIntent().getStringExtra("TAG_PARAM_API_KEY");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (Utill.isNotNullOrEmpty(API_KEY) && Utill.isNotNullOrEmpty(pataa)) {
//            Api.getClient().getPataaDetail(API_KEY,
////                    pataa.trim().toUpperCase(),
//                    "KUMAR100",
//                    new Callback<GetPataaDetailResponse>() {
//                        @Override
//                        public void success(GetPataaDetailResponse getPataaDetail, Response response) {
//                            Logger.e("API responded successfully" + new Gson().toJson(getPataaDetail));
//
//                            if (getPataaDetail.status == 200) {
//                                Intent resultIntent = new Intent();
//                                resultIntent.putExtra("pataa_data", new Gson().toJson(getPataaDetail.getResult()).toString());
//                                setResult(Activity.RESULT_OK, resultIntent);
//                                finish();
//                            } else {
//                                Logger.e("API failed" + new Gson().toJson(getPataaDetail));
//                                sendOnlyPataaCode(pataa);
//                            }
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//                            Logger.e("API failed" + new Gson().toJson(error));
//                            sendOnlyPataaCode(pataa);
//                        }
//                    });
//        } else {
//            Logger.e("unble to use given API_KEY");
//        }
//
//    }

    private void sendOnlyPataaCode(String pataa) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ON_ACT_RSLT_PATAA_DATA, pataa);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}