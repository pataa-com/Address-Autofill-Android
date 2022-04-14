package com.pataa.sdk;

import static com.pataa.sdk.AppConstants.BASE_URL;
import static com.pataa.sdk.AppConstants.BASE_URL_DEV;
import static com.pataa.sdk.AppConstants.metaEnableDevelopmentKey;

import android.content.Context;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {


//    public static ApiInterface getClient() {
//        RestAdapter adapter = new RestAdapter.Builder()
//                .setEndpoint(AppConstants.BASE_URL).build();
//        ApiInterface api = adapter.create(ApiInterface.class);
//        return api;
//    }

    private static OkHttpClient.Builder httpClientBuilder = null;
    private static Retrofit retrofit = null;
    private static final String API_VERSION = "";

    public static Retrofit getInstance(Context context) {
        if (retrofit == null) {

            httpClientBuilder = getSSLContext(context);
            String url = Utill.getMetaBoolean(context, metaEnableDevelopmentKey()) ? BASE_URL_DEV : BASE_URL;
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(url + API_VERSION)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClientBuilder.build());

            retrofit = builder.build();

        }
        return retrofit;
    }

    public static ApiInterface getApi(Context context) {
        return getInstance(context).create(ApiInterface.class);
    }


    public static OkHttpClient.Builder getSSLContext(Context mContext) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();
        clientBuilder.connectTimeout(5, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(10, TimeUnit.SECONDS);
        clientBuilder.readTimeout(15, TimeUnit.SECONDS);
        try {

            TrustManager[] trustAllCerts = new TrustManager[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                trustAllCerts = new TrustManager[]{

                        new X509ExtendedTrustManager() {
                            @Override
                            public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {

                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {

                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {

                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {

                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                            }

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                            }
                        }
                };
            } else {
            }

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            clientBuilder.sslSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            clientBuilder.hostnameVerifier(allHostsValid);
            // Install the all-trusting host verifier
        } catch (Exception e) {
//            Log.e(TAG, "trustAllHosts: ", e);
        }
        return clientBuilder;


    }

}