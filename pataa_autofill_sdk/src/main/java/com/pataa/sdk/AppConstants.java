package com.pataa.sdk;

public class AppConstants {
//    public static String BASE_URL_DEV = "https://pataa.in:5057/";
    public static String BASE_URL = "https://apiv1.pataa.com/";
    public static String BASE_URL_DEV = "https://pataa.in:5059/";

    public static int REFRESS_INTERVAL_FOR_VALIDATION_CHECK = 300;
    public static int REQUEST_KEY_CREATE_PATAA = 13531;
    public static final String END_POINT_GET_PATAA_DETAIL = "/get-pataa";
    public static final String ON_ACT_RSLT_PATAA_DATA = "pc_dt";

    public static String metaClientKey() {
        return "pataa.autofill.sdk.ClientKey";
    }

    public static String metaEnableDebugKey() {
        return "pataa.autofill.sdk.EnableLogger";
    }

    public static String metaEnableDevelopmentKey() {
        return "pataa.autofill.sdk.EnableDevelopment";
    }
}
