package cn.com.findfine.jddaojia.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUtil {
    public static void setLoginStatus(Context context, boolean loginStatus) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("login_status", loginStatus);
        editor.apply();
    }

    public static boolean getLoginStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sp.getBoolean("login_status", false);
    }

    public static void setIsInitData(Context context, boolean isInitData) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("is_init_data", isInitData);
        editor.apply();
    }

    public static boolean getIsInitData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sp.getBoolean("is_init_data", false);
    }

    public static void saveUserAccount(Context context, String userAccount) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_account", userAccount);
        editor.apply();
    }

    public static String getUserAccount(Context context) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sp.getString("user_account", "");
    }

    public static void saveUserId(Context context, String userId) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_id", userId);
        editor.apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sp.getString("user_id", "");
    }

    public static void saveUserPassword(Context context, String password) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("password", password);
        editor.apply();
    }

    public static String getUserPassword(Context context) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sp.getString("password", "");
    }

    public static void saveIpAddress(Context context, String ipAddress) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ip_address", ipAddress);
        editor.apply();
    }

    public static String getIpAddress(Context context) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sp.getString("ip_address", "");
    }

//    public static void saveAddress(Context context, JSONObject jsonObject) throws JSONException {
//        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        JSONArray address = getAddress(context);
//        if (address == null) {
//            address = new JSONArray();
//        }
//        address.put(jsonObject);
//        editor.putString("address", address.toString());
//        editor.apply();
//    }
//
//    public static void saveAddress(Context context, JSONArray jsonArray) throws JSONException {
//        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("address", jsonArray.toString());
//        editor.apply();
//    }
//
//    public static JSONArray getAddress(Context context) throws JSONException {
//        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
//        String address = sp.getString("address", "");
//        if (TextUtils.isEmpty(address)) {
//            return null;
//        }
//        return new JSONArray(address);
//    }
//
//    public static void deleteAddress(Context context, int index) throws JSONException {
//        JSONArray address = getAddress(context);
//        if (address != null && address.length() > 0){
//            address.remove(index);
//            saveAddress(context, address);
//        }
//    }
}
