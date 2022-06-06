package com.pataa.sdk;

public enum PataaErrorCodes {
    PATAA_FOUND(200),
    PATAA_NOT_FOUND(204),
    PATAA_LIMIT_EXIST(400),
    PATAA_ORIGIN_MISMATCHED(401),
    PATAA_INVALID_KEY(400);

    private int errorCode;

    PataaErrorCodes(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
