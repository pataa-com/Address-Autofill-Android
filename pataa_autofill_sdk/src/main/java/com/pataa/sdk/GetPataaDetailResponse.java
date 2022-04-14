package com.pataa.sdk;

import java.util.ArrayList;

public class GetPataaDetailResponse {

    private int status;
    private String msg;
    private Result result;

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public Result getResult() {
        return result;
    }

    public static class Result {
        private User user;
        private Pataa pataa;
        private ArrayList<Object> pc_img;
        private ArrayList<Object> landmarks;

        public User getUser() {
            return user;
        }

        public Pataa getPataa() {
            return pataa;
        }

        public ArrayList<Object> getPc_img() {
            return pc_img;
        }

        public ArrayList<Object> getLandmarks() {
            return landmarks;
        }
    }

//    public static class User {
//        private String first_name;
//        private String last_name;
//        private String country_code;
//        private String mobile;
//
//        public String getFirst_name() {
//            return first_name;
//        }
//
//        public String getLast_name() {
//            return last_name;
//        }
//
//        public String getCountry_code() {
//            return country_code;
//        }
//
//        public String getMobile() {
//            return mobile;
//        }
//    }

//    public static class Pataa {
//        private String pataa_code;
//        private String address1;
//        private String address2;
//        private String address3;
//        private String address4;
//        private String zipcode;
//        private String city_name;
//        private String state_code;
//        private String state_name;
//        private String country_code;
//        private String country_name;
//        private String qr_code;
//
//        public String getPataa_code() {
//            return pataa_code;
//        }
//
//        public String getAddress1() {
//            return address1;
//        }
//
//        public String getAddress2() {
//            return address2;
//        }
//
//        public String getAddress3() {
//            return address3;
//        }
//
//        public String getAddress4() {
//            return address4;
//        }
//
//        public String getZipcode() {
//            return zipcode;
//        }
//
//        public String getCity_name() {
//            return city_name;
//        }
//
//        public String getState_code() {
//            return state_code;
//        }
//
//        public String getState_name() {
//            return state_name;
//        }
//
//        public String getCountry_code() {
//            return country_code;
//        }
//
//        public String getCountry_name() {
//            return country_name;
//        }
//
//        public String getQr_code() {
//            return qr_code;
//        }
//
//        public String getFormattedAddress() {
//            StringBuilder builder = new StringBuilder();
//
//            if (Utill.isNotNullOrEmpty(getAddress1())) {
//                builder.append(getAddress1().trim());
//            }
//            if (Utill.isNotNullOrEmpty(getAddress2())) {
//                builder.append(", " + getAddress2().trim());
//            }
//            if (Utill.isNotNullOrEmpty(getAddress3())) {
//                builder.append(", " + getAddress3().trim());
//            }
//            if (Utill.isNotNullOrEmpty(getAddress4())) {
//                builder.append(", " + getAddress4().trim());
//            }
//            if (Utill.isNotNullOrEmpty(getCity_name())) {
//                builder.append(", " + getCity_name());
//            }
//            if (Utill.isNotNullOrEmpty(getZipcode())) {
//                if (Utill.isNotNullOrEmpty(getAddress1()) || Utill.isNotNullOrEmpty(getAddress2()) || Utill.isNotNullOrEmpty(getAddress3())
//                        || Utill.isNotNullOrEmpty(getAddress4()) || Utill.isNotNullOrEmpty(getCity_name())) {
//                    builder.append(" - " + getZipcode().trim());
//                } else {
//                    builder.append(getZipcode().trim());
//                }
//            }
//            if (Utill.isNotNullOrEmpty(getState_name())) {
//                builder.append(", " + getState_name());
//            }
//            if (Utill.isNotNullOrEmpty(getCountry_name())) {
//                builder.append(", " + getCountry_name());
//            }
//
//            String address = builder.toString().trim();
//            if (Utill.isNotNullOrEmpty(address))
//                if (address.indexOf(",") > 0) {
//                    return address;
//                } else {
//                    return address.substring(1, address.length()).trim();
//                }
//            return "";
//        }
//
//        public String getFormattedAddressShort() {
//            StringBuilder builder = new StringBuilder();
//
//            if (Utill.isNotNullOrEmpty(getCity_name())) {
//                builder.append(", " + getCity_name());
//            }
//            if (Utill.isNotNullOrEmpty(getZipcode())) {
//                if (Utill.isNotNullOrEmpty(getAddress1()) || Utill.isNotNullOrEmpty(getAddress2()) || Utill.isNotNullOrEmpty(getAddress3())
//                        || Utill.isNotNullOrEmpty(getAddress4()) || Utill.isNotNullOrEmpty(getCity_name())) {
//                    builder.append(" - " + getZipcode().trim());
//                } else {
//                    builder.append(getZipcode().trim());
//                }
//            }
//            if (Utill.isNotNullOrEmpty(getState_name())) {
//                builder.append(", " + getState_name());
//            }
//            if (Utill.isNotNullOrEmpty(getCountry_name())) {
//                builder.append(", " + getCountry_name());
//            }
//
//            String address = builder.toString().trim();
//            if (Utill.isNotNullOrEmpty(address))
//                if (address.indexOf(",") > 0) {
//                    return address;
//                } else {
//                    return address.substring(1, address.length()).trim();
//                }
//            return "";
//        }
//    }
}
