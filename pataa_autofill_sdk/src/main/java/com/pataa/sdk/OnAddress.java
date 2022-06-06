package com.pataa.sdk;

public interface OnAddress {
        void onNetworkIsNotAvailable();

        void onPataaNotFound(GetPataaDetailResponse message);

        void onPataaFound(User user, Pataa response);

        void onError(int statusCode, String message);
    }