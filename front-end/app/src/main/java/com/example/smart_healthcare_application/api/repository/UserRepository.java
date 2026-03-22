package com.example.smart_healthcare_application.api.repository;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.api_config.ApiClient;
import com.example.smart_healthcare_application.api.request.LoginRequest;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.api.services.UserService;
import com.example.smart_healthcare_application.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private UserService userService;
    private static UserRepository instance;

    private UserRepository() {
        userService = ApiClient.getClient().create(UserService.class);
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new UserRepository();
                }
            }
        }
        return instance;
    }

    // call cac endpoint cua api tai day
    public void login(String username, String password, ApiCallback<User> callback) {
        String type = "";
        if (username.contains("@gmail.com")) {
            type = "email";
        }
        else if (username.length() == 10) {
            type = "phone";
        }
        else if (username.length() == 12) {
            type = "nationalId";
        }
        else {
            callback.onError("Username not valid");
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        loginRequest.setType(type);

        userService.login(loginRequest).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                }
                else if (response.body() != null) {
                    callback.onError("Lỗi: " + response.body().getMessage());
                }
                else {
                    callback.onError("Lỗi máy chủ: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                callback.onError("Lỗi kết nối đến máy chủ: " + t.getMessage());
            }
        });
    }
}
