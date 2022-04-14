package com.pataa.sdk;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

//    @FormUrlEncoded
//    @POST(END_POINT_GET_PATAA_DETAIL)
//    @Headers(("Content-Type : application/json"))
//    void getPataaDetail(@Field("api_key") String api_key,
//                        @Field("pc") String name,
//                        @Field("app_key") String app_key,
//                        Callback<GetPataaDetailResponse> callback);

    @FormUrlEncoded
    @POST(AppConstants.END_POINT_GET_PATAA_DETAIL)
    Call<GetPataaDetailResponse> getPataaDetail(@Field("api_key") String api_key, @Field("pc") String pc, @Field("app_key") String app_key);
}