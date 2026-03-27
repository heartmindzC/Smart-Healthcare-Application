package com.example.smart_healthcare_application.utils;

import org.json.JSONObject;

import okhttp3.ResponseBody;

public class ApiErrorMessage {
    public static String getErrorMessage(ResponseBody errorBody) {
        if (errorBody == null) {
            return "Đã xảy ra lỗi không xác định";
        }
        try {
            String errorString = errorBody.string();
            JSONObject jObjError = new JSONObject(errorString);
            return jObjError.getString("message");

        } catch (Exception e) {
            return "Lỗi cấu trúc dữ liệu từ Server";
        }
    }
}
