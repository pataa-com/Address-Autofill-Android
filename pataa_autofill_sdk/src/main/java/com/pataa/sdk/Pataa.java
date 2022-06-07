package com.pataa.sdk;

public class Pataa {
    private String pataa_code;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String zipcode;
    private String city_name;
    private String state_code;
    private String state_name;
    private String country_code;
    private String country_name;
    private String qr_code;

    public String getPataa_code() {
        return pataa_code;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getAddress3() {
        return address3;
    }

    public String getAddress4() {
        return address4;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getState_code() {
        return state_code;
    }

    public String getState_name() {
        return state_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public String getCountry_name() {
        return country_name;
    }

    public String getQr_code() {
        return qr_code;
    }

    public String getFormattedAddress() {
        StringBuilder builder = new StringBuilder();

        if (Utill.isNotNullOrEmpty(getAddress1())) {
            builder.append(getAddress1().trim());
        }
        if (Utill.isNotNullOrEmpty(getAddress2())) {
            builder.append(", " + getAddress2().trim());
        }
        if (Utill.isNotNullOrEmpty(getAddress3())) {
            builder.append(", " + getAddress3().trim());
        }
        if (Utill.isNotNullOrEmpty(getAddress4())) {
            builder.append(", " + getAddress4().trim());
        }
        if (Utill.isNotNullOrEmpty(getCity_name())) {
            builder.append(", " + getCity_name());
        }
        if (Utill.isNotNullOrEmpty(getZipcode())) {
            if (Utill.isNotNullOrEmpty(getAddress1()) || Utill.isNotNullOrEmpty(getAddress2()) || Utill.isNotNullOrEmpty(getAddress3())
                    || Utill.isNotNullOrEmpty(getAddress4()) || Utill.isNotNullOrEmpty(getCity_name())) {
                builder.append(" - " + getZipcode().trim());
            } else {
                builder.append(getZipcode().trim());
            }
        }
        if (Utill.isNotNullOrEmpty(getState_name())) {
            builder.append(", " + getState_name());
        }
        if (Utill.isNotNullOrEmpty(getCountry_name())) {
            builder.append(", " + getCountry_name());
        }

        String address = builder.toString().trim();
        if (Utill.isNotNullOrEmpty(address))
            if (address.indexOf(",") > 0) {
                return address;
            } else {
                return address.substring(1, address.length()).trim();
            }
        return "";
    }

    public String getFormattedAddressShort() {
        StringBuilder builder = new StringBuilder();

        if (Utill.isNotNullOrEmpty(getCity_name())) {
            builder.append(", " + getCity_name());
        }
        if (Utill.isNotNullOrEmpty(getZipcode())) {
            if (Utill.isNotNullOrEmpty(getAddress1()) || Utill.isNotNullOrEmpty(getAddress2()) || Utill.isNotNullOrEmpty(getAddress3())
                    || Utill.isNotNullOrEmpty(getAddress4()) || Utill.isNotNullOrEmpty(getCity_name())) {
                builder.append(" " + getZipcode().trim());
            } else {
                builder.append(getZipcode().trim());
            }
        }
        if (Utill.isNotNullOrEmpty(getState_name())) {
            builder.append(", " + getState_name());
        }
        if (Utill.isNotNullOrEmpty(getCountry_name())) {
            builder.append(", " + getCountry_name());
        }

        String address = builder.toString().trim();
        if (Utill.isNotNullOrEmpty(address))
            if (address.indexOf(",") > 0) {
                return address;
            } else {
                return address.substring(1, address.length()).trim();
            }
        return "";
    }
}