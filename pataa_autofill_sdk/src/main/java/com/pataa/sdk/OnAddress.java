package com.pataa.sdk;

public interface OnAddress {
        void onNetworkIsNotAvailable();

        void onPataaNotFound(String message);

        void onPataaFound(User user, Pataa response);
    }