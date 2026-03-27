package com.example.smart_healthcare_application.api.repository;

import android.util.Log;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.api_config.ApiClient;
import com.example.smart_healthcare_application.api.request.ChangePasswordRequest;
import com.example.smart_healthcare_application.api.request.ForgotPasswordRequest;
import com.example.smart_healthcare_application.api.request.LoginRequest;

import com.example.smart_healthcare_application.api.request.ResetpasswordRequest;
import com.example.smart_healthcare_application.api.request.UpdateUserRequest;
import com.example.smart_healthcare_application.api.request.VerifyOtpRequest;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.api.services.UserService;
import com.example.smart_healthcare_application.models.User;
import com.example.smart_healthcare_application.utils.ApiErrorMessage;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final String INTERNET_DISCONNECT = "Lỗi kết nối đến máy chủ: ";
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
                    User user = response.body().getResult();
                    if (user.getRoles().contains("DOCTOR")) {
                        callback.onSuccess(user);
                    }
                    else {
                        callback.onError("Không có quyển truy cập");
                    }
                }
                else {
                    callback.onError(ApiErrorMessage.getErrorMessage(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                callback.onError(INTERNET_DISCONNECT + t.getMessage());
            }
        });
    }

    public void updateUser(String userId, UpdateUserRequest request, ApiCallback<User> callback) {
        userService.updateUser(userId, request).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                }
                else {
                    callback.onError(ApiErrorMessage.getErrorMessage(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                callback.onError(INTERNET_DISCONNECT + t.getMessage());
            }
        });
    }

    public void forgotPassword(String email, ApiCallback<Boolean> callback) {
        userService.forgotPassword(new ForgotPasswordRequest(email)).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                }
                else {
                    callback.onError(ApiErrorMessage.getErrorMessage(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                callback.onError(INTERNET_DISCONNECT + t.getMessage());
            }
        });
    }

    public void verifyOTP(String email, String otp, ApiCallback<Boolean> callback) {
        userService.verifyOTP(new VerifyOtpRequest(email, otp)).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                }
                else {
                    callback.onError(ApiErrorMessage.getErrorMessage(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                callback.onError(INTERNET_DISCONNECT + t.getMessage());
            }
        });
    }

    public void resetPassword(String email, String newPassword, ApiCallback<Boolean> callback) {
        userService.resetPassword(new ResetpasswordRequest(email, newPassword)).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                }
                else {
                    callback.onError(ApiErrorMessage.getErrorMessage(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                callback.onError(INTERNET_DISCONNECT + t.getMessage());
            }
        });
    }

    public void changePassword(String userId, String oldPassword, String newPassword, ApiCallback<User> callback) {
        userService.changePassword(new ChangePasswordRequest(userId, oldPassword, newPassword)).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                }
                else {
                    callback.onError(ApiErrorMessage.getErrorMessage(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                callback.onError(INTERNET_DISCONNECT + t.getMessage());
            }
        });
    }
}
