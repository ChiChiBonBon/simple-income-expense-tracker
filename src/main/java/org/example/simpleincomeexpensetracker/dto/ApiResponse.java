package org.example.simpleincomeexpensetracker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
//@Data生成以下
//getter、setter
//equals()、hashCode()
//toString()
//RequiredArgsConstructor:必要參數建構子,只包含「必要的」欄位（final 或標註了 @NonNull 的欄位）
@Data
@NoArgsConstructor
public class ApiResponse<T> {
    //成功
    private boolean success;
    //訊息
    private String message;

    //資料
    private T data;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "操作成功", data);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data);
    }
}
